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

public class TestJline {
    public static void main(String[] args) throws Exception{
        TestJline cli = new TestJline();
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
            System.out.println("enter -helo for help.");
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
                    System.out.println("Login success, you should call ListProjects.");
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
                String device = cl.getOptionValue("GetVer");
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
                return 1;
            }
            if (cl.hasOption("GetCfg")) {
                String device = cl.getOptionValue("GetCfg");
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
                return 1;
            }
            if (cl.hasOption("SetCfg")) {
                String argsArray[] = cl.getOptionValues("SetCfg");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", argsArray[0])
                    .add("config", argsArray[1])
                    .build();
                dc.SetCfg(jobj.toString());
                return 1;
            }
            if (cl.hasOption("Ping")) {
                String device = cl.getOptionValue("Ping");
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
                return 1;
            }
            if (cl.hasOption("UpdateFirmware")) {
                String argsArray[] = cl.getOptionValues("UpdateFirmware");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", argsArray[0])
                    .add("srcaddr", argsArray[1])
                    .add("version", argsArray[2])
                    .build();
                dc.UpdateFirmware(jobj.toString());
                return 1;
            }
            if (cl.hasOption("System")) {
                String argsArray[] = cl.getOptionValues("System");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", argsArray[0])
                    .add("cmd", argsArray[1])
                    .build();
                dc.System(jobj.toString());
                return 1;
            }
            if (cl.hasOption("GetAppList")) {
                String device = cl.getOptionValue("GetAppList");
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
                return 1;
            }
            if (cl.hasOption("StartApp")) {
                String argsArray[] = cl.getOptionValues("StartApp");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", argsArray[0])
                    .add("name", argsArray[1])
                    .add("params", argsArray[2])
                    .build();
                dc.StartApp(jobj.toString());
                return 1;
            }
            if (cl.hasOption("StopApp")) {
                String argsArray[] = cl.getOptionValues("StopApp");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", argsArray[0])
                    .add("name", argsArray[1])
                    .build();
                dc.StopApp(jobj.toString());
                return 1;
            }
            if (cl.hasOption("InstallApp")) {
                String argsArray[] = cl.getOptionValues("InstallApp");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", argsArray[0])
                    .add("apppath", argsArray[1])
                    .build();
                dc.InstallApp(jobj.toString());
                return 1;
            }
            if (cl.hasOption("Reboot")) {
                String device = cl.getOptionValue("Reboot");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", device)
                    .build();
                dc.Reboot(jobj.toString());
                return 1;
            }
            if (cl.hasOption("Filec2d")) {
                String argsArray[] = cl.getOptionValues("Filec2d");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", argsArray[0])
                    .add("src", argsArray[1])
                    .add("dest", argsArray[2])
                    .build();
                dc.Filec2d(jobj.toString());
                return 1;
            }
            if (cl.hasOption("Filed2c")) {
                String argsArray[] = cl.getOptionValues("Filed2c");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", argsArray[0])
                    .add("src", argsArray[1])
                    .add("dest", argsArray[2])
                    .build();
                dc.Filed2c(jobj.toString());
                return 1;
            }
            if (cl.hasOption("RpcCall")) {
                String argsArray[] = cl.getOptionValues("RpcCall");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("device", argsArray[0])
                    .add("cmd", argsArray[1])
                    .build();
                dc.RpcCall(jobj.toString());
                return 1;
            }

            /* **********************************************************
             * project managment
             * *********************************************************/

