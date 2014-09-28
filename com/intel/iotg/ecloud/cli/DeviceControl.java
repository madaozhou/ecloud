package com.intel.iotg.ecloud.cli;

import com.intel.iotg.ecloud.cli.Post;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;

import javax.json.*;


public class DeviceControl{
    public DeviceControl() {
        APost = new Post();
        APost.SetBaseUrl(BaseUrl);
        APost.SetDeviceManagementUrl(DeviceManagementUrl);
    }

    public int Login(String json_str) throws Exception{
        APost.SetUserName(username);
        APost.SetPassWord(password);
        String URL = BaseUrl + "v1/admin/user/userlogin1";
        APost.ApiResponseParser(URL, json_str);
        auth_token = APost.GetToken();
        return APost.GetRetCode();
    }

    /**
     * Device management api
     */

    public String GetAPIStatus(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/getapistatus";
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String GetVer(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/getver";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String GetCfg(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/getcfg";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String SetCfg(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/setcfg";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String Ping(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/ping";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String UpdateFirmware(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/updatefirmware";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String System(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/system";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String GetAppList(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/getapplist";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String StartApp(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/startapp";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String StopApp(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/stopapp";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String InstallApp(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/installapp";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String Reboot(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/reboot";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String Filec2d(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/filec2d";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String Filed2c(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/filed2c";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    public String RpcCall(String json_str) throws Exception{
        String URL = BaseUrl + DeviceManagementUrl + "/rpccall";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetStatus();
    }

    /**
     * project management api
     */

    public String AddProject(String json_str) throws Exception{
        String URL = BaseUrl + ProjectManagementUrl + "/addproject";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String UpdateProject(String json_str) throws Exception{
        String URL = BaseUrl + ProjectManagementUrl + "/updateproject";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String GetProjectInfo(String json_str) throws Exception{
        String URL = BaseUrl + ProjectManagementUrl + "/getprojectinfo";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String DeleteProject(String json_str) throws Exception{
        String URL = BaseUrl + ProjectManagementUrl + "/deleteproject";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String ListProjects() throws Exception{
        String URL = BaseUrl + ProjectManagementUrl + "/listprojects";
        JsonObject jobj = Json.createObjectBuilder()
            .add("auth_token", auth_token)
            .build();
        String json_str = jobj.toString();
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String GetProjectStats(String json_str) throws Exception{
        String URL = BaseUrl + ProjectManagementUrl + "/getprojectstats";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String ListDevInProject(String json_str) throws Exception{
        String URL = BaseUrl + ProjectManagementUrl + "/listdevinproject";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String ListDevByGroup(String json_str) throws Exception{
        String URL = BaseUrl + ProjectManagementUrl + "/listdevbygroup";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String ListProjectUser(String json_str) throws Exception{
        String URL = BaseUrl + ProjectManagementUrl + "/listprojectuser";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    /**
     * gateway management api
     */

    public String AddDev(String json_str) throws Exception{
        String URL = BaseUrl + GatewayManagementUrl + "/adddev";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String UpdateDev(String json_str) throws Exception{
        String URL = BaseUrl + GatewayManagementUrl + "/updatedev";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String GetDevInfo(String json_str) throws Exception{
        String URL = BaseUrl + GatewayManagementUrl + "/getdevinfo";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

    public String DeleteDev(String json_str) throws Exception{
        String URL = BaseUrl + GatewayManagementUrl + "/deletedev";
        int i = json_str.indexOf("auth_token");
        if (i == -1) {
            String tmp = ",\"auth_token\":\"" + auth_token + "\"}";
            String subStr = json_str.substring(0, json_str.length() - 1);
            json_str = subStr + tmp;
        }
        APost.ApiResponseParser(URL, json_str);
        return APost.GetResponse();
    }

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

    private String auth_token;
    private String username;
    private String password;

    private static final String BaseUrl =
        "http://115.29.204.44:8080/ecloud/";
    private static final String DeviceManagementUrl = "v1/dev/ctl";
    private static final String ProjectManagementUrl = "v1/admin/project";
    private static final String GatewayManagementUrl = "v1/admin/dev";

    private Post APost;
}

