package com.iap.phenologyweather.utils;

/**
 * Created by infolife on 2017/2/13.
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class HttpUtils {

    public HttpUtils() {
    }

    public static String RequestToString(String url, String data) {
        HttpURLConnection connection = null;
        InputStreamReader ins = null;
        StringBuilder sb = new StringBuilder();

        try {
            URL e = new URL(url);
            connection = (HttpURLConnection)e.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            ins = new InputStreamReader(connection.getInputStream(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(ins);

            String inputLine;
            while((inputLine = bufferedReader.readLine()) != null) {
                sb.append(inputLine);
            }
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if(null != ins) {
                try {
                    ins.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

            if(null != connection) {
                connection.disconnect();
            }

        }

        return sb.toString();
    }

    public static byte[] RequestToByteArray(String url) {
        HttpURLConnection connection = null;
        InputStream ins = null;
        ByteArrayOutputStream baos = null;

        try {
            URL httpUrl = new URL(url);
            connection = (HttpURLConnection)httpUrl.openConnection();
            ins = connection.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            boolean readBytes = true;

            int readBytes1;
            while((readBytes1 = ins.read(buf)) != -1) {
                baos.write(buf, 0, readBytes1);
            }
        } catch (Exception var15) {
            ;
        } finally {
            try {
                if(baos != null) {
                    baos.close();
                }

                if(ins != null) {
                    ins.close();
                }
            } catch (Exception var14) {
                ;
            }

        }

        return baos.toByteArray();
    }

    public static JSONObject requestToJson(String url, JSONObject json) {
        HttpURLConnection connection = null;
        InputStreamReader ins = null;
        String data = "";

        try {
            URL e = new URL(url);
            connection = (HttpURLConnection)e.openConnection();
            ins = new InputStreamReader(connection.getInputStream(), "utf-8");

            String inputLine;
            for(BufferedReader bufferedReader = new BufferedReader(ins); (inputLine = bufferedReader.readLine()) != null; data = data + inputLine) {
                ;
            }

            json = new JSONObject(data);
        } catch (Exception var16) {
            ;
        } finally {
            if(null != ins) {
                try {
                    ins.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

            if(null != connection) {
                connection.disconnect();
            }

        }

        return json;
    }

    public static JSONObject requestInstalledPluginInfo(String url, JSONObject jsonObject) {
        HttpURLConnection connection = null;
        InputStreamReader ins = null;
        String data = "";
        JSONObject json = null;
        BufferedOutputStream out = null;

        try {
            URL e = new URL(url);
            connection = (HttpURLConnection)e.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setDoInput(true);
            out = new BufferedOutputStream(connection.getOutputStream());
            out.write(jsonObject.toString().getBytes());
            out.flush();
            out.close();
            ins = new InputStreamReader(connection.getInputStream(), "utf-8");

            String inputLine;
            for(BufferedReader bufferedReader = new BufferedReader(ins); (inputLine = bufferedReader.readLine()) != null; data = data + inputLine) {
                ;
            }

            json = new JSONObject(data);
        } catch (Exception var22) {
            var22.printStackTrace();
        } finally {
            if(null != ins) {
                try {
                    ins.close();
                } catch (IOException var21) {
                    var21.printStackTrace();
                }
            }

            if(null != connection) {
                connection.disconnect();
            }

            try {
                if(out != null) {
                    out.close();
                }
            } catch (IOException var20) {
                var20.printStackTrace();
            }

        }

        return json;
    }
}