            if (cl.hasOption("ListProjects")) {
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
                return 1;
            }
            if (cl.hasOption("AddProject")) {
                String argsArray[] = cl.getOptionValues("AddProject");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("name", argsArray[0])
                    .add("vendor", argsArray[1])
                    .add("info", argsArray[2])
                    .add("tag", argsArray[3])
                    .build();
                dc.AddProject(jobj.toString());
                return 1;
            }
            if (cl.hasOption("GetProjectInfo")) {
                String projectId = cl.getOptionValue("GetProjectInfo");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("project_id", projectId)
                    .build();
                dc.GetProjectInfo(jobj.toString());
                return 1;
            }
            if (cl.hasOption("UpdateProject")) {
                String argsArray[] = cl.getOptionValues("UpdateProject");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("project_id", argsArray[0])
                    .add("name", argsArray[1])
                    .add("info", argsArray[2])
                    .add("tag", argsArray[3])
                    .build();
                dc.UpdateProject(jobj.toString());
                return 1;
            }
            if (cl.hasOption("DeleteProject")) {
                String projectId = cl.getOptionValue("DeleteProject");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("project_id", projectId)
                    .build();
                dc.DeleteProject(jobj.toString());
                return 1;
            }
            if (cl.hasOption("ListProjectUser")) {
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
                return 1;
            }
            if (cl.hasOption("ListDevInProject")) {
                String argsArray[] = cl.getOptionValues("ListDevInProject");
                String filter = "";
                if (argsArray.length == 2) {
                    filter = argsArray[1];
                }
                System.out.println(argsArray[0]);
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
                return 1;
            }
            if (cl.hasOption("ListDevByGroup")) {
                String argsArray[] = cl.getOptionValues("ListDevByGroup");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("project_id", argsArray[0])
                    .add("group_id", argsArray[1])
                    .build();
                dc.ListDevByGroup(jobj.toString());
                return 1;
            }
            if (cl.hasOption("GetProjectStats")) {
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
                return 1;
            }

            if (cl.hasOption("AddDev")) {
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
                dc.AddDev(jobj.toString());
                return 1;
            }
            if (cl.hasOption("UpdateDev")) {
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
                dc.UpdateDev(jobj.toString());
                return 1;
            }
            if (cl.hasOption("GetDevInfo")) {
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
                return 1;
            }
            if (cl.hasOption("DeleteDev")) {
                String argsArray[] = cl.getOptionValues("DeleteDev");
                JsonObject jobj = Json.createObjectBuilder()
                    .add("project_id", argsArray[0])
                    .add("gateway_id", argsArray[1])
                    .build();
                dc.DeleteDev(jobj.toString());
                return 1;
            }
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
        }
        return 1;
    }

    public TestJline() {
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
                .withArgName("device")
                .hasArgs(1)
                .create("GetVer");
        GetCfg =
            OptionBuilder
                .withDescription("get configuration of gateway")
                .withArgName("device")
                .hasArgs(1)
                .create("GetCfg");
        SetCfg =
            OptionBuilder
                .withDescription("configure gateway")
                .withArgName("device> <config")
                .hasArgs(2)
                .create("SetCfg");
        Ping =
            OptionBuilder
                .withDescription("detect device's status")
                .withArgName("device")
                .hasArgs(1)
                .create("Ping");
        UpdateFirmware =
            OptionBuilder
                .withDescription("update firmware of gateway")
                .withArgName("device> <srcaddr> <version")
                .hasArgs(3)
                .create("UpdateFirmware");
        system =
            OptionBuilder
                .withDescription("run System cmd")
                .withArgName("device> <cmd")
                .hasArgs(2)
                .create("System");
        GetAppList =
            OptionBuilder
                .withDescription("get application list")
                .withArgName("device")
                .hasArgs(1)
                .create("GetAppList");
        StartApp =
            OptionBuilder
                .withDescription("start application")
                .withArgName("device> <name> <params")
                .hasArgs(3)
                .create("StartApp");
        StopApp =
            OptionBuilder
                .withDescription("stop application")
                .withArgName("device> <name")
                .hasArgs(2)
                .create("StopApp");
        InstallApp =
            OptionBuilder
                .withDescription("install application")
                .withArgName("device> <apppath")
                .hasArgs(2)
                .create("InstallApp");
        Reboot =
            OptionBuilder
                .withDescription("reboot device")
                .withArgName("device")
                .hasArgs(1)
                .create("Reboot");
        Filec2d =
            OptionBuilder
                .withDescription("tansfer file from cloud to device")
                .withArgName("device> <src> <dest")
                .hasArgs(3)
                .create("Filec2d");
        Filed2c =
            OptionBuilder
                .withDescription("tansfer file from device to cloud")
                .withArgName("device> <src> <dest")
                .hasArgs(3)
                .create("Filed2c");
        RpcCall =
            OptionBuilder
                .withDescription("call device's RPC")
                .withArgName("device> <cmd")
                .hasArgs(2)
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
                .create("AddDev");
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
