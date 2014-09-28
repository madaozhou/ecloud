package com.intel.iotg.ecloud.cli;

import com.intel.iotg.ecloud.cli.DeviceControl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;

import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import jline.console.completer.FileNameCompleter;
import jline.console.completer.StringsCompleter;

import org.apache.commons.cli.*;
import org.apache.commons.cli.Options;

import javax.json.*;

public class EcloudCli {
    public static void main(String[] args) throws Exception{
        EcloudCli cli = new EcloudCli();
        cli.cons = new ConsoleReader();
        cli.completors = new LinkedList<Completer>();
        cli.completors.add(new StringsCompleter(
                    "-h",
                    "-help",
                    "-q",
                    "-quit",
                    "-Login",
                    "-ListProjects",
                    "-AddProject",
                    "-UpdateProject",
                    "-GetProjectInfo",
                    "-DeleteProject",
                    "-GetProjectStats",
                    "-ListProjectUser",
                    "-ListDevInProject",
                    "-ListDevByGroup",
                    "-GetVer",
                    "-GetCfg",
                    "-SetCfg",
                    "-Ping",
                    "-UpdateFirmware",
                    "-System",
                    "-GetAppList",
                    "-StartApp",
                    "-StopApp",
                    "-InstallApp",
                    "-Reboot",
                    "-Filec2d",
                    "-Filed2c",
                    "-RpcCall",
                    "-AddDev",
                    "-UpdateDev",
                    "-GetDevInfo",
                    "-DeleteDev"
                    ));
        if (cli.cons != null) {
            for (Completer c : cli.completors) {
                cli.cons.addCompleter(c);
            }
            System.out.println("Hi, welcome to Ecloud!");
            System.out.println("enter -help for help.");
            PrintWriter writer = new PrintWriter(cli.cons.getOutput());
            cli.cons.setPrompt("Ecloud ");
            while (true) {
                String str1 = cli.cons.readLine();
                String[] inputArgs = str1.split(" ");
                if (cli.engine(inputArgs) == 0)
                    break;
            }
        }
    }

