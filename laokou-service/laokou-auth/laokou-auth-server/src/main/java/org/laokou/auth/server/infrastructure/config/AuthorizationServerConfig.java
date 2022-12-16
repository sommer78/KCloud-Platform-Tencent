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
package org.laokou.auth.server.infrastructure.config;
import org.laokou.auth.server.infrastructure.filter.ValidateInfoFilter;
import org.laokou.auth.server.infrastructure.provider.UsernamePasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * Spring Security配置
 * SpringSecurity最新版本更新
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/5/28 0028 上午 10:33
 */
@Configuration
public class AuthorizationServerConfig {

    /**
     * https://docs.spring.io/spring-authorization-server/docs/current/reference/html/configuration-model.html
     * @param http
     * @param validateInfoFilter
     * @return
     * @throws Exception
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http
            , ValidateInfoFilter validateInfoFilter
            , UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider) throws Exception {
        // 加载默认配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http
                .addFilterBefore(validateInfoFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(usernamePasswordAuthenticationProvider).build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        InMemoryRegisteredClientRepository inMemoryRegisteredClientRepository = new InMemoryRegisteredClientRepository(
                RegisteredClient.withId("client_auth")
                        .id("client_auth")
                        .clientId("client_auth")
                        .clientSecret("{noop}secret")
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .authorizationGrantType(new AuthorizationGrantType(OAuth2ParameterNames.PASSWORD))
                        .clientName("认证客户端")
                        .scope("auth")
                        .build()
        );
        return inMemoryRegisteredClientRepository;
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

}
