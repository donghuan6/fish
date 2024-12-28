package com.nine.dingtalk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "dingtalk")
public class DingTalkConfig {

    // 企业id
    private String corpId;
    // 默认的userId
    private String defaultUserId;
    // 应用名
    private String name;
    // 应用id
    private String agentId;
    // 应用key
    private String appKey;
    // 应用密钥
    private String appSecret;
    // 事件类型
    private String bpmsInstanceChange;
    // 项目信息审批流程code,接收钉钉项目审批数据
    private String projectInfoProcessCode;
    // 公告审批流程code，推送公告至钉钉进行审批，接收审批结果
    private String announcementProcessCode;
}
