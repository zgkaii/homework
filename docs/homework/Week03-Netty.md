# 作业1

**题目描述**：整合上次的 Httpclient/Okhttp。

分析：之前传入handlerTest中value值是写死的`handlerTest(fullRequest, ctx, "Hello,Everyone!") --> String value= body`。这里修改成，通过HttpClient客户端访问HTTPServer服务端获取的body值。

1. 创建HTTPServer服务端：

```java
public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8800);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(()->{
                    service(socket);
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body ="hello,nio2";
            printWriter.println("Content-length:"+body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

2. 创建HttpClient客户端：

```java
public class HttpClientHelper {
    public static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static String getAsString(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = null;
        String content = "";
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    content = EntityUtils.toString(httpEntity, "UTF-8");
                    System.out.println("Content body:" + content
                            + " Content-length:" + content.getBytes().length);
                }
            }
            return content;
        } finally {
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
```

3. 修改`handlerTest(fullRequest, ctx, "Hello,Everyone!")`成`handlerBody(fullRequest, ctx);`

4. 创建`handlerBody`方法调用：

```java
    private void handlerBody(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws IOException {
        //long startTime = System.currentTimeMillis();
        FullHttpResponse response = null;
        try {
            // 调用HttpClientHelper 获取value值
            String value = HttpClientHelper.getAsString("http://localhost:8800");

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, 
                                                   Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

        } catch (Exception e) {
            System.out.println("处理测试接口出错" + e.getMessage());
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpHeaderUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }
```

5. 测试：启动HttpServer，再启动NettyHttpServer，访问`http://127.0.0.1:8804`获得：

```html
hello,nio2
```

说明成功了。

# 作业3

**题目描述**：实现过滤器。

```java
public class MyHttpRequestFilter implements HttpRequestFilter {
    @Override
    public void filter(final FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        String uri = fullRequest.uri();
        System.out.println("接收到的请求url: " + uri);
        if (!uri.contains("/hello")) {
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
            ctx.write(response);
            ctx.flush();
            ctx.close();
        }
        HttpHeaders headers = fullRequest.headers();
        if (headers == null) {
            headers = new DefaultHttpHeaders();
        }
        headers.add("proxy-tag", this.getClass().getSimpleName());
    }
}
```