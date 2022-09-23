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
package org.laokou.log.event;

import org.laokou.common.dto.OperateLogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * 操作日志事件
 */
public class OperateLogEvent extends ApplicationEvent {
    public OperateLogEvent(OperateLogDTO source) {
        super(source);
    }
}
