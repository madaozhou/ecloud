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
    private ConsoleReader cons;
    private List<Completer> completors;
    private Options options;
    private CommandLine cl;
    private BasicParser parser;
    private HelpFormatter formatter;
    private DeviceControl dc;

    private String promptInfo = "Ecloud$ ";
    private String access;
    private boolean isSystemAdmin;

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

    private Option AddUser;
    private Option UserLogout;
    private Option SetPass;
    private Option UpdateUser;
    private Option GetUsrInfo;

    private Option AddDev;
    private Option UpdateDev;
    private Option GetDevInfo;
    private Option DeleteDev;
    private Option GetDevKey;

    private Option AddGroup;
    private Option UpdateGroup;
    private Option GetGroupInfo;
    private Option ListGroups;
    private Option ListGroupPath;
    private Option DeleteGroup;

    private Option AddDataPoint;
    private Option UpdateDataPoint;
    private Option DeleteDataPoint;
    private Option GetDataPointInfo;
    private Option ListDataPoints;
    private Option ListDataPointByGroup;

    private Option RawLogQuery;
    private Option RawLogExport;

    private Option AlertQuery;

    private Option RawDataQuery;
    private Option StatsDataQuery;
    private Option AlarmDataQuery;
    private Option EventDataQuery;

    private String loginUserName;
    private String role;

    public static void main(String[] args) {
        EcloudCli cli = new EcloudCli();
        try {
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
                        "-DeleteDev",
                        "-GetDevKey",
                        "-AddUser",
                        "-UserLogout",
                        "-SetPass",
                        "-UpdateUser",
                        "-GetUsrInfo",
                        "-AddGroup",
                        "-UpdateGroup",
                        "-GetGroupInfo",
                        "-ListGroups",
                        "-ListGroupPath",
                        "-DeleteGroup",
                        "-AddDataPoint",
                        "-UpdateDataPoint",
                        "-DeleteDataPoint",
                        "-GetDataPointInfo",
                        "-ListDataPoints",
                        "-ListDataPointByGroup",
                        "-RawLogQuery",
                        "-RawLogExport",
                        "-AlertQuery",
                        "-RawDataQuery",
                        "-StatsDataQuery",
                        "-AlarmDataQuery",
                        "-EventDataQuery"
                        ));
            if (cli.cons != null) {
                for (Completer c : cli.completors) {
                    cli.cons.addCompleter(c);
                }
                System.out.println("Hi, welcome to Ecloud!");
                System.out.println("enter -help for help.");
                PrintWriter writer = new PrintWriter(cli.cons.getOutput());
                cli.cons.setPrompt("Ecloud$ ");
                while (true) {
                    String str1 = cli.cons.readLine();
                    String[] inputArgs = str1.split(" ");
                    if (cli.engine(inputArgs) == 0)
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int engine(String[] args) {
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
                loginUserName = username;
                cons.setPrompt(null);
                String password = cons.readLine("password:", '*');
                cons.setPrompt("Ecloud$ ");
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
                    String ret = dc.GetUsrInfo(json_str);
                    ByteArrayInputStream in = new ByteArrayInputStream(ret.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    jobj = jsonReader.readObject();
                    role = jobj.getString("role");
                    int index = role.indexOf(",");
                    access = role.substring(0, index);
                    if (role.equals("system"))
                        isSystemAdmin = true;
                    else
                        isSystemAdmin = false;
                    promptInfo = "Ecloud[" + username + "-" + access + "]$ ";
                    System.out.println("Login success, enter -ListProjects to get project list.");
                }
                else {
                    System.out.println("Login fail!");
                }
                cons.setPrompt(promptInfo);
                return 1;
            }

            /**
             * Device management
             **/

            if (cl.hasOption("GetVer")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetVer");
                    if (argsArray == null || argsArray.length < 2) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                    }
                    return GetVer(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetCfg")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetCfg");
                    if (argsArray == null || argsArray.length < 2) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[2];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                    }
                    return GetCfg(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("SetCfg")) {
                try {
                    String argsArray[] = cl.getOptionValues("SetCfg");
                    if (argsArray == null || argsArray.length < 3) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        String config = getDeviceCfg();
                        argsArray = new String[3];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = config;
                    }
                    return SetCfg(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("Ping")) {
                try {
                    String argsArray[] = cl.getOptionValues("Ping");
                    if (argsArray == null || argsArray.length < 2) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[2];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                    }
                    return Ping(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("UpdateFirmware")) {
                try {
                    String argsArray[] = cl.getOptionValues("UpdateFirmware");
                    if (argsArray == null || argsArray.length < 4) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        String srcAddr = getSrcAddr();
                        String version = getVersion();
                        argsArray = new String[4];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = srcAddr;
                        argsArray[3] = version;
                    }
                    return UpdateFirmware(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("System")) {
                try {
                    String argsArray[] = cl.getOptionValues("System");
                    if (argsArray == null || argsArray.length < 3) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        String cmd = getCmd();
                        argsArray = new String[3];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = cmd;
                    }
                    return System(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetAppList")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetAppList");
                    if (argsArray == null || argsArray.length < 2) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[2];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                    }
                    return GetAppList(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("StartApp")) {
                try {
                    String argsArray[] = cl.getOptionValues("StartApp");
                    if (argsArray == null || argsArray.length < 3) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        String name = getName();
                        String params = getParams();
                        argsArray = new String[4];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = name;
                        argsArray[3] = params;
                    }
                    return StartApp(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("StopApp")) {
                try {
                    String argsArray[] = cl.getOptionValues("StopApp");
                    if (argsArray == null || argsArray.length < 3) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        String name = getName();
                        argsArray = new String[3];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = name;
                    }
                    return StopApp(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("InstallApp")) {
                try {
                    String argsArray[] = cl.getOptionValues("InstallApp");
                    if (argsArray == null || argsArray.length < 3) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        String appPath = getAppPath();
                        argsArray = new String[3];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = appPath;
                    }
                    return InstallApp(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("Reboot")) {
                try {
                    String argsArray[] = cl.getOptionValues("Reboot");
                    if (argsArray == null || argsArray.length < 2) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[2];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                    }
                    return Reboot(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("Filec2d")) {
                try {
                    String argsArray[] = cl.getOptionValues("Filec2d");
                    if (argsArray == null || argsArray.length < 4) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        String appPath = getAppPath();
                        String src = getSrc();
                        String dest = getDest();
                        argsArray = new String[4];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = src;
                        argsArray[3] = dest;
                    }
                    return Filec2d(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("Filed2c")) {
                try {
                    String argsArray[] = cl.getOptionValues("Filed2c");
                    if (argsArray == null || argsArray.length < 4) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        String appPath = getAppPath();
                        String src = getSrc();
                        String dest = getDest();
                        argsArray = new String[4];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = src;
                        argsArray[3] = dest;
                    }
                    return Filed2c(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("RpcCall")) {
                try {
                    String argsArray[] = cl.getOptionValues("RpcCall");
                    if (argsArray == null || argsArray.length < 3) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        String cmd = getCmd();
                        argsArray = new String[3];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = cmd;
                    }
                    return RpcCall(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }

            /**
             * project management
             **/

            if (cl.hasOption("ListProjects")) {
                try {
                    String retStr = dc.ListProjects();
                    ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
                    JsonReader jsonReader = Json.createReader(in);
                    JsonObject jobj = jsonReader.readObject();
                    int retcode = jobj.getInt("retcode", -1);
                    if (retcode == 0) {
                        JsonArray projects = jobj.getJsonArray("projects");
                        Iterator<JsonValue> iter = projects.iterator();
                        System.out.println("---------------------------------------");
                        while (iter.hasNext()) {
                            System.out.println(iter.next().toString());
                        }
                        System.out.println("---------------------------------------");
                        System.out.println("enter -ListDevInProject to get device id");
                        System.out.println();
                    }
                    else {
                        CodePaser(retcode);
                    }
                } catch (JsonException e) {
                    System.err.println(e.getMessage());
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("AddProject")) {
                try {
                    String argsArray[] = cl.getOptionValues("AddProject");
                    if (argsArray == null || argsArray.length < 4) {
                        argsArray = new String[4];
                        argsArray[0] = guide("project name");
                        argsArray[1] = guide("project vendor");
                        argsArray[2] = guide("project info");
                        argsArray[3] = guide("project tag");
                    }
                    return AddProject(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetProjectInfo")) {
                try {
                    String projectId = cl.getOptionValue("GetProjectInfo");
                    if (projectId == null)
                        projectId = getProjId();
                    return GetProjectInfo(projectId);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                }
            }
            if (cl.hasOption("UpdateProject")) {
                try {
                    String argsArray[] = cl.getOptionValues("UpdateProject");
                    if (argsArray == null || argsArray.length < 4) {
                        argsArray = new String[4];
                        argsArray[0] = getProjId();
                        argsArray[1] = guide("project vendor");
                        argsArray[2] = guide("project info");
                        argsArray[3] = guide("project tag");
                    }
                    return UpdateProject(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("DeleteProject")) {
                try {
                    String projectId = cl.getOptionValue("DeleteProject");
                    if (projectId == null)
                        projectId = getProjId();
                    return DeleteProject(projectId);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                }
            }
            if (cl.hasOption("ListProjectUser")) {
                try {
                    String projectId = cl.getOptionValue("ListProjectUser");
                    if (projectId == null)
                        projectId = getProjId();
                    return ListProjectUser(projectId);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                }
            }
            if (cl.hasOption("ListDevInProject")) {
                try {
                    String argsArray[] = cl.getOptionValues("ListDevInProject");
                    if (argsArray == null || argsArray.length < 1) {
                        argsArray = new String[2];
                        argsArray[0] = getProjId();
                        argsArray[1] = guide("filter");
                    }
                    return ListDevInProject(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("ListDevByGroup")) {
                try {
                    String argsArray[] = cl.getOptionValues("ListDevByGroup");
                    if (argsArray == null || argsArray.length < 3) {
                        argsArray = new String[3];
                        argsArray[0] = getProjId();
                        argsArray[1] = "*";
                        argsArray[2] = "*";
                        ListGroups(argsArray);
                        argsArray[1] = guide("group_id");
                        argsArray[2] = guide("type");
                    }
                    return ListDevByGroup(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetProjectStats")) {
                try {
                    String projectId = cl.getOptionValue("GetProjectStats");
                    if (projectId == null) {
                        projectId = getProjId();
                    }
                    return GetProjectStats(projectId);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }

            /**
             * user management
             **/

            if (cl.hasOption("AddUser")) {
                try {
                    String argsArray[] = cl.getOptionValues("AddUser");
                    if (argsArray == null || argsArray.length < 2) {
                        argsArray = new String[2];
                        argsArray[0] = guide("username");
                        argsArray[1] = guide("role");
                    }
                    return AddUser(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("UserLogout")) {
                try {
                    return UserLogout();
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("SetPass")) {
                try {
                    String argsArray[] = cl.getOptionValues("SetPass");
                    if (argsArray == null || argsArray.length < 2) {
                        argsArray = new String[2];
                        argsArray[0] = guide("username");
                        argsArray[1] = guide("password");
                    }
                    return SetPass(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("UpdateUser")) {
                try {
                    String argsArray[] = cl.getOptionValues("UpdateUser");
                    if (argsArray == null || argsArray.length < 3) {
                        argsArray = new String[3];
                        argsArray[0] = guide("username");
                        argsArray[1] = guide("role");
                        argsArray[2] = guide("active");
                    }
                    return UpdateUser(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetUsrInfo")) {
                try {
                    String username = cl.getOptionValue("GetUsrInfo");
                    if (username == null) {
                        username = guide("username");
                    }
                    return GetUsrInfo(username);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }

            /**
             * group management
             **/

            if (cl.hasOption("AddGroup")) {
                try {
                    String argsArray[] = cl.getOptionValues("AddGroup");
                    if (argsArray == null || argsArray.length < 7) {
                        argsArray = new String[7];
                        argsArray[0] = getProjId();
                        argsArray[1] = guide("name");
                        argsArray[2] = guide("type");
                        argsArray[3] = guide("descriptions");
                        argsArray[4] = guide("userdata");
                        argsArray[5] = guide("tags");
                        argsArray[6] = guide("parentgroupid");
                    }
                    return AddGroup(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("UpdateGroup")) {
                try {
                    String argsArray[] = cl.getOptionValues("UpdateGroup");
                    if (argsArray == null || argsArray.length < 8) {
                        argsArray = new String[8];
                        argsArray[0] = getProjId();
                        argsArray[1] = "*";
                        argsArray[2] = "*";
                        ListGroups(argsArray);
                        argsArray[1] = guide("group_id");
                        argsArray[2] = guide("name");
                        argsArray[3] = guide("type");
                        argsArray[4] = guide("descriptions");
                        argsArray[5] = guide("userdata");
                        argsArray[6] = guide("tags");
                        argsArray[7] = guide("parentgroupid");
                    }
                    return UpdateGroup(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetGroupInfo")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetGroupInfo");
                    if (argsArray == null || argsArray.length < 2) {
                        argsArray = new String[3];
                        argsArray[0] = getProjId();
                        argsArray[1] = "*";
                        argsArray[2] = "*";
                        ListGroups(argsArray);
                        argsArray[1] = guide("group_id");
                    }
                    return GetGroupInfo(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("ListGroups")) {
                try {
                    String argsArray[] = cl.getOptionValues("ListGroups");
                    if (argsArray == null || argsArray.length < 3) {
                        argsArray = new String[3];
                        argsArray[0] = getProjId();
                        argsArray[1] = guide("group_id");
                        argsArray[2] = guide("type");
                    }
                    return ListGroups(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("ListGroupPath")) {
                try {
                    String argsArray[] = cl.getOptionValues("ListGroupPath");
                    if (argsArray == null || argsArray.length < 2) {
                        argsArray = new String[3];
                        argsArray[0] = getProjId();
                        argsArray[1] = "*";
                        argsArray[2] = "*";
                        ListGroups(argsArray);
                        argsArray[1] = guide("group_id");
                    }
                    return ListGroupPath(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("DeleteGroup")) {
                try {
                    String argsArray[] = cl.getOptionValues("DeleteGroup");
                    if (argsArray == null || argsArray.length < 2) {
                        argsArray = new String[3];
                        argsArray[0] = getProjId();
                        argsArray[1] = "*";
                        argsArray[2] = "*";
                        ListGroups(argsArray);
                        argsArray[1] = guide("group_id");
                    }
                    return DeleteGroup(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }

            /**
             * gateway management
             **/

            if (cl.hasOption("AddDev")) {
                try {
                    String argsArray[] = cl.getOptionValues("AddDev");
                    if (argsArray == null || argsArray.length < 8) {
                        argsArray = new String[8];
                        argsArray[0] = getProjId();
                        argsArray[1] = guide("device_hw_id");
                        argsArray[2] = guide("tag");
                        argsArray[3] = guide("name");
                        argsArray[4] = guide("vendor");
                        argsArray[5] = guide("systeminfo");
                        argsArray[6] = guide("location");
                        argsArray[7] = guide("groupids");
                    }
                    return AddDev(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("UpdateDev")) {
                try {
                    String argsArray[] = cl.getOptionValues("UpdateDev");
                    if (argsArray == null || argsArray.length < 9) {
                        argsArray = new String[9];
                        argsArray[0] = getProjId();
                        argsArray[1] = guide("gateway_id");
                        argsArray[2] = guide("device_hw_id");
                        argsArray[3] = guide("tag");
                        argsArray[4] = guide("name");
                        argsArray[5] = guide("vendor");
                        argsArray[6] = guide("systeminfo");
                        argsArray[7] = guide("location");
                        argsArray[8] = guide("groupids");
                    }
                    return UpdateDev(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetDevInfo")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetDevInfo");
                    if (argsArray == null || argsArray.length < 2) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[2];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                    }
                    return GetDevInfo(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("DeleteDev")) {
                try {
                    String argsArray[] = cl.getOptionValues("DeleteDev");
                    if (argsArray == null || argsArray.length < 2) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[2];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                    }
                    return DeleteDev(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetDevKey")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetDevKey");
                    if (argsArray == null || argsArray.length < 2) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[2];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                    }
                    return GetDevKey(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }

            /**
             * data point management
             **/

            if (cl.hasOption("AddDataPoint")) {
                try {
                    String argsArray[] = cl.getOptionValues("AddDataPoint");
                    if (argsArray == null || argsArray.length < 9) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[9];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        argsArray[2] = guide("name");
                        argsArray[3] = guide("datatype");
                        argsArray[4] = guide("dataunit");
                        argsArray[5] = guide("description");
                        argsArray[6] = guide("tag");
                        argsArray[7] = guide("userdata");
                        argsArray[8] = guide("groupid");
                    }
                    return AddDataPoint(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("UpdateDataPoint")) {
                try {
                    String argsArray[] = cl.getOptionValues("AddDataPoint");
                    if (argsArray == null || argsArray.length < 10) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[10];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        ListDataPoints(argsArray);
                        argsArray[2] = guide("datapoint_id");
                        argsArray[3] = guide("name");
                        argsArray[4] = guide("datatype");
                        argsArray[5] = guide("dataunit");
                        argsArray[6] = guide("description");
                        argsArray[7] = guide("tag");
                        argsArray[8] = guide("userdata");
                        argsArray[9] = guide("groupid");
                    }
                    return AddDataPoint(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("DeleteDataPoint")) {
                try {
                    String argsArray[] = cl.getOptionValues("DeleteDataPoint");
                    if (argsArray == null || argsArray.length < 3) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[3];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        ListDataPoints(argsArray);
                        argsArray[2] = guide("datapoint_id");
                    }
                    return DeleteDataPoint(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("GetDataPointInfo")) {
                try {
                    String argsArray[] = cl.getOptionValues("GetDataPointInfo");
                    if (argsArray == null || argsArray.length < 3) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[3];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                        ListDataPoints(argsArray);
                        argsArray[2] = guide("datapoint_id");
                    }
                    return GetDataPointInfo(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("ListDataPoints")) {
                try {
                    String argsArray[] = cl.getOptionValues("ListDataPoints");
                    if (argsArray == null || argsArray.length < 2) {
                        String projectId = getProjId();
                        String fakeArgs[];
                        fakeArgs = new String[1];
                        fakeArgs[0] = projectId;
                        ListDevInProject(fakeArgs);
                        String DeviceId = getDeviceId();
                        argsArray = new String[3];
                        argsArray[0] = projectId;
                        argsArray[1] = DeviceId;
                    }
                    return ListDataPoints(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("ListDataPointByGroup")) {
                try {
                    String argsArray[] = cl.getOptionValues("ListDataPointByGroup");
                    if (argsArray == null || argsArray.length < 2) {
                        argsArray = new String[3];
                        argsArray[0] = getProjId();
                        argsArray[1] = "*";
                        argsArray[2] = "*";
                        ListGroups(argsArray);
                        argsArray[1] = guide("group_id");
                    }
                    return ListDataPointByGroup(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            /**
             * log management
             **/

            if (cl.hasOption("RawLogQuery")) {
                try {
                    String argsArray[] = cl.getOptionValues("RawLogQuery");
                    if (argsArray == null || argsArray.length < 4) {
                        argsArray = new String[4];
                        argsArray[0] = guide("maxdatapoints");
                        argsArray[1] = guide("time_from");
                        argsArray[2] = guide("time_to");
                        argsArray[3] = guide("log_tag");
                    }
                    return RawLogQuery(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("RawLogExport")) {
                try {
                    String argsArray[] = cl.getOptionValues("RawLogExport");
                    if (argsArray == null || argsArray.length < 4) {
                        argsArray = new String[4];
                        argsArray[0] = guide("time_from");
                        argsArray[1] = guide("time_to");
                        argsArray[2] = guide("log_tag");
                        argsArray[3] = guide("output");
                    }
                    return RawLogExport(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            /**
             * data management
             **/

            if (cl.hasOption("RawDataQuery")) {
                try {
                    String argsArray[] = cl.getOptionValues("RawDataQuery");
                    if (argsArray == null || argsArray.length < 6) {
                        argsArray = new String[6];
                        argsArray[0] = guide("maxdatapoints");
                        argsArray[1] = guide("time_from");
                        argsArray[2] = guide("time_to");
                        argsArray[3] = guide("project_id");
                        argsArray[4] = guide("gateway_id");
                        argsArray[5] = guide("datapoint_id");
                    }
                    return RawDataQuery(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("StatsDataQuery")) {
                try {
                    String argsArray[] = cl.getOptionValues("StatsDataQuery");
                    if (argsArray == null || argsArray.length < 5) {
                        argsArray = new String[5];
                        argsArray[0] = guide("maxdatapoints");
                        argsArray[1] = guide("time_from");
                        argsArray[2] = guide("time_to");
                        argsArray[3] = guide("project_id");
                        argsArray[4] = guide("stats_id");
                    }
                    return StatsDataQuery(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("AlarmDataQuery")) {
                try {
                    String argsArray[] = cl.getOptionValues("AlarmDataQuery");
                    if (argsArray == null || argsArray.length < 5) {
                        argsArray = new String[5];
                        argsArray[0] = guide("maxdatapoints");
                        argsArray[1] = guide("time_from");
                        argsArray[2] = guide("time_to");
                        argsArray[3] = guide("project_id");
                        argsArray[4] = guide("tag");
                    }
                    return AlarmDataQuery(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
            if (cl.hasOption("EventDataQuery")) {
                try {
                    String argsArray[] = cl.getOptionValues("EventDataQuery");
                    if (argsArray == null || argsArray.length < 5) {
                        argsArray = new String[5];
                        argsArray[0] = guide("maxdatapoints");
                        argsArray[1] = guide("time_from");
                        argsArray[2] = guide("time_to");
                        argsArray[3] = guide("project_id");
                        argsArray[4] = guide("tag");
                    }
                    return EventDataQuery(argsArray);
                } catch (IllegalStateException e) {
                    System.err.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("lack of args.");
                }
                return 1;
            }
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
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
                .hasOptionalArgs(2)
                .create("GetVer");
        GetCfg =
            OptionBuilder
                .withDescription("get configuration of gateway")
                .withArgName("project_id> <device_id")
                .hasOptionalArgs(2)
                .create("GetCfg");
        SetCfg =
            OptionBuilder
                .withDescription("configure gateway")
                .withArgName("project_id> <device_id> <config")
                .hasOptionalArgs(3)
                .create("SetCfg");
        Ping =
            OptionBuilder
                .withDescription("detect device's status")
                .withArgName("project_id> <device_id")
                .hasOptionalArgs(2)
                .create("Ping");
        UpdateFirmware =
            OptionBuilder
                .withDescription("update firmware of gateway")
                .withArgName("project_id> <device_id> <srcaddr> <version")
                .hasOptionalArgs(4)
                .create("UpdateFirmware");
        system =
            OptionBuilder
                .withDescription("run System cmd")
                .withArgName("project_id> <device_id> <cmd")
                .hasOptionalArgs(3)
                .create("System");
        GetAppList =
            OptionBuilder
                .withDescription("get application list")
                .withArgName("project_id> <device_id")
                .hasOptionalArgs(2)
                .create("GetAppList");
        StartApp =
            OptionBuilder
                .withDescription("start application")
                .withArgName("project_id> <device_id> <name> <params")
                .hasOptionalArgs(4)
                .create("StartApp");
        StopApp =
            OptionBuilder
                .withDescription("stop application")
                .withArgName("project_id> <device_id> <name")
                .hasOptionalArgs(3)
                .create("StopApp");
        InstallApp =
            OptionBuilder
                .withDescription("install application")
                .withArgName("project_id> <device_id> <apppath")
                .hasOptionalArgs(3)
                .create("InstallApp");
        Reboot =
            OptionBuilder
                .withDescription("reboot device")
                .withArgName("project_id> <device_id")
                .hasOptionalArgs(2)
                .create("Reboot");
        Filec2d =
            OptionBuilder
                .withDescription("tansfer file from cloud to device")
                .withArgName("project_id> <device_id> <src> <dest")
                .hasOptionalArgs(4)
                .create("Filec2d");
        Filed2c =
            OptionBuilder
                .withDescription("tansfer file from device to cloud")
                .withArgName("project_id> <device_id> <src> <dest")
                .hasOptionalArgs(4)
                .create("Filed2c");
        RpcCall =
            OptionBuilder
                .withDescription("call device's RPC")
                .withArgName("project_id> <device_id> <cmd")
                .hasOptionalArgs(3)
                .create("RpcCall");

        ListProjects =
            OptionBuilder
                .withDescription("list existed projects")
                .create("ListProjects");
        AddProject =
            OptionBuilder
                .withDescription("add projects")
                .withArgName("name> <vendor> <info> <tag")
                .hasOptionalArgs(4)
                .create("AddProject");
        GetProjectInfo =
            OptionBuilder
                .withDescription("get project's information")
                .withArgName("project_id")
                .hasOptionalArgs(1)
                .create("GetProjectInfo");
        UpdateProject =
            OptionBuilder
                .withDescription("update project")
                .withArgName("project_id> <name> <info> <tag")
                .hasOptionalArgs(4)
                .create("UpdateProject");
        DeleteProject =
            OptionBuilder
                .withDescription("Delete the given project")
                .withArgName("project_id")
                .hasOptionalArgs(1)
                .create("DeleteProject");
        ListProjectUser =
            OptionBuilder
                .withDescription("list the users of given project")
                .withArgName("project_id")
                .hasOptionalArgs(1)
                .create("ListProjectUser");
        ListDevInProject =
            OptionBuilder
                .withDescription("list the devices of given project")
                .withArgName("project_id> <filter")
                .hasOptionalArgs(2)
                .create("ListDevInProject");
        ListDevByGroup =
            OptionBuilder
                .withDescription("list the devices of given group")
                .withArgName("project_id> <group_id> <type")
                .hasOptionalArgs(3)
                .create("ListDevByGroup");
        GetProjectStats =
            OptionBuilder
                .withDescription("get the stats of given project")
                .withArgName("project_id")
                .hasOptionalArgs(1)
                .create("GetProjectStats");

        AddDev =
            OptionBuilder
                .withDescription("add device")
                .withArgName("project_id> <device_hw_id> <tag> <name> <vendor> <systeminfo> <location> <groupids")
                .hasOptionalArgs(8)
                .create("AddDev");
        UpdateDev =
            OptionBuilder
                .withDescription("update device")
                .withArgName("project_id> <gateway_id> <device_hw_id> <tag> <name> <vendor> <systeminfo> <location> <groupids")
                .hasOptionalArgs(9)
                .create("UpdateDev");
        GetDevInfo =
            OptionBuilder
                .withDescription("get device information")
                .withArgName("project_id> <gateway_id")
                .hasOptionalArgs(2)
                .create("GetDevInfo");
        DeleteDev =
            OptionBuilder
                .withDescription("delete device")
                .withArgName("project_id> <gateway_id")
                .hasOptionalArgs(2)
                .create("DeleteDev");
        GetDevKey =
            OptionBuilder
                .withDescription("get device's key")
                .withArgName("projectId> <gateway_id")
                .hasOptionalArgs(2)
                .create("GetDevKey");

        AddUser =
            OptionBuilder
                .withDescription("add user ")
                .withArgName("username> <role")
                .hasOptionalArgs(2)
                .create("AddUser");
        UserLogout =
            OptionBuilder
                .withDescription("logout")
                .create("UserLogout");
        SetPass =
            OptionBuilder
                .withDescription("set password")
                .withArgName("username> <password")
                .hasOptionalArgs(2)
                .create("SetPass");
        UpdateUser =
            OptionBuilder
                .withDescription("updata user")
                .withArgName("username> <role> <active")
                .hasOptionalArgs(3)
                .create("UpdateUser");
        GetUsrInfo =
            OptionBuilder
                .withDescription("get user information")
                .withArgName("username")
                .hasOptionalArgs(1)
                .create("GetUsrInfo");

        AddGroup =
            OptionBuilder
                .withDescription("add group")
                .withArgName("project_id> <name> <type> <description> <userdata> <tags> <parentgroupid")
                .hasOptionalArgs(7)
                .create("AddGroup");
        UpdateGroup =
            OptionBuilder
                .withDescription("update group")
                .withArgName("project_id> <group_id> <name> <type> <description> <userdata> <tags> <parentgroupid")
                .hasOptionalArgs(8)
                .create("UpdateGroup");
        GetGroupInfo =
            OptionBuilder
                .withDescription("GetGroupInfo")
                .withArgName("project_id> <group_id")
                .hasOptionalArgs(2)
                .create("GetGroupInfo");
        ListGroups =
            OptionBuilder
                .withDescription("list group related to input group_id")
                .withArgName("project_id> <group_id> <type")
                .hasOptionalArgs(3)
                .create("ListGroups");
        ListGroupPath =
            OptionBuilder
                .withDescription("list group's hierarchy")
                .withArgName("project_id> <group_id")
                .hasOptionalArgs(2)
                .create("ListGroupPath");
        DeleteGroup =
            OptionBuilder
                .withDescription("delete group")
                .withArgName("project_id> <group_id")
                .hasOptionalArgs(2)
                .create("DeleteGroup");

        AddDataPoint =
            OptionBuilder
                .withDescription("add data point")
                .withArgName("project_id> <gateway_id> <name> <datatype> <dataunit> <description> <tag> <userdata> <groupid")
                .hasOptionalArgs(9)
                .create("AddDataPoint");
        UpdateDataPoint =
            OptionBuilder
                .withDescription("add data point")
                .withArgName("project_id> <gateway_id> <datapoint_id> <name> <datatype> <dataunit> <description> <tag> <userdata> <groupid")
                .hasOptionalArgs(10)
                .create("UpdateDataPoint");
        DeleteDataPoint =
            OptionBuilder
                .withDescription("delete data point")
                .withArgName("project_id> <gateway_id> <datapoint_id")
                .hasOptionalArgs(3)
                .create("DeleteDataPoint");
        GetDataPointInfo =
            OptionBuilder
                .withDescription("get data point information")
                .withArgName("project_id> <gateway_id> <datapoint_id")
                .hasOptionalArgs(3)
                .create("GetDataPointInfo");
        ListDataPoints =
            OptionBuilder
                .withDescription("list data point")
                .withArgName("project_id> <gateway_id")
                .hasOptionalArgs(2)
                .create("ListDataPoints");
        ListDataPointByGroup =
            OptionBuilder
                .withDescription("list data point by group")
                .withArgName("project_id> <group_id")
                .hasOptionalArgs(2)
                .create("ListDataPointByGroup");

        RawLogQuery =
            OptionBuilder
                .withDescription("raw log query")
                .withArgName("maxdatapoints> <time_from> <time_to> <log_tag")
                .hasOptionalArgs(4)
                .create("RawLogQuery");
        RawLogExport =
            OptionBuilder
                .withDescription("raw log export")
                .withArgName("time_from> <time_to> <log_tag> <output")
                .hasOptionalArgs(4)
                .create("RawLogExport");

        AlertQuery =
            OptionBuilder
                .withDescription("alert query")
                .withArgName("project_id> <timeout> <last_seq")
                .hasOptionalArgs(3)
                .create("AlertQuery");

        RawDataQuery =
            OptionBuilder
                .withDescription("raw data query")
                .withArgName("maxdatapoints> <time_from> <time_to> <project_id> <gateway_id> <datapoint_id")
                .hasOptionalArgs(6)
                .create("RawDataQuery");
        StatsDataQuery =
            OptionBuilder
                .withDescription("stats data query")
                .withArgName("maxdatapoints> <time_from> <time_to> <project_id> <stats_id")
                .hasOptionalArgs(5)
                .create("StatsDataQuery");
        AlarmDataQuery =
            OptionBuilder
                .withDescription("alarm data query")
                .withArgName("maxdatapoints> <time_from> <time_to> <project_id> <tag")
                .hasOptionalArgs(5)
                .create("AlarmDataQuery");
        EventDataQuery =
            OptionBuilder
                .withDescription("event data query")
                .withArgName("maxdatapoints> <time_from> <time_to> <project_id> <tag")
                .hasOptionalArgs(5)
                .create("EventDataQuery");




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
        options.addOption(GetDevKey);

        options.addOption(AddUser);
        options.addOption(UserLogout);
        options.addOption(SetPass);
        options.addOption(UpdateUser);
        options.addOption(GetUsrInfo);

        options.addOption(AddGroup);
        options.addOption(UpdateGroup);
        options.addOption(GetGroupInfo);
        options.addOption(ListGroups);
        options.addOption(ListGroupPath);
        options.addOption(DeleteGroup);

        options.addOption(AddDataPoint);
        options.addOption(UpdateDataPoint);
        options.addOption(DeleteDataPoint);
        options.addOption(GetDataPointInfo);
        options.addOption(ListDataPoints);
        options.addOption(ListDataPointByGroup);

        options.addOption(RawLogQuery);
        options.addOption(RawLogExport);

        options.addOption(RawDataQuery);
        options.addOption(StatsDataQuery);
        options.addOption(AlarmDataQuery);
        options.addOption(EventDataQuery);
        formatter = new HelpFormatter();
    }

    private void helpPage() {
        formatter.setWidth(200);
        formatter.printHelp("Ecloud", options);
    }

    private void CodePaser(int retcode) {
        switch (retcode) {
            case 10001:
                System.out.println("no such method!");
                break;
            case 10002:
                System.out.println("invalid argument!");
                break;
            case 10003:
                System.out.println("unauthorized!");
                break;
            case 10004:
                System.out.println("device error!");
                break;
            case 10005:
                System.out.println("server error!");
                break;
            default:
                System.out.println("unknown error code!");
                break;
        }
    }

    /**
     * guide func
     **/

    private String guide(String output) {
        System.out.println("please enter " + output);
        String ret = "";
        try {
            ret = cons.readLine();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return ret;
    }

    private String getProjId() {
        System.out.println("your role is : " + role);
        return isSystemAdmin ? guide("project_id") : access;
    }

    private String getDeviceId() {
        return guide("device_id");
    }

    private String getDeviceCfg() {
        return guide("config");
    }

    private String getSrcAddr() {
        return guide("srcAddr");
    }

    private String getVersion() {
        return guide("version");
    }

    private String getCmd() {
        return guide("cmd");
    }

    private String getName() {
        return guide("app name");
    }

    private String getParams() {
        return guide("app params");
    }

    private String getAppPath() {
        return guide("app path");
    }

    private String getSrc() {
        return guide("file src");
    }

    private String getDest() {
        return guide("file dest");
    }


    /**
     * Device management
     **/

    private int GetVer(String argsArray[]) {
        try {
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int GetCfg(String argsArray[]) {
        try {
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
                String cfg = jobj.getString("config");
                System.out.println("---------------------------------------");
                System.out.println("config : " + cfg);
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int SetCfg(String argsArray[]) {
        try {
            String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
            JsonObject jobj = Json.createObjectBuilder()
                .add("device", device)
                .add("config", argsArray[2])
                .build();
            String retStr = dc.SetCfg(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                String version = jobj.getString("cfg");
                System.out.println("---------------------------------------");
                System.out.println("Success.");
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int Ping(String argsArray[]) {
        try {
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int UpdateFirmware(String argsArray[]) {
        try {
            String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
            JsonObject jobj = Json.createObjectBuilder()
                .add("device", device)
                .add("srcaddr", argsArray[2])
                .add("version", argsArray[3])
                .build();
            String retStr = dc.UpdateFirmware(jobj.toString());
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int System(String argsArray[]) {
        try {
            String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
            JsonObject jobj = Json.createObjectBuilder()
                .add("device", device)
                .add("cmd", argsArray[2])
                .build();
            String retStr = dc.System(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println(jobj.getString("data"));
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int GetAppList(String argsArray[]) {
        try {
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
                Iterator<JsonValue> iter = apps.iterator();
                System.out.println("---------------------------------------");
                while (iter.hasNext()) {
                    System.out.println(iter.next().toString());
                }
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int StartApp(String argsArray[]) {
        try {
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
                System.out.println(jobj.getString("data"));
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int StopApp(String argsArray[]) {
        try {
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
                System.out.println(jobj.getString("data"));
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int InstallApp(String argsArray[]) {
        try {
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
                System.out.println(jobj.getString("data"));
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int Reboot(String argsArray[]) {
        try {
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int Filec2d(String argsArray[]) {
        try {
            String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
            JsonObject jobj = Json.createObjectBuilder()
                .add("device", device)
                .add("src", argsArray[2])
                .add("dest", argsArray[3])
                .build();
            String retStr = dc.Filec2d(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                String version = jobj.getString("cfg");
                System.out.println("---------------------------------------");
                System.out.println("Success.");
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int Filed2c(String argsArray[]) {
        try {
            String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
            JsonObject jobj = Json.createObjectBuilder()
                .add("device", device)
                .add("src", argsArray[2])
                .add("dest", argsArray[3])
                .build();
            String retStr = dc.Filed2c(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                String version = jobj.getString("cfg");
                System.out.println("---------------------------------------");
                System.out.println("Success.");
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int RpcCall(String argsArray[]) {
        try {
            String device = "/dev/ctl/" + "/" + argsArray[0] + "/" + argsArray[1];
            JsonObject jobj = Json.createObjectBuilder()
                .add("device", device)
                .add("cmd", argsArray[2])
                .build();
            String retStr = dc.RpcCall(jobj.toString());
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    /**
     * project management
     **/

    private int AddProject(String argsArray[]) {
        try {
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
                String proj_id = jobj.getString("project_id");
                System.out.println("---------------------------------------");
                System.out.println("added project_id : " + proj_id);
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int GetProjectInfo(String projectId) {
        try {
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int UpdateProject(String argsArray[]) {
        try {
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
                GetProjectInfo(argsArray[0]);
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int DeleteProject(String projectId) {
        try {
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int ListProjectUser(String projectId) {
        try {
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
                Iterator<JsonValue> iter = users.iterator();
                System.out.println("---------------------------------------");
                while (iter.hasNext()) {
                    System.out.println(iter.next().toString());
                }
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int ListDevInProject(String argsArray[]) {
        try {
            String filter = "*";
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
                Iterator<JsonValue> iter = gateways.iterator();
                System.out.println("---------------------------------------");
                while (iter.hasNext()) {
                    System.out.println(iter.next().toString());
                }
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int ListDevByGroup(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("group_id", argsArray[1])
                .build();
            String retStr = dc.ListDevByGroup(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                JsonArray gateways = jobj.getJsonArray("gateways");
                Iterator<JsonValue> iter = gateways.iterator();
                System.out.println("---------------------------------------");
                while (iter.hasNext()) {
                    System.out.println(iter.next().toString());
                }
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int GetProjectStats(String projectId) {
        try {
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    /**
     * user management
     **/

    private int AddUser(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("username", argsArray[0])
                .add("role", argsArray[1])
                .build();
            String retStr = dc.AddUser(jobj.toString());
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int UserLogout() {
        try {
            String retStr = dc.UserLogout();
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            JsonObject jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println("Success.");
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int SetPass(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("username", argsArray[0])
                .add("password", argsArray[1])
                .build();
            String retStr = dc.SetPass(jobj.toString());
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int UpdateUser(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("username", argsArray[0])
                .add("role", argsArray[1])
                .add("active", argsArray[2])
                .build();
            String retStr = dc.UpdateUser(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                GetUsrInfo(argsArray[0]);
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int GetUsrInfo(String username) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("username", username)
                .build();
            String retStr = dc.GetUsrInfo(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println(jobj.toString());
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }
    /**
     * group management
     **/

    private int AddGroup(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("name", argsArray[1])
                .add("type", argsArray[2])
                .add("descriptions", argsArray[3])
                .add("userdata", argsArray[4])
                .add("tags", argsArray[5])
                .add("parentgroupid", argsArray[6])
                .build();
            String retStr = dc.AddGroup(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                String group_id = jobj.getString("group_id");
                System.out.println("---------------------------------------");
                System.out.println("added group_id : " + group_id);
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int UpdateGroup(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("group_id", argsArray[1])
                .add("name", argsArray[2])
                .add("type", argsArray[3])
                .add("descriptions", argsArray[4])
                .add("userdata", argsArray[5])
                .add("tags", argsArray[6])
                .add("parentgroupid", argsArray[7])
                .build();
            String retStr = dc.UpdateGroup(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                GetGroupInfo(argsArray);
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int GetGroupInfo(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("group_id", argsArray[1])
                .build();
            String retStr = dc.GetGroupInfo(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println(jobj.toString());
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int ListGroups(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("group_id", argsArray[1])
                .add("type", argsArray[2])
                .build();
            String retStr = dc.ListGroups(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                JsonArray projects = jobj.getJsonArray("groups");
                Iterator<JsonValue> iter = projects.iterator();
                System.out.println("---------------------------------------");
                while (iter.hasNext()) {
                    System.out.println(iter.next().toString());
                }
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int ListGroupPath(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("group_id", argsArray[1])
                .build();
            String retStr = dc.ListGroupPath(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                JsonArray projects = jobj.getJsonArray("path");
                Iterator<JsonValue> iter = projects.iterator();
                System.out.println("---------------------------------------");
                while (iter.hasNext()) {
                    System.out.println(iter.next().toString());
                }
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int DeleteGroup(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("group_id", argsArray[1])
                .build();
            String retStr = dc.AddGroup(jobj.toString());
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }
    /**
     * gateway management
     **/

    private int AddDev(String argsArray[]) {
        try {
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
                String gateway_id = jobj.getString("gateway_id");
                System.out.println("---------------------------------------");
                System.out.println("added gateway_id : " + gateway_id);
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int UpdateDev(String argsArray[]) {
        try {
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int GetDevInfo(String argsArray[]) {
        try {
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int DeleteDev(String argsArray[]) {
        try {
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int GetDevKey(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("gateway_id", argsArray[1])
                .build();
            String retStr = dc.GetDevKey(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                String devkey = jobj.getString("devkey");
                System.out.println("---------------------------------------");
                System.out.println("device's key: " + devkey);
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }
    /**
     * data point management
     **/

    private int AddDataPoint(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("gateway_id", argsArray[1])
                .add("name", argsArray[2])
                .add("datatype", argsArray[3])
                .add("dataunit", argsArray[4])
                .add("description", argsArray[5])
                .add("tag", argsArray[6])
                .add("userdata", argsArray[7])
                .add("groupid", argsArray[8])
                .build();
            String retStr = dc.AddDataPoint(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                String datapoint_id = jobj.getString("datapoint_id");
                System.out.println("---------------------------------------");
                System.out.println("added datapoint_id : " + datapoint_id);
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int UpdateDataPoint(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("gateway_id", argsArray[1])
                .add("datapoint_id", argsArray[2])
                .add("name", argsArray[3])
                .add("datatype", argsArray[4])
                .add("dataunit", argsArray[5])
                .add("description", argsArray[6])
                .add("tag", argsArray[7])
                .add("userdata", argsArray[8])
                .add("groupid", argsArray[9])
                .build();
            String retStr = dc.UpdateDataPoint(jobj.toString());
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int DeleteDataPoint(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("gateway_id", argsArray[1])
                .add("datapoint_id", argsArray[2])
                .build();
            String retStr = dc.DeleteDataPoint(jobj.toString());
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
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int GetDataPointInfo(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("gateway_id", argsArray[1])
                .add("datapoint_id", argsArray[2])
                .build();
            String retStr = dc.GetDataPointInfo(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println(jobj.toString());
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int ListDataPoints(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("gateway_id", argsArray[1])
                .build();
            String retStr = dc.ListDataPoints(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                JsonArray projects = jobj.getJsonArray("datapoints");
                Iterator<JsonValue> iter = projects.iterator();
                System.out.println("---------------------------------------");
                while (iter.hasNext()) {
                    System.out.println(iter.next().toString());
                }
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int ListDataPointByGroup(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("project_id", argsArray[0])
                .add("group_id", argsArray[1])
                .build();
            String retStr = dc.ListDataPointByGroup(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                JsonArray projects = jobj.getJsonArray("datapoints");
                Iterator<JsonValue> iter = projects.iterator();
                System.out.println("---------------------------------------");
                while (iter.hasNext()) {
                    System.out.println(iter.next().toString());
                }
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }
    /**
     * log management
     **/

    private int RawLogQuery(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("maxdatapoints", argsArray[0])
                .add("time_from", argsArray[1])
                .add("time_to", argsArray[2])
                .add("log_tag", argsArray[3])
                .build();
            String retStr = dc.RawLogQuery(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println(jobj.toString());
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int RawLogExport(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("time_from", argsArray[0])
                .add("time_to", argsArray[1])
                .add("log_tag", argsArray[2])
                .add("output", argsArray[3])
                .build();
            String retStr = dc.RawLogExport(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println("URL : " + jobj.getString("data"));
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    /**
     * data management
     **/

    private int RawDataQuery(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("maxdatapoints", argsArray[0])
                .add("time_from", argsArray[1])
                .add("time_to", argsArray[2])
                .add("project_id", argsArray[3])
                .add("gateway_id", argsArray[4])
                .add("datapoint_id", argsArray[5])
                .build();
            String retStr = dc.RawDataQuery(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println(jobj.toString());
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int StatsDataQuery(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("maxdatapoints", argsArray[0])
                .add("time_from", argsArray[1])
                .add("time_to", argsArray[2])
                .add("project_id", argsArray[3])
                .add("stats_id", argsArray[4])
                .build();
            String retStr = dc.StatsDataQuery(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println(jobj.toString());
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int AlarmDataQuery(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("maxdatapoints", argsArray[0])
                .add("time_from", argsArray[1])
                .add("time_to", argsArray[2])
                .add("project_id", argsArray[3])
                .add("tag", argsArray[4])
                .build();
            String retStr = dc.AlarmDataQuery(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println(jobj.toString());
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }

    private int EventDataQuery(String argsArray[]) {
        try {
            JsonObject jobj = Json.createObjectBuilder()
                .add("maxdatapoints", argsArray[0])
                .add("time_from", argsArray[1])
                .add("time_to", argsArray[2])
                .add("project_id", argsArray[3])
                .add("tag", argsArray[4])
                .build();
            String retStr = dc.EventDataQuery(jobj.toString());
            ByteArrayInputStream in = new ByteArrayInputStream(retStr.getBytes());
            JsonReader jsonReader = Json.createReader(in);
            jobj = jsonReader.readObject();
            int retcode = jobj.getInt("retcode", -1);
            if (retcode == 0) {
                System.out.println("---------------------------------------");
                System.out.println(jobj.toString());
                System.out.println("---------------------------------------");
                System.out.println();
            }
            else {
                CodePaser(retcode);
            }
        } catch (JsonException e) {
            System.err.println(e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("lack of args.");
        }
        return 1;
    }
}
