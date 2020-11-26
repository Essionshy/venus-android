package com.tingyu.venus.utils.http;

import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import io.netty.util.internal.StringUtil;

public class HttpClient {

    public static final int TIME_OUT_MILLIS = 50*1000;
    public static final int CON_TIME_OUT_MILLIS =3000*1000;

    private boolean isHttps;//判断请求协议是否是 https://

    private String PUT = "PUT";

    private String POST = "POST";

    /**
     * 发送GET方式的请求
     *
     * @param url      //请求服务器地址
     * @param headers  //请求头信息
     * @param params   //请求参数
     * @param encoding //编码方式
     * @return
     */
    public static HttpResponse httGet(String url, List<String> headers, Map<String, String> params, String encoding) {
        return request(url, headers, params, encoding,"GET");
    }

    /**
     *  实际发送请求
     * @param url
     * @param headers
     * @param params
     * @param encoding
     * @return
     */
    public static HttpResponse request(String url, List<String> headers, Map<String, String> params, String encoding,String method) {

      HttpURLConnection connection=null;//http请求连接器

        HttpResponse response=null;

        try {
            //请求参数进行编码 //get请求方式的编码完成，如果是post或者put提交则不需要考虑编码问题
            String encodedContent = encodingParams(params, encoding);
            //构建请求路径 url+params
            url=url+(StringUtil.isNullOrEmpty(encodedContent)?"":encodedContent);
            //实例化HttpURLConnection
            synchronized (HttpClient.class){
                connection=(HttpURLConnection)(new URL(url)).openConnection();

             //   OkHttpClient okHttpClient = new OkHttpClient();


                //设置请求头信息
            setHeaders(connection,headers,encoding);
            //设置请求连接的其他参数
            //连接超时
            connection.setConnectTimeout(CON_TIME_OUT_MILLIS);
            //读取超时
            connection.setReadTimeout(TIME_OUT_MILLIS);
            //设置请求方式
            connection.setRequestMethod(method);

                //禁用网络缓存
                connection.setUseCaches(false);
            //默认为false,表示该连接是否支持 OutputStream 流的方式往外写数据，因为该 request 方法包含处理POST或PUT请求，因此需要开启此功能
            //GET请求可以不设置
           // connection.setDoOutput(true);
            //判断请求类型
            if("POST".equals(method)|| "PUT".equals(method)){
                byte[] b = encodedContent.getBytes();
                connection.setRequestProperty("Content-Length", String.valueOf(b.length));
                connection.getOutputStream().write(b, 0, b.length);
                connection.getOutputStream().flush();
                connection.getOutputStream().close();
            }
            //开始连接
            connection.connect();

            }
           /// Log.i("Request from server:",url);
            response=getResult(connection);

        } catch (Exception e) {
            //连接中出现异常
            Log.e("HttpUrlConnection Error",e.getMessage());
        }finally {
            if(connection!=null){
                //断开连接
                connection.disconnect();
            }
        }
        return response;
    }

    /**
     * 根据请求连接获取请求响应
     * @param conn
     * @return
     */
    private static HttpResponse getResult(HttpURLConnection conn) throws IOException {



        //获取响应
        int respCode = conn.getResponseCode();
        Object inputStream;
        if (200 != respCode && 304 != respCode && 307 != respCode) {
            inputStream = conn.getErrorStream();
        } else {
            inputStream = conn.getInputStream();
        }

        Map<String, String> respHeaders = new HashMap(conn.getHeaderFields().size());
        Iterator var4 = conn.getHeaderFields().entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry)var4.next();
            respHeaders.put(entry.getKey(), (entry.getValue()).get(0));
        }

        String encodingGzip = "gzip";
        if (encodingGzip.equals(respHeaders.get("Content-Encoding"))) {
            inputStream = new GZIPInputStream((InputStream)inputStream);
        }


        return new HttpClient.HttpResponse(respCode, IOUtils.toString((InputStream)inputStream, getCharset(conn)), respHeaders);
    }

    /**
     * 从请求连接中获取字符编码
     * @param conn
     * @return
     */
    private static String getCharset(HttpURLConnection conn) {

        String contentType = conn.getContentType();
        if (StringUtil.isNullOrEmpty(contentType)) {
            //如果没有设置字符编码，则设定默认值为UTF-8
            return "UTF-8";
        } else {
            String[] values = contentType.split(";");
            if (values.length == 0) {
                return "UTF-8";
            } else {
                String charset = "UTF-8";
                String[] var4 = values;
                int var5 = values.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String value = var4[var6];
                    value = value.trim();
                    if (value.toLowerCase().startsWith("charset=")) {
                        charset = value.substring("charset=".length());
                    }
                }

                return charset;
            }
        }
    }


    /**
     * 设置请求头信息
     * @param connection
     * @param headers
     * @param encoding
     */
    private static void setHeaders(HttpURLConnection connection, List<String> headers, String encoding) {
        //判断是否非空 ，排除空指针异常 NPE  注意
        if(null != headers){

            Iterator<String> iterator = headers.iterator();
            while (iterator.hasNext()){
                String header = iterator.next();
                connection.addRequestProperty(header,header);
            }
        }

        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
        connection.addRequestProperty("Accept-Charset", encoding);

    }

    /**
     * 请求参数进行编码
     * @param params
     * @param encoding
     */
    private static String encodingParams(Map<String, String> params, String encoding) throws UnsupportedEncodingException {
        //字符串构建器
        StringBuilder sb = new StringBuilder();
        //判断 params是否为null或者有值
        if(null!=params && !params.isEmpty()){
            //将编码方式保存在请求参数中
            params.put("encoding",encoding);

            //通过迭代器的方式遍历params
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> entry = iterator.next();
                //如果参数有值才进行编码处理
                if(!StringUtil.isNullOrEmpty(entry.getValue()))
                    sb.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(),encoding))//此处会抛出编码异常
                        .append("&");
            }


            //去掉最后一个 & 字符
            if(sb.length()>0){
                sb = sb.deleteCharAt(sb.length() - 1); 
            }

            return  sb.toString();
        }else{
            return "";
        }


    }


  public static   class HttpResponse {

        //响应码
        public final int code;
        //响应体
        public final String content;
        //响应头
        private final Map<String, String> respHeaders;

        public HttpResponse(int code, String content, Map<String, String> respHeaders) {
            this.code = code;
            this.content = content;
            this.respHeaders = respHeaders;
        }

        public String getHeader(String name) {
            return (String)this.respHeaders.get(name);
        }
    }


}
