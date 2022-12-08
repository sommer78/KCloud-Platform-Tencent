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
package org.laokou.flowable.server.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/7 0007 下午 5:34
 */
@Data
public class TaskVO {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务key
     */
    private String taskDefinitionKey;

    /**
     * 任务执行人名称
     */
    private String assigneeName;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 流程定义id
     */
    private String definitionId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 实例名称
     */
    private String processInstanceName;

    /**
     * 业务主键
     */
    private String businessKey;
}