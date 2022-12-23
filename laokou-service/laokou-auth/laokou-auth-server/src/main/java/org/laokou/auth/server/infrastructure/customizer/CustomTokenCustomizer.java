/**
 * Copyright (c) 2022 KCloud-Platform-Tencent Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.auth.server.infrastructure.customizer;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 扩展信息
 * @author laokou
 */
public class CustomTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
//        JwtClaimsSet.Builder claims = context.getClaims();
//        RegisteredClient registeredClient = context.getRegisteredClient();
//        String clientId = registeredClient.getClientId();
//        String grantType = context.getAuthorizationGrantType().getValue();
//        // 写入clientId 和 grantType
//        claims.claim(OAuth2ParameterNames.CLIENT_ID,clientId);
//        claims.claim(OAuth2ParameterNames.GRANT_TYPE,grantType);
    }
}
