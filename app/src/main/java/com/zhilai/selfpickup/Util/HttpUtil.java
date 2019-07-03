package com.zhilai.selfpickup.Util;

import android.os.Bundle;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ZERO on 2017/10/29.
 */

public class HttpUtil {

    private static String ZM = "10.30.131.15:8088";
    private static String SKF = "";
    public static String ZTGServer = ZM;
    public static String SKFServer = SKF;

    private static String TAG = "HttpUtil";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 与管理员相关的操作
     */
    //管理员注销登录
    public static void sendLogoutOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url("http://" + ZTGServer + "/admin/logout?admin_id=" + mBundle.getString("admin_id"))
                .patch(requestBody)
                .build();
        Log.d(TAG, "sendLogoutOkHttpRequest: " + request.url() + mBundle.getString("admin_id") + "\n");
        client.newCall(request).enqueue(callback);
    }
    //管理员登录
    public static void sendLoginOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, mBundle.getString("json"));
        Request request = new Request.Builder()
                .url("http://" + ZTGServer + "/admin/login")
                .post(requestBody)
                .build();
        Log.d(TAG, "sendLoginOkHttp" +
                "Request: " + request.url() + "\n" + mBundle.getString("json") + "\n");
        client.newCall(request).enqueue(callback);
    }
    //查询负责柜子的管理员(call)
    public static void sendQueryAdminOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + ZTGServer + "/admin/query")
                .get()
                .build();
        Log.d(TAG, "sendLogoutOkHttpRequest: " + request.url() + mBundle.getString("admin_id") + "\n");
        client.newCall(request).enqueue(callback);
    }
    //管理员展示柜子物品
    public static void sendShowStatusOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + ZTGServer + "/admin/showStatus?admin_id=" + mBundle.getString("admin_id")
                        + "&cupboard_id=" + mBundle.getString("cupboard_id"))
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 获取柜子的总数、行列数
     */
    public static void sendGetCabinetNumsOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, "");
        Request request = new Request.Builder()
                .url("http://" + ZTGServer + "/cupboard/info")
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 送货码、取件码验证和完成通知，扫码复用接口
     * 二维码内容即取件码和送货码内容
     */
    /**
     * 送货码验证
     * */
    public static void sendSendVerifyOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, mBundle.getString("json"));
        Request request = new Request.Builder()
                .url("http://" + SKFServer + "/admin/login")
                .post(requestBody)
                .build();
        Log.d(TAG, "sendSendScanOkHttp" +
                "Request: " + request.url() + "\n" + mBundle.getString("json") + "\n");
        client.newCall(request).enqueue(callback);
    }
    /**
     * 送货完成通知
     * */
    public static void sendSendFinishOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, mBundle.getString("json"));
        Request request = new Request.Builder()
                .url("http://" + SKFServer + "/admin/login")
                .post(requestBody)
                .build();
        Log.d(TAG, "sendSendFinishOkHttp" +
                "Request: " + request.url() + "\n" + mBundle.getString("json") + "\n");
        client.newCall(request).enqueue(callback);
    }
    /***
     * 取货码验证
     */
    public static void sendTakeVerifyOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, mBundle.getString("json"));
        Request request = new Request.Builder()
                .url("http://" + SKFServer + "/admin/login")
                .post(requestBody)
                .build();
        Log.d(TAG, "sendTakeVerifyOkHttp" +
                "Request: " + request.url() + "\n" + mBundle.getString("json") + "\n");
        client.newCall(request).enqueue(callback);
    }

    /***
     * 送货完成通知
     */
    public static void sendTakeFinishOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, mBundle.getString("json"));
        Request request = new Request.Builder()
                .url("http://" + SKFServer + "/admin/login")
                .post(requestBody)
                .build();
        Log.d(TAG, "sendTakeFinishOkHttp" +
                "Request: " + request.url() + "\n" + mBundle.getString("json") + "\n");
        client.newCall(request).enqueue(callback);
    }


    /**
     * 分配柜子
     */
    public static void sendDistributeCabinetOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, mBundle.getString("json"));
        Request request = new Request.Builder()
                .url("http://" + SKFServer + ""+mBundle.get("ztgID"))
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }




    /**
     * 柜门结果上报
     */
    public static void sendOpenCabinetResultOkHttpRequest(Bundle mBundle, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, mBundle.getString("json"));
        Request request = new Request.Builder()
                .url("http://" + SKFServer + "")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }





}
