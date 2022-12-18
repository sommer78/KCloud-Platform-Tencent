/**
 * Copyright (c) 2022 KCloud-Platform-Official Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.auth.server.application.service.impl;
import cn.hutool.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.server.infrastructure.token.AuthenticationToken;
import org.laokou.auth.server.infrastructure.token.OAuth2Token;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.exception.CustomException;
import org.laokou.common.core.exception.ErrorCode;
import org.laokou.auth.server.application.service.SysAuthApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.*;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/**
 * SpringSecurity最新版本更新
 * @author Kou Shenhai
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysAuthApplicationServiceImpl implements SysAuthApplicationService {
    private final RegisteredClientRepository registeredClientRepository;
    private static final OAuth2AccessTokenGenerator OAUTH2_ACCESS_TOKEN_GENERATOR = new OAuth2AccessTokenGenerator();
    private static final OAuth2RefreshTokenGenerator OAUTH2_REFRESH_TOKEN_GENERATOR = new OAuth2RefreshTokenGenerator();
    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    @Override
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1.验证认证相关信息
        RegisteredClient registeredClient = loginBefore(request);
        // 2.登录
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = login(request);
        // 3.生成token
        loginAfter(registeredClient,usernamePasswordAuthenticationToken,response,request);
    }

    private RegisteredClient loginBefore(HttpServletRequest request) {
        // 1.验证clientId
        String clientId = request.getParameter(OAuth2ParameterNames.CLIENT_ID);
        if (StringUtil.isEmpty(clientId)) {
            throw new CustomException(ErrorCode.INVALID_CLIENT);
        }
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new CustomException(ErrorCode.INVALID_CLIENT);
        }
        // 2.验证clientSecret
        String secret = request.getParameter(OAuth2ParameterNames.CLIENT_SECRET);
        String clientSecret = registeredClient.getClientSecret();
        if (!Objects.equals(clientSecret,secret)) {
            throw new CustomException(ErrorCode.INVALID_CLIENT);
        }
        // 3.验证scope
        String scope = request.getParameter(OAuth2ParameterNames.SCOPE);
        Set<String> scopes = registeredClient.getScopes();
        if (StringUtil.isEmpty(scope)) {
            throw new CustomException(ErrorCode.INVALID_SCOPE);
        }
        List<String> scopeList = Arrays.asList(scope.split(Constant.COMMA));
        for (String s : scopeList) {
            if (!scopes.contains(s)) {
                throw new CustomException(ErrorCode.INVALID_SCOPE);
            }
        }
        return registeredClient;
    }

    private UsernamePasswordAuthenticationToken login(HttpServletRequest request) {
        return authenticationToken(request).login(request);
    }

    private AuthenticationToken authenticationToken(HttpServletRequest request) {
        // 1.验证grantType
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (StringUtil.isEmpty(grantType)) {
            throw new CustomException(ErrorCode.UNSUPPORTED_GRANT_TYPE);
        }
        try {
            String className = AuthenticationToken.class.getSimpleName();
            AuthenticationToken authenticationToken = SpringContextUtil.getBean(grantType + className, AuthenticationToken.class);
            // 2.验证账号/密码/验证码 或 手机号/验证码等等
            return authenticationToken;
        } catch (NoSuchBeanDefinitionException e ) {
            throw new CustomException(ErrorCode.UNSUPPORTED_GRANT_TYPE);
        }
    }

    private void loginAfter(RegisteredClient registeredClient
            , UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
            , HttpServletResponse response
            , HttpServletRequest request) throws IOException {
        // 1.生成token（access_token + refresh_token）
        // 获取认证类型
        AuthorizationGrantType grantType = authenticationToken(request).getGrantType();
        // 获取上下文
        DefaultOAuth2TokenContext.Builder builder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthenticationToken)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(grantType);
        DefaultOAuth2TokenContext context = builder.build();
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthenticationToken.getName())
                .authorizationGrantType(grantType);
        // 生成access_token
        OAuth2AccessToken generatedOAuth2AccessToken = OAUTH2_ACCESS_TOKEN_GENERATOR.generate(context);
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER
                , generatedOAuth2AccessToken.getTokenValue()
                , generatedOAuth2AccessToken.getIssuedAt()
                , generatedOAuth2AccessToken.getExpiresAt()
                , context.getAuthorizedScopes());
        authorizationBuilder.accessToken(oAuth2AccessToken);
        // 生成refresh_token
        context = builder
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                .build();
        OAuth2RefreshToken generateOAuth2RefreshToken = OAUTH2_REFRESH_TOKEN_GENERATOR.generate(context);
        authorizationBuilder.refreshToken(generateOAuth2RefreshToken);
        OAuth2Authorization oAuth2Authorization = authorizationBuilder.build();
        // 放入内存
        oAuth2AuthorizationService.save(oAuth2Authorization);
        log.info("获取{},",oAuth2Authorization);
        // 2.响应给前端
        String accessToken = generatedOAuth2AccessToken.getTokenValue();
        String refreshToken = generateOAuth2RefreshToken.getTokenValue();
        Instant expiresAt = generatedOAuth2AccessToken.getExpiresAt();
        Instant issuedAt = generatedOAuth2AccessToken.getIssuedAt();
        String tokenType = generatedOAuth2AccessToken.getTokenType().getValue();
        long expireIn = ChronoUnit.SECONDS.between(issuedAt, expiresAt);
        handleResponse(response,new OAuth2Token(accessToken,refreshToken,tokenType,expireIn));
    }

    /**
     * 响应给前端
     * @param response
     * @throws IOException
     */
    private void handleResponse(HttpServletResponse response,Object obj) throws IOException {
        response.setStatus(HttpStatus.HTTP_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(JacksonUtil.toJsonStr(new HttpResultUtil<>().ok(obj)));
        writer.flush();
    }

    @Override
    public String captcha(HttpServletRequest request) {
        return authenticationToken(request).captcha(request);
    }

}
