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
package org.laokou.rocketmq.consumer.feign.elasticsearch;

import org.laokou.common.core.constant.ServiceConstant;
import org.laokou.elasticsearch.client.model.ElasticsearchModel;
import org.laokou.rocketmq.consumer.feign.elasticsearch.factory.ElasticsearchApiFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * @author Kou Shenhai
 */
@FeignClient(name = ServiceConstant.LAOKOU_ELASTICSEARCH, fallbackFactory = ElasticsearchApiFeignClientFallbackFactory.class)
@Service
public interface ElasticsearchApiFeignClient {

    /**
     * 异步批量同步索引
     * @param model
     */
    @PostMapping("/api/syncAsyncBatch")
    void syncAsyncBatch(@RequestBody final ElasticsearchModel model);

}