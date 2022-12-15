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
package org.laokou.common.mybatisplus.entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Kou Shenhai
 */
@Data
public abstract class BasePage {

    @NotNull(message = "请填写显示页数")
    private Integer pageNum;

    @NotNull(message = "请填写显示条数")
    private Integer pageSize;

    /**
     * sql拼接
     */
    private String sqlFilter;

}