    public int engine(String[] args) throws Exception{
        parser = new BasicParser();
        try {
            cl = parser.parse(options, args);
            if (cl.hasOption("help")) {
                helpPage();
                return 1;
            }
            if (cl.hasOption("quit")) {
                System.out.println("bye");
                return 0;
            }
            if (cl.hasOption("Login")) {
                cons.setPrompt("username:");
                String username = cons.readLine();
                cons.setPrompt(null);
                String password = cons.readLine("password:", '*');
                cons.setPrompt("Ecloud ");
                dc.setUserName(username);
                dc.setPassWord(password);
                String json_str;
                JsonObject jobj = Json.createObjectBuilder()
                    .add("username", username)
                    .build();
                json_str = jobj.toString();
                int retcode = dc.Login(json_str);
                cons.setPrompt(null);
                if (retcode == 0) {
                    System.out.println("Login success, enter -ListProjects to get project list.");
                }
                else {
                    System.out.println("Login fail!");
                }
                cons.setPrompt("Ecloud ");
                return 1;
            }

            /* **********************************************************
             * Device management
             * *********************************************************/

            if (cl.hasOption("GetVer")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetVer");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .build();
                    String retStr = dc.GetVer(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        String version = jobj.getString("ver");
                        System.out.println("---------------------------------------");
                        System.out.println("version : " + version);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetCfg")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetCfg");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .build();
                    String retStr = dc.GetCfg(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        String version = jobj.getString("cfg");
                        System.out.println("---------------------------------------");
                        System.out.println("version : " + version);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("SetCfg")) {
                try {
                    String argsArray[] = cl.getOptionValues("SetCfg");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .add("config", argsArray[2])
                        .build();
                    dc.SetCfg(jobj.toString());
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("Ping")) {
                try {
                    String argsArray[] = cl.getOptionValues("Ping");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .build();
                    String retStr = dc.Ping(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        String data = jobj.getString("data");
                        System.out.println("---------------------------------------");
                        System.out.println("return data : " + data);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("UpdateFirmware")) {
                try {
                    String argsArray[] = cl.getOptionValues("UpdateFirmware");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .add("srcaddr", argsArray[2])
                        .add("version", argsArray[3])
                        .build();
                    dc.UpdateFirmware(jobj.toString());
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("System")) {
                try {
                    String argsArray[] = cl.getOptionValues("System");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .add("cmd", argsArray[2])
                        .build();
                    dc.System(jobj.toString());
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetAppList")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetAppList");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .build();
                    String retStr = dc.GetAppList(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        JsonArray apps = jobj.getJsonArray("apps");
                        Iterator iter = apps.iterator();
                        System.out.println("---------------------------------------");
                        while (iter.hasNext()) {
                            System.out.println(iter.next().toString());
                        }
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("StartApp")) {
                try {
                    String argsArray[] = cl.getOptionValues("StartApp");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    String params = "";
                    if (argsArray.length == 4) {
                        params = argsArray[3];
                    }
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .add("name", argsArray[2])
                        .add("params", params)
                        .build();
                    String retStr = dc.StartApp(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        System.out.println("---------------------------------------");
                        System.out.println(argsArray[2] + " started.");
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("StopApp")) {
                try {
                    String argsArray[] = cl.getOptionValues("StopApp");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .add("name", argsArray[2])
                        .build();
                    String retStr = dc.StopApp(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        System.out.println("---------------------------------------");
                        System.out.println(argsArray[2] + " stoped.");
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("InstallApp")) {
                try {
                    String argsArray[] = cl.getOptionValues("InstallApp");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .add("apppath", argsArray[2])
                        .build();
                    String retStr = dc.InstallApp(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        System.out.println("---------------------------------------");
                        System.out.println("App installed");
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("Reboot")) {
                try {
                    String argsArray[] = cl.getOptionValues("Reboot");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .build();
                    String retStr = dc.Reboot(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        System.out.println("---------------------------------------");
                        System.out.println("Reboot is scheduled");
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("Filec2d")) {
                try {
                    String argsArray[] = cl.getOptionValues("Filec2d");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .add("src", argsArray[2])
                        .add("dest", argsArray[3])
                        .build();
                    dc.Filec2d(jobj.toString());
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("Filed2c")) {
                try {
                    String argsArray[] = cl.getOptionValues("Filed2c");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .add("src", argsArray[2])
                        .add("dest", argsArray[3])
                        .build();
                    dc.Filed2c(jobj.toString());
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("RpcCall")) {
                try {
                    String argsArray[] = cl.getOptionValues("RpcCall");
                    String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("device", device)
                        .add("cmd", argsArray[2])
                        .build();
                    dc.RpcCall(jobj.toString());
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }

            /* **********************************************************
             * project managment
             * *********************************************************/

            if (cl.hasOption("ListProjects")) {
                try {
                    String retStr = dc.ListProjects();
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    JsonObject jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        JsonArray projects = jobj.getJsonArray("projects");
                        Iterator iter = projects.iterator();
                        System.out.println("---------------------------------------");
                        while (iter.hasNext()) {
                            System.out.println(iter.next().toString());
                        }
                        System.out.println("---------------------------------------");
                        System.out.println("enter -ListDevInProject to get device id");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("AddProject")) {
                try {
                    String argsArray[] = cl.getOptionValues("AddProject");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("name", argsArray[0])
                        .add("vendor", argsArray[1])
                        .add("info", argsArray[2])
                        .add("tag", argsArray[3])
                        .build();
                    String retStr = dc.AddProject(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        System.out.println("---------------------------------------");
                        System.out.println("added project_id : " + argsArray[0]);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetProjectInfo")) {
                try {
                    String projectId = cl.getOptionValue("GetProjectInfo");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", projectId)
                        .build();
                    String retStr = dc.GetProjectInfo(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        //jobj.remove("retcode");
                        System.out.println("---------------------------------------");
                        System.out.println(jobj.toString());
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("UpdateProject")) {
                try {
                    String argsArray[] = cl.getOptionValues("UpdateProject");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", argsArray[0])
                        .add("name", argsArray[1])
                        .add("info", argsArray[2])
                        .add("tag", argsArray[3])
                        .build();
                    String retStr = dc.UpdateProject(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        jobj = Json.createObjectBuilder()
                            .add("project_id", argsArray[0])
                            .build();
                        retStr = dc.GetProjectInfo(jobj.toString());
                        System.out.println("---------------------------------------");
                        System.out.println(retStr);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("DeleteProject")) {
                try {
                    String projectId = cl.getOptionValue("DeleteProject");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", projectId)
                        .build();
                    String retStr = dc.DeleteProject(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        System.out.println("---------------------------------------");
                        System.out.println(projectId + " is deleted.");
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("ListProjectUser")) {
                try {
                    String projectId = cl.getOptionValue("ListProjectUser");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", projectId)
                        .build();
                    String retStr = dc.ListProjectUser(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        JsonArray users = jobj.getJsonArray("users");
                        Iterator iter = users.iterator();
                        System.out.println("---------------------------------------");
                        while (iter.hasNext()) {
                            System.out.println(iter.next().toString());
                        }
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("ListDevInProject")) {
                try {
                    String argsArray[] = cl.getOptionValues("ListDevInProject");
                    String filter = "";
                    if (argsArray.length == 2) {
                        filter = argsArray[1];
                    }
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", argsArray[0])
                        .add("filter", filter)
                        .build();
                    String retStr = dc.ListDevInProject(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        JsonArray gateways = jobj.getJsonArray("gateways");
                        Iterator iter = gateways.iterator();
                        System.out.println("---------------------------------------");
                        while (iter.hasNext()) {
                            System.out.println(iter.next().toString());
                        }
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("ListDevByGroup")) {
                try {
                    String argsArray[] = cl.getOptionValues("ListDevByGroup");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", argsArray[0])
                        .add("group_id", argsArray[1])
                        .build();
                    dc.ListDevByGroup(jobj.toString());
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetProjectStats")) {
                try {
                    String projectId = cl.getOptionValue("GetProjectStats");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", projectId)
                        .build();
                    String retStr = dc.GetProjectStats(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        String stats = jobj.getString("stats");
                        System.out.println("---------------------------------------");
                        System.out.println("stats : " + stats);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }

            if (cl.hasOption("AddDev")) {
                try {
                    String argsArray[] = cl.getOptionValues("AddDev");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", argsArray[0])
                        .add("device_hw_id", argsArray[1])
                        .add("tag", argsArray[2])
                        .add("name", argsArray[3])
                        .add("vendor", argsArray[4])
                        .add("systeminfo", argsArray[5])
                        .add("location", argsArray[6])
                        .add("groupids", argsArray[7])
                        .build();
                    String retStr = dc.AddDev(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        System.out.println("---------------------------------------");
                        System.out.println("Success.");
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("UpdateDev")) {
                try {
                    String argsArray[] = cl.getOptionValues("UpdateDev");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", argsArray[0])
                        .add("gateway_id", argsArray[1])
                        .add("device_hw_id", argsArray[2])
                        .add("tag", argsArray[3])
                        .add("name", argsArray[4])
                        .add("vendor", argsArray[5])
                        .add("systeminfo", argsArray[7])
                        .add("location", argsArray[8])
                        .add("groupids", argsArray[9])
                        .build();
                    String retStr = dc.UpdateDev(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        jobj = Json.createObjectBuilder()
                            .add("project_id", argsArray[0])
                            .add("gateway_id", argsArray[1])
                            .build();
                        retStr = dc.GetDevInfo(jobj.toString());
                        System.out.println("---------------------------------------");
                        System.out.println(retStr);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetDevInfo")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetDevInfo");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", argsArray[0])
                        .add("gateway_id", argsArray[1])
                        .build();
                    String retStr = dc.GetDevInfo(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        //jobj.remove("retcode");
                        System.out.println("---------------------------------------");
                        System.out.println(jobj.toString());
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("DeleteDev")) {
                try {
                    String argsArray[] = cl.getOptionValues("DeleteDev");
                    JsonObject jobj = Json.createObjectBuilder()
                        .add("project_id", argsArray[0])
                        .add("gateway_id", argsArray[1])
                        .build();
                    String retStr = dc.DeleteDev(jobj.toString());
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        System.out.println("---------------------------------------");
                        System.out.println(argsArray[1] + " is deleted.");
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    else {
                        System.out.printf("retcode = %d\n", retcode);
                    }
                }catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
        }
        return 1;
    }

    public EcloudCli() {
        dc = new DeviceControl();
        options = new Options();
        help =
            OptionBuilder
                .withLongOpt("help")
                .withDescription("print help information")
                .create("h");
        quit =
            OptionBuilder
                .withLongOpt("quit")
                .withDescription("quit cli")
                .create("q");
        Login =
            OptionBuilder
                .withDescription("Login into energy cloud System")
                .create("Login");
        GetVer =
            OptionBuilder
                .withDescription("get version of gateway")
                .withArgName("project_id> <device_id")
                .hasArgs(2)
                .create("GetVer");
        GetCfg =
            OptionBuilder
                .withDescription("get configuration of gateway")
                .withArgName("project_id> <device_id")
                .hasArgs(2)
                .create("GetCfg");
        SetCfg =
            OptionBuilder
                .withDescription("configure gateway")
                .withArgName("project_id> <device_id> <config")
                .hasArgs(3)
                .create("SetCfg");
        Ping =
            OptionBuilder
                .withDescription("detect device's status")
                .withArgName("project_id> <device_id")
                .hasArgs(2)
                .create("Ping");
        UpdateFirmware =
            OptionBuilder
                .withDescription("update firmware of gateway")
                .withArgName("project_id> <device_id> <srcaddr> <version")
                .hasArgs(4)
                .create("UpdateFirmware");
        system =
            OptionBuilder
                .withDescription("run System cmd")
                .withArgName("project_id> <device_id> <cmd")
                .hasArgs(3)
                .create("System");
        GetAppList =
            OptionBuilder
                .withDescription("get application list")
                .withArgName("project_id> <device_id")
                .hasArgs(2)
                .create("GetAppList");
        StartApp =
            OptionBuilder
                .withDescription("start application")
                .withArgName("project_id> <device_id> <name> <params")
                .hasArgs(4)
                .create("StartApp");
        StopApp =
            OptionBuilder
                .withDescription("stop application")
                .withArgName("project_id> <device_id> <name")
                .hasArgs(3)
                .create("StopApp");
        InstallApp =
            OptionBuilder
                .withDescription("install application")
                .withArgName("project_id> <device_id> <apppath")
                .hasArgs(3)
                .create("InstallApp");
        Reboot =
            OptionBuilder
                .withDescription("reboot device")
                .withArgName("project_id> <device_id")
                .hasArgs(2)
                .create("GetAppList");
        Filec2d =
            OptionBuilder
                .withDescription("tansfer file from cloud to device")
                .withArgName("project_id> <device_id> <src> <dest")
                .hasArgs(4)
                .create("Filec2d");
        Filed2c =
            OptionBuilder
                .withDescription("tansfer file from device to cloud")
                .withArgName("project_id> <device_id> <src> <dest")
                .hasArgs(4)
                .create("Filed2c");
        RpcCall =
            OptionBuilder
                .withDescription("call device's RPC")
                .withArgName("project_id> <device_id> <cmd")
                .hasArgs(3)
                .create("RpcCall");

        ListProjects =
            OptionBuilder
                .withDescription("list existed projects")
                .create("ListProjects");
        AddProject =
            OptionBuilder
                .withDescription("add projects")
                .withArgName("name> <vendor> <info> <tag")
                .hasArgs(4)
                .create("AddProject");
        GetProjectInfo =
            OptionBuilder
                .withDescription("get project's information")
                .withArgName("project_id")
                .hasArgs(1)
                .create("GetProjectInfo");
        UpdateProject =
            OptionBuilder
                .withDescription("update project")
                .withArgName("project_id> <name> <info> <tag")
                .hasArgs(4)
                .create("UpdateProject");
        DeleteProject =
            OptionBuilder
                .withDescription("Delete the given project")
                .withArgName("project_id")
                .hasArgs(1)
                .create("DeleteProject");
        ListProjectUser =
            OptionBuilder
                .withDescription("list the users of given project")
                .withArgName("project_id")
                .hasArgs(1)
                .create("ListProjectUser");
        ListDevInProject =
            OptionBuilder
                .withDescription("list the devices of given project")
                .withArgName("project_id> <filter")
                .hasArgs(2)
                .create("ListDevInProject");
        ListDevByGroup =
            OptionBuilder
                .withDescription("list the devices of given group")
                .withArgName("project_id> <group_id")
                .hasArgs(2)
                .create("ListDevByGroup");
        GetProjectStats =
            OptionBuilder
                .withDescription("get the stats of given project")
                .withArgName("project_id")
                .hasArgs(1)
                .create("GetProjectStats");

        AddDev =
            OptionBuilder
                .withDescription("add device")
                .withArgName("project_id> <device_hw_id> <tag> <name> <vendor> <systeminfo> <location> <groupids")
                .hasArgs(8)
                .create("AddDev");
        UpdateDev =
            OptionBuilder
                .withDescription("update device")
                .withArgName("project_id> <gateway_id> <device_hw_id> <tag> <name> <vendor> <systeminfo> <location> <groupids")
                .hasArgs(9)
                .create("UpdateDev");
        GetDevInfo =
            OptionBuilder
                .withDescription("get device information")
                .withArgName("project_id> <gateway_id")
                .hasArgs(2)
                .create("GetDevInfo");
        DeleteDev =
            OptionBuilder
                .withDescription("delete device")
                .withArgName("project_id> <gateway_id")
                .hasArgs(2)
                .create("DeleteDev");

        options.addOption(help);
        options.addOption(quit);
        options.addOption(Login);
        options.addOption(GetVer);
        options.addOption(GetCfg);
        options.addOption(SetCfg);
        options.addOption(Ping);
        options.addOption(UpdateFirmware);
        options.addOption(system);
        options.addOption(GetAppList);
        options.addOption(StartApp);
        options.addOption(StopApp);
        options.addOption(InstallApp);
        options.addOption(Reboot);
        options.addOption(Filec2d);
        options.addOption(Filed2c);
        options.addOption(RpcCall);
        options.addOption(ListProjects);
        options.addOption(AddProject);
        options.addOption(GetProjectInfo);
        options.addOption(UpdateProject);
        options.addOption(DeleteProject);
        options.addOption(ListProjectUser);
        options.addOption(ListDevInProject);
        options.addOption(ListDevByGroup);
        options.addOption(GetProjectStats);
        options.addOption(AddDev);
        options.addOption(UpdateDev);
        options.addOption(GetDevInfo);
        options.addOption(DeleteDev);
        formatter = new HelpFormatter();
    }

    private void helpPage() {
        formatter.setWidth(200);
        formatter.printHelp("Ecloud", options);
    }

    private ConsoleReader cons;
    List<Completer> completors;
    private Options options;
    private CommandLine cl;
    private BasicParser parser;
    private HelpFormatter formatter;
    private DeviceControl dc;

    private Option help;
    private Option quit;
    private Option Login;

    private Option ListProjects;
    private Option AddProject;
    private Option GetProjectInfo;
    private Option UpdateProject;
    private Option DeleteProject;
    private Option ListProjectUser;
    private Option ListDevInProject;
    private Option ListDevByGroup;
    private Option GetProjectStats;

    private Option GetVer;
    private Option GetCfg;
    private Option SetCfg;
    private Option Ping;
    private Option UpdateFirmware;
    private Option system;
    private Option GetAppList;
    private Option StartApp;
    private Option StopApp;
    private Option InstallApp;
    private Option Reboot;
    private Option Filec2d;
    private Option Filed2c;
    private Option RpcCall;

    private Option AddDev;
    private Option UpdateDev;
    private Option GetDevInfo;
    private Option DeleteDev;
}
