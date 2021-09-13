package com.gov.wiki.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MyHttpUtils {
    public static String doPost(String urlStr, String parameter) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();  // 新建连接实例
            connection.setConnectTimeout(2500);                     // 设置连接超时时间，单位毫秒
            connection.setReadTimeout(2500);                        // 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);                           // 是否打开输出流 true|false
            connection.setDoInput(true);                            // 是否打开输入流true|false
            connection.setRequestMethod("POST");                    // 提交方法POST
            connection.setUseCaches(false);                         // 是否缓存true|false
            connection.connect();                                   // 打开连接端口
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(parameter);                              // 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();                                            // 刷新
            out.close();                                            // 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));// 往对端写完数据对端服务器返回数据 ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }
}
