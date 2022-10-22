package org.laokou.kafka.consumer.component.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.laokou.common.utils.JacksonUtil;
import org.laokou.kafka.client.constant.KafkaConstant;
import org.laokou.kafka.client.dto.ResourceAuditLogDTO;
import org.laokou.kafka.consumer.entity.SysResourceAuditLogDO;
import org.laokou.kafka.consumer.entity.SysResourceDO;
import org.laokou.kafka.consumer.service.SysResourceAuditLogService;
import org.laokou.kafka.consumer.service.SysResourceService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Kou Shenhai
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceConsumer {

    private final SysResourceAuditLogService sysResourceAuditLogService;

    private final SysResourceService sysResourceService;

    /**
     * 审核消息
     * @param message
     * @param acknowledgment
     */
    @KafkaListener(topics = {KafkaConstant.LAOKOU_RESOURCE_AUDIT_TOPIC})
    public void loginLog(String message, Acknowledgment acknowledgment) {
        try {
            if (StringUtils.isNotBlank(message)) {
                final ResourceAuditLogDTO auditLogDTO = JacksonUtil.toBean(message, ResourceAuditLogDTO.class);
                SysResourceDO sysResourceDO = sysResourceService.getById(auditLogDTO.getResourceId());
                sysResourceDO.setStatus(auditLogDTO.getStatus());
                sysResourceService.updateById(sysResourceDO);
                //插入审批日志
                SysResourceAuditLogDO logDO = new SysResourceAuditLogDO();
                logDO.setAuditDate(new Date());
                logDO.setAuditName(auditLogDTO.getAuditName());
                logDO.setCreator(auditLogDTO.getCreator());
                logDO.setComment(auditLogDTO.getComment());
                logDO.setResourceId(auditLogDTO.getResourceId());
                logDO.setAuditStatus(auditLogDTO.getAuditStatus());
                sysResourceAuditLogService.save(logDO);
            }
        } catch (Exception e) {
            log.error("消息消费失败", e);
        } finally {
            //手动签发，并回馈信息给MQ
            acknowledgment.acknowledge();
        }
    }

}