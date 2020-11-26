package com.tingyu.venus.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public final class OkHttpClientUtils {


    /**
     * 连接超时时间
     */
    private static final long CONN_TIMEOUT = 50000L;
    /**
     * 客户端读超时时间
     */
    private static final long READ_TIMEOUT = 1000L;
    /**
     * 客户端写超时时间
     */
    private static final long WRITE_TIMEOUT = 1000L;

    /**
     * 发送WebSocket请求 ，带心中检测，如果中断则重连
     *
     * @param url
     * @param listener
     */
    public static WebSocket ws(String url, WebSocketListener listener) {
        OkHttpClient client = getOkHttpClient();
        Request request = new Request.Builder().url(url).build();
        WebSocket webSocket = client.newWebSocket(request, listener);
        //断开客户断连接
        client.dispatcher().executorService().shutdown();
        return webSocket;

    }

    @NotNull
    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static void get(String url, Callback responseCallBack) {

        OkHttpClient client = getOkHttpClient();
        Request request = new Request.Builder().url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(responseCallBack);
        client.dispatcher().executorService().shutdown();

    }

    public static void post(String url, Map<String, String> params, Callback responseCallback) {
        //创建client
        OkHttpClient client = getOkHttpClient();
        //构建请求体
        FormBody formBody = resolveParameters(params);
        //创建请求对象
        final Request request = new Request.Builder().url(url).post(formBody).build();
        //将请求封装成Call
        Call call = client.newCall(request);
        //异步调用请求并重写回调函数
        call.enqueue(responseCallback);
        //释放资源
        client.dispatcher().executorService().shutdown();
    }

    /**
     * 解析参数
     *
     * @param params
     * @return
     */
    private static FormBody resolveParameters(Map<String, String> params) {

        if (null == params || params.isEmpty()) {
            return null;
        }

        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        FormBody.Builder builder = new FormBody.Builder();
        while (entries.hasNext()) {
            Map.Entry<String, String> next = entries.next();
            builder.add(next.getKey(), next.getValue());
        }
        FormBody formBody = builder.build();
        return formBody;
    }

}
