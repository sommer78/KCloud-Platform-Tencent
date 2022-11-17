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
package org.laokou.admin.server.interfaces.controller;
import org.laokou.admin.server.application.service.SysMonitorApplicationService;
import org.laokou.admin.client.vo.CacheVO;
import org.laokou.admin.server.infrastructure.server.Server;
import org.laokou.common.core.utils.HttpResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/27 0027 下午 3:13
 */
@RestController
@Api(value = "系统监控API",protocols = "http",tags = "系统监控API")
@RequestMapping("/sys/monitor/api")
public class SysMonitorApiController {

    @Autowired
    private SysMonitorApplicationService sysMonitorApplicationService;

    @GetMapping("/cache")
    @ApiOperation("系统监控>缓存")
    public HttpResultUtil<CacheVO> redis() {
        return new HttpResultUtil<CacheVO>().ok(sysMonitorApplicationService.getCacheInfo());
    }

    @GetMapping("/server")
    @ApiOperation("系统监控>主机")
    public HttpResultUtil<Server> server() throws Exception {
        return new HttpResultUtil<Server>().ok(sysMonitorApplicationService.getServerInfo());
    }

}
