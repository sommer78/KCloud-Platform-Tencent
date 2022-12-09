/**
 * Copyright (c) 2022 KCloud-Platform-Official Authors. All Rights Reserved.
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

package org.laokou.rocketmq.consumer.core.log;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.log.client.dto.LoginLogDTO;
import org.laokou.rocketmq.client.constant.RocketmqConstant;
import org.laokou.rocketmq.consumer.feign.log.LogApiFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author Kou Shenhai
 */
@RocketMQMessageListener(consumerGroup = "laokou-consumer-group-1", topic = RocketmqConstant.LAOKOU_LOGIN_LOG_TOPIC)
@Component
@RequiredArgsConstructor
@Slf4j
public class LoginLogConsumer implements RocketMQListener<String> {

    private final LogApiFeignClient logApiFeignClient;

    @Override
    public void onMessage(String message) {
        try {
            final LoginLogDTO loginLogDTO = JacksonUtil.toBean(message, LoginLogDTO.class);
            logApiFeignClient.login(loginLogDTO);
        } catch (FeignException e) {
            log.error("错误信息:{}",e.getMessage());
        }
    }
}
