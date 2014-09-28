import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.lang.Thread;
import java.security.MessageDigest;

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

    public int Login(String json_str) throws Exception{
        String URL = BaseUrl + "v1/admin/user/userlogin1";
        ApiResponseParser(URL, json_str);
        return retcode;
    }

    /* **********************************************************
     * Device management api
     * *********************************************************/

    public String GetAPIStatus(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/getapistatus";
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String GetVer(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/getver";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String GetCfg(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/getcfg";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String SetCfg(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/setcfg";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String Ping(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/ping";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String UpdateFirmware(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/updatefirmware";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String System(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/system";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String GetAppList(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/getapplist";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String StartApp(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/startapp";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String StopApp(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/stopapp";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String InstallApp(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/installapp";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String Reboot(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/reboot";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String Filec2d(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/filec2d";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String Filed2c(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/filed2c";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    public String RpcCall(String json_str) throws Exception{
        String URL = BaseUrl + deviceManagementUrl + "/rpccall";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return status;
    }

    /* **********************************************************
     * project managment api
     * *********************************************************/

    public String AddProject(String json_str) throws Exception{
        String URL = BaseUrl + projectManagementUrl + "/addproject";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String UpdateProject(String json_str) throws Exception{
        String URL = BaseUrl + projectManagementUrl + "/updateproject";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String GetProjectInfo(String json_str) throws Exception{
        String URL = BaseUrl + projectManagementUrl + "/getprojectinfo";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String DeleteProject(String json_str) throws Exception{
        String URL = BaseUrl + projectManagementUrl + "/deleteproject";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String ListProjects() throws Exception{
        String URL = BaseUrl + projectManagementUrl + "/listprojects";
        JsonObject jobj = Json.createObjectBuilder()
            .add("auth_token", auth_token)
            .build();
        String json_str = jobj.toString();
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String GetProjectStats(String json_str) throws Exception{
        String URL = BaseUrl + projectManagementUrl + "/getprojectstats";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String ListDevInProject(String json_str) throws Exception{
        String URL = BaseUrl + projectManagementUrl + "/listdevinproject";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String ListDevByGroup(String json_str) throws Exception{
        String URL = BaseUrl + projectManagementUrl + "/listdevbygroup";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String ListProjectUser(String json_str) throws Exception{
        String URL = BaseUrl + projectManagementUrl + "/listprojectuser";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    /* **********************************************************
     * gateway managment api
     * *********************************************************/

    public String AddDev(String json_str) throws Exception{
        String URL = BaseUrl + gatewayManagementUrl + "/adddev";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String UpdateDev(String json_str) throws Exception{
        String URL = BaseUrl + gatewayManagementUrl + "/updatedev";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String GetDevInfo(String json_str) throws Exception{
        String URL = BaseUrl + gatewayManagementUrl + "/getdevinfo";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }

    public String DeleteDev(String json_str) throws Exception{
        String URL = BaseUrl + gatewayManagementUrl + "/deletedev";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        ApiResponseParser(URL, json_str);
        return response;
    }
   // public static void main(String[] args) throws Exception{
   //     DeviceControl devicecontrol = new DeviceControl();
   //     devicecontrol.setUserName("admin");
   //     devicecontrol.setPassWord("intel123");
   //     String json_str;
   //     JsonObject jobj = Json.createObjectBuilder()
   //         .add("username", devicecontrol.getUserName())
   //         .build();
   //     json_str = jobj.toString();
   //     devicecontrol.Login(json_str);
   //     jobj = Json.createObjectBuilder()
   //         .add("auth_token", devicecontrol.getToken())
   //         .add("device", "/dev/ctl/demo01/gw01")
   //         .build();
   //     json_str = jobj.toString();
   //     devicecontrol.GetVer(json_str);
   //     devicecontrol.GetCfg(json_str);
   //     devicecontrol.Ping(json_str);
   //     devicecontrol.GetAppList(json_str);
   //     devicecontrol.Reboot(json_str);
   // }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setPassWord(String password) {
        this.password = password;
    }

    public String getUserName() {
        return username;
    }

    public String getToken() {
        return auth_token;
    }

    private void ApiResponseParser(String URL, String json_str) throws Exception{
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
                        login_response();
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
                            System.out.println(e.toString());
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
            } finally {
                apiResponse.close();
            }
        } finally {
            httpClient.close();
        }
    }

    private void login_response() throws Exception{
        String str1 = password + salt;
        //System.out.println(str1);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(str1.getBytes());
            String hash1 = getFormattedText(messageDigest.digest());
            //System.out.println(hash1);
            String str2 = hash1 + challenge;
            //System.out.println(str2);
            messageDigest.update(str2.getBytes());
            String hash2 = getFormattedText(messageDigest.digest());
            //System.out.println(hash2);
            JsonObject jobj = Json.createObjectBuilder()
                .add("username", username)
                .add("login_id", login_id)
                .add("response", hash2)
                .build();
            String URL = BaseUrl + "v1/admin/user/userlogin2";
            String json_str = jobj.toString();
            ApiResponseParser(URL, json_str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            buf.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return buf.toString();
    }


    private int retcode;
    private String api_qid;
    private int time_out;
    private String auth_token;
    private String response;
    private String status;
    private String username;
    private String password;
    private String login_id;
    private String challenge;
    private String algorithm;
    private String salt;
    private static final String deviceManagementUrl = "v1/dev/ctl";
    private static final String projectManagementUrl = "v1/admin/project";
    private static final String gatewayManagementUrl = "v1/admin/dev";
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
}
