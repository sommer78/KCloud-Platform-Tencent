/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
package org.laokou.ump.server.feign.auth;
import org.laokou.auth.client.dto.LoginDTO;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.auth.client.vo.LoginVO;
import org.laokou.common.constant.Constant;
import org.laokou.common.constant.ServiceConstant;
import org.laokou.common.utils.HttpResultUtil;
import org.laokou.ump.server.feign.auth.factory.AuthApiFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author Kou Shenhai
 */
@FeignClient(name = ServiceConstant.LAOKOU_AUTH, fallbackFactory = AuthApiFeignClientFallbackFactory.class)
@Service
public interface AuthApiFeignClient {

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    @PostMapping("/sys/auth/api/login")
    HttpResultUtil<LoginVO> login(@RequestBody LoginDTO loginDTO);

    /**
     * 验证码
     * @param uuid
     */
    @GetMapping("/sys/auth/api/captcha")
    void captcha(@RequestParam(Constant.UUID)String uuid);

    /**
     * 退出
     * @return
     */
    @GetMapping("/sys/auth/api/logout")
    void logout();

    /**
     * 用户详情
     * @param token
     * @return
     */
    @GetMapping(value = "/sys/auth/api/userDetail")
    HttpResultUtil<UserDetail> userDetail(@RequestParam(Constant.AUTHORIZATION_HEAD)String token);

}
