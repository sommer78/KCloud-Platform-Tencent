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
package org.laokou.auth.server.domain.sys.repository.service;
import java.util.List;

/**
 * @author Kou Shenhai
 */
public interface SysDeptService {

    /**
     * 根据userId查询deptIds
     * @param userId
     * @return
     */
    List<Long> getDeptIdsByUserId(Long userId);

    /**
     * 查询deptIds
     * @return
     */
    List<Long> getDeptIds();

}