package com.gov.wiki.aliyunsms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "gov.wiki.aliyunsms")
public class AliyunSmsSendConfig {
    private String accesskeyid;
    private String accesssecret;
    private String singlename;
    private String templatecode;
    
}
