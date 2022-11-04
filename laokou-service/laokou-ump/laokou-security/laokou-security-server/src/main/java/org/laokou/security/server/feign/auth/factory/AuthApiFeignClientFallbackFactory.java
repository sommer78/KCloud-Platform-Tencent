/**
 * Copyright (c) 2022 KCloud-Platform-Official Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.security.server.feign.auth.factory;
import org.laokou.security.server.feign.auth.fallback.AuthApiFeignClientFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
/**
 * 回调工厂
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/5 0005 上午 12:12
 */
@Component
public class AuthApiFeignClientFallbackFactory implements FallbackFactory<AuthApiFeignClientFallback> {

    @Override
    public AuthApiFeignClientFallback create(Throwable cause) {
        return new AuthApiFeignClientFallback(cause);
    }

}