package com.temp;

import java.util.ArrayList;
import java.util.List;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage.Data;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import me.chanjar.weixin.common.error.WxErrorException;

public class TestSendTemplet {

	public static void main(String[] args) throws WxErrorException, InterruptedException {
		WxMaService service = new WxMaServiceImpl();
		WxMaDefaultConfigImpl c = new WxMaDefaultConfigImpl();
		c.setAppid("wxe31572f9ec8584ef");
		c.setSecret("884489f45ed106582f68af76448de059");
		c.setToken("wmw");
		service.setWxMaConfig(c);
		for (int i = 0; i < 1000; i++) {
			Thread.sleep(1000);
			try {
				snedMessage(service);							
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void snedMessage(WxMaService service) throws WxErrorException {
		WxMaMsgService msgService = service.getMsgService();
		WxMaSubscribeMessage m = new WxMaSubscribeMessage();
		m.setTemplateId("ZmHzbt4rWcQXNVJ50k9iwpgHaEV6QXp5je7cUr-V4TA");
		m.setToUser("oQM305G4hb9DdX2EXgSy_BLusrig");
		m.addData(new Data("date1", "2019年10月11日"))
		.addData(new Data("thing2", "111111"))
		.addData(new Data("thing3", "111111"));
		msgService.sendSubscribeMsg(m);
	}
}
