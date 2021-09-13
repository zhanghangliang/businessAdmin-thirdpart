package com.gov.wiki.common.utils;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

/**
 * @ClassName: HttpUtils 
 * @Description: 远程请求数据
 * @author cys 
 * @date 2017年11月10日 下午5:41:59
 */
public class HttpUtils {
	
	/**
	 * Content-Type
	 */
	public static final String CONTENT_TPYE = "application/x-www-form-urlencoded";
	
	/**
	 * @Title: sentRequestData 
	 * @Description: 发送数据请求
	 * @param 设定文件 
	 * @return AjaxData    返回类型 
	 * @throws
	 */
	public static Object sentRequestData(String url, String method, Map<String, Object> params, String body, Class responseClass, String contentType){
		Object data = null;
		try {
			contentType = StringUtils.isEmpty(contentType)?CONTENT_TPYE:contentType;
			if(StringUtils.isEmpty(url) || StringUtils.isEmpty(method)){
				return data;
			}
			HttpResponse<Object> postResponse = null;
			if("POST".equals(method.toUpperCase())){
				HttpRequestWithBody hrwb = Unirest.post(url)
						.header("Content-Type", contentType)
						.header("accept", contentType);
				if(params != null){
					hrwb.fields(params);
				}
				if(body != null){
					hrwb.body(body);
				}
				postResponse = hrwb.asObject(responseClass);
			}else if("GET".equals(method.toUpperCase())){
				GetRequest gr = Unirest.get(url)
						.header("Content-Type", contentType);
				if(params != null){
					gr.queryString(params);
				}
				postResponse = gr.asObject(responseClass);
			}
			
			data = postResponse != null?postResponse.getBody():null;
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static void main(String[] args) {
		int Code = (int) (Math.random() * 9000 + 1000);
		try {
			Map<String,Object> param = new HashMap<>();
			param.put("uid", "172411");
			param.put("pwd", MD5.lower32MD5Encoder("l67pk0"));
			param.put("mobile", "15775965127");
			param.put("srcphone", "72411");
			param.put("msg", URLEncoder.encode("【政务大百科】欢迎使用政务大百科系统，您的手机验证码是"+Code+"。本条信息无需回复。", "UTF-8"));
			String url = "http://bst.8315.cn:9892/cmppweb/sendsms";
			String str = (String) HttpUtils.sentRequestData(url, "POST",  param, null, String.class, "");
			System.out.println("str:" + str);
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}
}
