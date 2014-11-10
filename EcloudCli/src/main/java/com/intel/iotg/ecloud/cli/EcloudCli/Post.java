package com.intel.iotg.ecloud.cli;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.lang.Thread;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;

import javax.json.*;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.ByteArrayEntity;

public class Post {
    private int retcode = -1;
    private String api_qid;
    private int time_out;

    private String auth_token = "test";
    private String username;
    private String password;
    private String login_id;
    private String challenge;
    private String algorithm;
    private String salt;

    private String response;
    private String status;

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private String BaseUrl;
    private String DeviceManagementUrl;

    public void SetBaseUrl(String BaseUrl) {
        this.BaseUrl = BaseUrl;
    }

    public void SetDeviceManagementUrl (String DeviceManagementUrl) {
        this.DeviceManagementUrl = DeviceManagementUrl;
    }

    public void SetUserName(String username) {
        this.username = username;
    }

    public void SetPassWord(String password) {
        this.password = password;
    }

    public String GetToken() {
        return auth_token;
    }

    public String GetStatus() {
        return status;
    }

    public String GetResponse() {
        return response;
    }

    public int GetRetCode() {
        return retcode;
    }

    private String GetAPIStatus(String json_str) {
        String URL = BaseUrl + DeviceManagementUrl + "/getapistatus";
        ApiResponseParser(URL, json_str);
        return status;
    }

    private void LoginResponse() {
        String str1 = password + salt;
        //System.out.println(str1);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(str1.getBytes());
            String hash1 = GetFormattedText(messageDigest.digest());
            //System.out.println(hash1);
            String str2 = hash1 + challenge;
            //System.out.println(str2);
            messageDigest.update(str2.getBytes());
            String hash2 = GetFormattedText(messageDigest.digest());
            //System.out.println(hash2);
            JsonObject jobj = Json.createObjectBuilder()
                .add("username", username)
                .add("login_id", login_id)
                .add("response", hash2)
                .build();
            String URL = BaseUrl + "v1/admin/user/userlogin2";
            String json_str = jobj.toString();
            ApiResponseParser(URL, json_str);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void ApiResponseParser(String URL, String json_str) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            //System.out.println("Post URL:");
            //System.out.println(URL);
            //System.out.println("Post request entity:");
            //System.out.println(json_str);
            HttpPost httpPost = new HttpPost(URL);
            ByteArrayInputStream in = new ByteArrayInputStream(json_str.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            JsonObject jobj = jsonReader.readObject();
            httpPost.setEntity(new ByteArrayEntity(
                        jobj.toString().getBytes("UTF8")));
            CloseableHttpResponse apiResponse = httpClient.execute(httpPost);
            try {
                //System.out.println(apiResponse.getStatusLine());
                if (apiResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = apiResponse.getEntity();
                    InputStream entityIn = entity.getContent();
                    jsonReader = Json.createReader(entityIn);
                    jobj = jsonReader.readObject();
                    //System.out.println("Recieved JSON:");
                    if (URL.contains("getapistatus")) {
                        status = jobj.toString();
                        //System.out.println(status);
                    }
                    else {
                        response = jobj.toString();
                        //System.out.println(response);
                    }
                    EntityUtils.consume(entity);
                    //get all values in json object
                    retcode = jobj.getInt("retcode", -1);
                    api_qid = jobj.getString("api_qid", "-1");
                    time_out = jobj.getInt("time_out", -1);
                    auth_token = jobj.getString("auth_token", auth_token);
                    login_id = jobj.getString("login_id", "-1");
                    challenge = jobj.getString("challenge", "-1");
                    algorithm = jobj.getString("algorithm", "-1");
                    salt = jobj.getString("salt", "-1");
                    if (retcode > 10000) {
                        //System.out.printf("Error code:  %d\n", retcode);
                        //System.out.println();
                    }
                    else if (retcode == 0 && jobj.containsKey("algorithm")) {
                        //System.out.println("Success.");
                        LoginResponse();
                        //System.out.println();
                    }
                    else if (retcode == 0 && jobj.containsKey("response")) {
                        //System.out.println("Success.");
                        //System.out.println();
                    }
                    else if (retcode == 0) {
                        //System.out.println("Success.");
                        //System.out.println();
                    }
                    else if (retcode == 1) {
                        try {
                            Thread.sleep(time_out);
                        } catch(InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                        JsonObject tmpJobj = Json.createObjectBuilder()
                            .add("api_qid", api_qid)
                            .add("auth_token", auth_token)
                            .build();
                        String tmp = tmpJobj.toString();
                        GetAPIStatus(tmp);
                    }
                    else {
                        System.out.printf("Unknown code:    %d\n", retcode);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            } catch (JsonException e) {
                System.out.println(e.getMessage());
            } finally {
                apiResponse.close();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Url is invalid, reason : " + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (JsonException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String GetFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            buf.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return buf.toString();
    }
}
