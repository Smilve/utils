package com.lvboaa.utils.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OkHttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    public final static int REQUEST_TIME_OUT = 20;

    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS) // 设置连接超时时间
            .readTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS) // 设置读取超时时间
            .writeTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS) // 设置写入超时时间
            .build();

    /**
     * 根据map获取get请求参数
     *
     * @param queries
     * @return
     */
    public static StringBuffer getQueryString(String url, Map<String, String> queries) {
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        return sb;
    }

    /**
     * 调用okhttp的newCall方法
     *
     * @param request
     * @return
     */
    private static String execNewCall(Request request) {
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                return response.body().string();
            }else {
                logger.error("请求【"+request.url()+"】出错！"+response.message());
            }
        } catch (Exception e) {
            logger.error("okhttp3 put error >> ex = {}", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return "";
    }

    /**
     * 调用okhttp的newCall方法
     *
     * @param request
     * @return
     */
    private static Response execResponseNewCall(Request request) {
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                return response;
            }
        } catch (Exception e) {
            logger.error("okhttp3 put error >> ex = {}", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return response;
    }

    /**
     * get
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
    public static String get(String url, Map<String, String> queries) {
        StringBuffer sb = getQueryString(url, queries);
        Request request = new Request.Builder()
                .url(sb.toString())
                .build();
        return execNewCall(request);
    }

    /**
     * get
     *
     * @param url 请求的url
     * @return
     */
    public static InputStream getStream(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = execResponseNewCall(request);


        return response.body().byteStream();
    }

    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public static String postFormParams(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        return execNewCall(request);
    }

    /**
     * post 表单上传文件
     *
     * @param url    请求的url
     * @param name   请求体的name
     * @param file   上传的文件
     * @return
     */
    public static String postFormFile(String url, String name, MultipartFile file) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(name, file.getOriginalFilename(), RequestBody.create(MediaType.parse("application/octet-stream"), file.getBytes()))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
    }


    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public static String postJsonParams(String url, String jsonParams) {
        return postJsonParams(url, jsonParams, "utf-8", null);
    }

    /**
     * put请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public static String postJsonParams(String url, String jsonParams, String charSet, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=" + charSet), jsonParams);
        Request.Builder builder = new Request.Builder();
        if (!CollectionUtils.isEmpty(headers)){
            for (String key : headers.keySet()){
                builder.addHeader(key, headers.get(key));
            }
        }
        Request request = builder
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
    }

    public static String putJsonParams(String url, Map<String, String> headers, String jsonParams) {
        return putJsonParams(url, headers, jsonParams, "utf-8");
    }

    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public static String putJsonParams(String url, Map<String, String> headers, String jsonParams, String charSet) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=" + charSet), jsonParams);

        Request.Builder builder = new Request.Builder();
        if (!CollectionUtils.isEmpty(headers)){
            for (String key : headers.keySet()){
                builder.addHeader(key, headers.get(key));
            }
        }
        Request request = builder
                .url(url)
                .put(requestBody)
                .build();
        return execNewCall(request);
    }

    public static String deleteJsonParams(String url, Map<String, String> headers, String jsonParams) {
        return deleteJsonParams(url, headers, jsonParams, "utf-8");
    }

    /**
     * delete请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public static String deleteJsonParams(String url, Map<String, String> headers, String jsonParams, String charSet) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=" + charSet), jsonParams);

        Request.Builder builder = new Request.Builder();
        if (!CollectionUtils.isEmpty(headers)){
            for (String key : headers.keySet()){
                builder.addHeader(key, headers.get(key));
            }
        }
        Request request = builder
                .url(url)
                .delete(requestBody)
                .build();
        return execNewCall(request);
    }


    /**
     * Post请求发送xml数据....
     * 参数一：请求Url
     * 参数二：请求的xmlString
     * 参数三：请求回调
     */
    public static String postXmlParams(String url, String xml) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
    }
}