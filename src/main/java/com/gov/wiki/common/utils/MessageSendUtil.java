/**
 * @Title: MessageSendUtil.java
 * @Package com.xiangtong.common.utils
 * @Description: 短消息发送工具类
 * @author cys
 * @date 2019年12月17日
 * @version V1.0
 */
package com.gov.wiki.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageSendUtil {
	 
    private MessageSendUtil() {
    }
    
    /**
     * @Title: sendPostMessage
     * @Description: 发送短信
     * @param strUrl
     * @param params
     * @param encode
     * @return String 返回类型
     * @throws
     */
    public static String sendPostMessage(String strUrl, Map<String, String> params, String encode) {
        URL url = null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
           log.info("{}", e);
        }
 
        StringBuffer stringBuffer = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    stringBuffer
                    	.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
 
                } catch (UnsupportedEncodingException e) {
                	log.info("{}", e);
                }
            }
            // 删掉最后一个 & 字符
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            InputStream inputStream = null;
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setDoInput(true);// 从服务器获取数据
                httpURLConnection.setDoOutput(true);// 向服务器写入数据
 
                // 获得上传信息的字节大小及长度
                byte[] mydata = stringBuffer.toString().getBytes();
                // 设置请求体的类型
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Content-Lenth", String.valueOf(mydata.length));
 
                // 获得输出流，向服务器输出数据
                OutputStream outputStream = (OutputStream) httpURLConnection.getOutputStream();
                outputStream.write(mydata);
 
                // 获得服务器响应的结果和状态码
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 200) {
                    // 获得输入流，从服务器端获得数据
                    inputStream = (InputStream) httpURLConnection.getInputStream();
                    return (changeInputStream(inputStream, encode));
                }
            } catch (IOException e) {
            	log.info("{}", e);
            } finally {
            	if(inputStream != null) {
            		try {
						inputStream.close();
					} catch (IOException e) {
						log.info("{}", e);
					}
            	}
            }
        }
        return "";
    }
 
    /**
     * @Title: changeInputStream
     * @Description: 把从输入流InputStream按指定编码格式encode变成字符串String
     * @param inputStream
     * @param encode
     * @return String 返回类型
     * @throws
     */
    public static String changeInputStream(InputStream inputStream, String encode) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    byteArrayOutputStream.write(data, 0, len);
                }
                result = new String(byteArrayOutputStream.toByteArray(), encode);
            } catch (IOException e) {
            	log.info("{}", e);
            }finally {
            	if(byteArrayOutputStream != null) {
            		try {
						byteArrayOutputStream.close();
					} catch (IOException e) {
						log.info("{}", e);
					}
            	}
            }
        }
        return result;
    }
    
    /**
     * @Title: randomNum
     * @Description: 生成n位随机数
     * @param num
     * @return String 返回类型
     * @throws
     */
    public static String randomNum(int num) {
    	num = num <= 0?1:num;
    	String sources = "0123456789";
    	Random rand = new Random();
    	StringBuffer result = new StringBuffer();
    	for (int j = 0; j < num; j++) 
    	{
    		result.append(sources.charAt(rand.nextInt(9)) + "");
    	}
    	return result.toString();
    }
    
    public static void main(String[] args) {
    	Map<String, String> params = new HashMap<String, String>();
		String ts = Utility.getNowDateStr();
		String pwd = Utility.getMD5("13980088627" + "Wxm84456393" + ts);// Md5签名(账号+密码+时间戳)
		//请求参数 
		params.put("account", "13980088627");
		params.put("pswd", pwd);//13795545237---15928953143
		params.put("mobile", "15928953143");
		params.put("msg", "单据：SDTQLS202004020004，申请完成，请及时处理。");
		params.put("ts", ts);
		params.put("needstatus", "true");
		params.put("sms_sign", "乡同");
		String rep = MessageSendUtil.sendPostMessage("http://139.196.108.241:8080/Api/HttpSendSMYzm.ashx", params, "utf-8");
		System.out.println(rep);
    }
}