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
package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.client.dto.AuditDTO;
import org.laokou.admin.server.interfaces.qo.TaskQo;
import org.laokou.admin.client.vo.StartProcessVO;
import org.laokou.admin.client.vo.TaskVO;

import java.io.IOException;

/**
 * @author Kou Shenhai
 */
public interface WorkflowProcessApplicationService {

    /**
     * 开启任务
     * @param processKey
     * @param businessKey
     * @param instanceName
     * @return
     */
    StartProcessVO startResourceProcess(String processKey,String businessKey,String instanceName);

    /**
     * 分页查询待我审批任务
     * @param qo
     * @return
     */
    IPage<TaskVO> queryResourceTaskPage(TaskQo qo);

    /**
     * 审批任务
     * @param dto
     * @throws IOException
     * @return
     */
    Boolean auditResourceTask(AuditDTO dto) throws IOException;
}
