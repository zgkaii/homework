package com.zgkaii.nio.mission;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/03/26 21:18
 * @Description: HttpClient
 **/
public class HttpClientTest {
    public static void main(String[] args) {
        // 创建一个httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 响应
        CloseableHttpResponse httpResponse = null;
        // 创建Get请求
        HttpGet httpGet = new HttpGet("http://localhost:8800");
        try {
            // 执行get请求得到返回对象
            httpResponse = httpClient.execute(httpGet);
            // 获取响应的状态码
            int status = httpResponse.getStatusLine().getStatusCode();
            // 请求成功
            if (status == HttpStatus.SC_OK) {
                // 通过返回对象获取返回数据
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    String content = EntityUtils.toString(httpEntity, "UTF-8");
                    System.out.println("Connection success! Content body:" + content
                            + " Content-length:" + content.getBytes().length);
                }
            }
        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        } finally {
            // 关闭使用的资源
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}