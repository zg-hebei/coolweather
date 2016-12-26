package com.coolweather.app.util;

/**
 * Created by Administrator on 2016/12/22.
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    //connection=new HttpURLConnection(url);
                    //       connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //     connection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
//                    connection.setDoInput(true);
//                    System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
                    connection.setRequestMethod("GET");
                    //String ct = connection.getRequestProperty("Content-Type");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

//                    connection.connect();
                    if (200==connection.getResponseCode()) {

                        InputStream in = connection.getInputStream();

//                        byte[] data = readStream(in); // 把输入流转换成字符串组
//                        String result = new String(data); // 把字符串组转换成字符串
                        InputStreamReader isr = new InputStreamReader(in,"utf-8");
                        BufferedReader reader = new BufferedReader(isr);
                        StringBuilder response = new StringBuilder();
                        String line=null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        if (listener != null) {
                            // 回调onFinish()方法
                           listener.onFinish(response.toString());
//                             listener.onFinish(result);
                        }

                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private static byte[] readStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            bout.write(buffer, 0, len);
        }
        bout.close();
        inputStream.close();
        return bout.toByteArray();
    }
}
