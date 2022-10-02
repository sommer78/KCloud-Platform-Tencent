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
package org.laokou.log.feign.rabbitmq;
import org.laokou.common.constant.ServiceConstant;
import org.laokou.common.dto.MqDTO;
import org.laokou.log.feign.rabbitmq.factory.RabbitMqApiFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kou Shenhai
 */
@FeignClient(name = ServiceConstant.LAOKOU_RABBITMQ,path = "/rabbitmq", fallback = RabbitMqApiFeignClientFallbackFactory.class)
@Service
public interface RabbitMqApiFeignClient {

    /**
     * @param routingKey: 路由键
     * @param dto:    消息内容（Json格式）
     * @Description: RabbitMQ发送消息(使用默认的交换机)
     */
    @PostMapping("/send/{routingKey}")
    Boolean sendMsg(@PathVariable("routingKey") String routingKey, @RequestBody MqDTO dto);

}
