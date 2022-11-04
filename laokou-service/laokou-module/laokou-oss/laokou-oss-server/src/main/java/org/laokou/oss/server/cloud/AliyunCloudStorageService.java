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
package org.laokou.oss.server.cloud;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.laokou.common.exception.CustomException;
import org.laokou.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.laokou.oss.client.vo.CloudStorageVO;

import java.io.InputStream;

/**
 * 阿里云存储
 * @author Kou Shenhai
 */
@Slf4j
public class AliyunCloudStorageService extends AbstractCloudStorageService {

    public AliyunCloudStorageService (CloudStorageVO vo){
        super.cloudStorageVO = vo;
    }

    @Override
    public String upload(InputStream inputStream, String fileName, Long fileSize) {
        final String filePath = cloudStorageVO.getAliyunPrefix() + SEPARATOR + IdUtil.simpleUUID() + FileUtil.getFileSuffix(fileName);
        OSS ossClient = new OSSClientBuilder().build(cloudStorageVO.getAliyunEndPoint(), cloudStorageVO.getAliyunAccessKeyId(), cloudStorageVO.getAliyunAccessKeySecret());
        try {
            ossClient.putObject(cloudStorageVO.getAliyunBucketName(), filePath , inputStream);
            ossClient.shutdown();
        } catch (Exception e){
            log.error("错误信息:{}", e.getMessage());
            throw new CustomException(e.getMessage());
        }
        return cloudStorageVO.getAliyunDomain() + SEPARATOR + filePath;
    }
}
