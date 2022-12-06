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
package org.laokou.rocketmq.client.constant;
/**
 * 队列常量值
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/18 0018 上午 7:07
 */
public interface RocketmqConstant {

    /**
     * 操作日志消息
     */
    String LAOKOU_OPERATE_LOG_TOPIC = "laokou-operate-log-topic";

    /**
     * 登录日志消息
     */
    String LAOKOU_LOGIN_LOG_TOPIC = "laokou-login-log-topic";

    /**
     * 资源审批消息
     */
    String LAOKOU_AUDIT_RESOURCE_TOPIC = "laokou-audit-resource-topic";

    /**
     * 资源同步消息
     */
    String LAOKOU_SYNC_RESOURCE_TOPIC = "laokou-sync-resource-topic";

    /**
     * 通知消息
     */
    String LAOKOU_NOTICE_MESSAGE_TOPIC = "laokou-notice-message-topic";

}
