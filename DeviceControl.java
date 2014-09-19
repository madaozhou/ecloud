import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.lang.Thread;

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

public class DeviceControl{
    public static final String BaseUrl =
        "http://115.29.204.44:8080/ecloud/";

    public String GetAPIStatus(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/getapistatus";
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String GetVer(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/getver";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String GetCfg(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/getcfg";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String SetCfg(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/setcfg";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String Ping(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/ping";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String UpdateFirmware(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/updatefirmware";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String System(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/system";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String GetAppList(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/getapplist";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String StartApp(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/startapp";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String StopApp(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/stopapp";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String InstallApp(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/installapp";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String Reboot(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/reboot";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String Filec2d(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/filec2d";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String Filed2c(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/filed2c";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public String RpcCall(String json_str) throws Exception{
        String URL = BaseUrl + "v1/dev/ctl/rpccall";
        ApiResponseParser(URL, json_str);
        return respones;
    }

    public static void main(String[] args) throws Exception{
        String json_str;
        json_str = "{\"device\":\"/dev/ctl/demo01/gw01\",\"auth_token\":\"test\"}";
        DeviceControl devicecontrol = new DeviceControl();
        devicecontrol.GetVer(json_str);
        devicecontrol.GetCfg(json_str);
        devicecontrol.Ping(json_str);
        devicecontrol.GetAppList(json_str);
        devicecontrol.Reboot(json_str);
    }

    private void ApiResponseParser(String URL, String json_str) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            System.out.println("Post URL:");
            System.out.println(URL);
            System.out.println("Post request entity:");
            System.out.println(json_str);
            HttpPost httpPost = new HttpPost(URL);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new ByteArrayInputStream(json_str.getBytes())));
            JsonReader jsonReader = Json.createReader(reader);
            JsonObject jobj = jsonReader.readObject();
            auth_token = "\"" + jobj.getString("auth_token") + "\"";
            reader.close();
            httpPost.setEntity(new ByteArrayEntity(
                        jobj.toString().getBytes("UTF8")));
            CloseableHttpResponse apiResponse = httpClient.execute(httpPost);
            try {
                System.out.println(apiResponse.getStatusLine());
                if (apiResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = apiResponse.getEntity();
                    reader = new BufferedReader(new InputStreamReader(
                                entity.getContent()));
                    jsonReader = Json.createReader(reader);
                    jobj = jsonReader.readObject();
                    reader.close();
                    System.out.println("Recieved JSON:");
                    if (URL.contains("getapistatus")) {
                        status = jobj.toString();
                        System.out.println(status);
                    }
                    else {
                        respones = jobj.toString();
                        System.out.println(respones);
                    }
                    EntityUtils.consume(entity);
                    if (jobj.containsKey("retcode"))
                        retcode = jobj.getInt("retcode");
                    if (jobj.containsKey("api_qid"))
                        api_qid = "\"" + jobj.getString("api_qid") + "\"";
                    if (jobj.containsKey("time_out"))
                        time_out = jobj.getInt("time_out");
                    if (retcode > 10000) {
                        System.out.printf("Error code:  %d\n", retcode);
                        System.out.println();
                    }
                    else if (retcode == 0) {
                        System.out.println("Success.");
                        System.out.println();
                    }
                    else if (retcode == 1) {
                        try {
                            Thread.sleep(time_out);
                        } catch(InterruptedException e) {
                            System.out.println(e.toString());
                        }
                        String tmp = "{" + "\"api_qid\"" + ":" + api_qid + ","
                            + "\"auth_token\"" + ":" + auth_token + "}";
                        GetAPIStatus(tmp);
                    }
                    else {
                        System.out.printf("Unknown code:    %d\n", retcode);
                    }
                }
            } finally {
                apiResponse.close();
            }
        } finally {
            httpClient.close();
        }
    }

    private int retcode;
    private String api_qid;
    private int time_out;
    private String auth_token;
    private String respones;
    private String status;
}

