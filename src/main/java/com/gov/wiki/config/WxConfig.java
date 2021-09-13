package com.gov.wiki.config;

import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gov.wiki.wx")
public class WxConfig {
    private String appid;
    private String appsecret;
    private String token;

    private String openidurl;
    private String userinfourl;
    private String redirecturi;
    private String templetSubmit;
    private String templetResult;
    private String serverurl;


    @Bean
    public WxMpService getWxMpService(){
        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId(appid);
        configStorage.setSecret(appsecret);
        configStorage.setToken(token);
        service.setWxMpConfigStorage(configStorage);
        return service;
    }
}
