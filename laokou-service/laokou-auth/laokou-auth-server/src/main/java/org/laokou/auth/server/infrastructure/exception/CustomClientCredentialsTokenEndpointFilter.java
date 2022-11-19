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
package org.laokou.auth.server.infrastructure.exception;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Kou Shenhai
 */
public class CustomClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {
    private final AuthorizationServerSecurityConfigurer configurer;
    private AuthenticationEntryPoint authenticationEntryPoint;
 
    public CustomClientCredentialsTokenEndpointFilter(AuthorizationServerSecurityConfigurer configurer) {
        this.configurer = configurer;
    }
 
    @Override
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
 
    @Override
    protected AuthenticationManager getAuthenticationManager() {
        return configurer.and().getSharedObject(AuthenticationManager.class);
    }
 
    @Override
    public void afterPropertiesSet() {
        setAuthenticationFailureHandler((request, response, exception) -> authenticationEntryPoint.commence(request, response, exception));
        setAuthenticationSuccessHandler((request, response, authentication) -> {
            // no-op - just allow filter chain to continue to token endpoint
        });
    }
}