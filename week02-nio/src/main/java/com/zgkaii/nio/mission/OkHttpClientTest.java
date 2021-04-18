package com.zgkaii.nio.mission;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/03/26 21:18
 * @Description: OkHttp
 **/
public class OkHttpClientTest {
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = "http://localhost:8800";

        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
