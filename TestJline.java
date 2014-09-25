import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

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
        if (cli.cons != null) {
            System.out.printf("Hi, welcome to Ecloud!\n");
            PrintWriter writer = new PrintWriter(cli.cons.getOutput());
            cli.cons.setPrompt("Ecloud ");
            while (true) {
                String str1 = cli.cons.readLine();
                System.out.println(str1);
                String[] inputArgs = str1.split(" ");
                if (cli.engine(inputArgs) == 0)
                    break;
            }
        }
    }

    public int engine(String[] args) throws Exception{
        parser = new BasicParser();
        try {
            System.out.println(args[0]);
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
           // if (cl.hasOption(GetVer)) {
           //     String jsonStr = (String)cl.getValue(GetVer);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(GetCfg)) {
           //     String jsonStr = (String)cl.getValue(GetCfg);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(SetCfg)) {
           //     String jsonStr = (String)cl.getValue(SetCfg);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(Ping)) {
           //     String jsonStr = (String)cl.getValue(Ping);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(UpdateFirmware)) {
           //     String jsonStr = (String)cl.getValue(UpdateFirmware);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(system)) {
           //     String jsonStr = (String)cl.getValue(system);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(GetAppList)) {
           //     String jsonStr = (String)cl.getValue(GetAppList);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(StartApp)) {
           //     String jsonStr = (String)cl.getValue(StartApp);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(StopApp)) {
           //     String jsonStr = (String)cl.getValue(StopApp);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(InstallApp)) {
           //     String jsonStr = (String)cl.getValue(InstallApp);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(Reboot)) {
           //     String jsonStr = (String)cl.getValue(Reboot);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(Filec2d)) {
           //     String jsonStr = (String)cl.getValue(Filec2d);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(Filed2c)) {
           //     String jsonStr = (String)cl.getValue(Filed2c);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(RpcCall)) {
           //     String jsonStr = (String)cl.getValue(RpcCall);
           //     dc.GetVer(jsonStr);
           //     return 1;
           // }
           // if (cl.hasOption(ListProjects)) {
           //     dc.ListProjects();
           //     return 1;
           // }
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
        }
        System.out.println("Unknown cmd");
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
                .withLongOpt("Login")
                .withDescription("Login into energy cloud system")
                .create();
        GetVer =
            OptionBuilder
                .withLongOpt("GetVer")
                .withDescription("get version of gateway")
                .withArgName("device")
                .hasArgs(1)
                .create();
        GetCfg =
            OptionBuilder
                .withLongOpt("GetCfg")
                .withDescription("get configuration of gateway")
                .withArgName("device")
                .hasArgs(1)
                .create();
        SetCfg =
            OptionBuilder
                .withLongOpt("SetCfg")
                .withDescription("configure gateway")
                .withArgName("device> <config")
                .hasArgs(2)
                .create();
        Ping =
            OptionBuilder
                .withLongOpt("Ping")
                .withDescription("detect device's status")
                .withArgName("device")
                .hasArgs(1)
                .create();
        UpdateFirmware =
            OptionBuilder
                .withLongOpt("UpdateFirmware")
                .withDescription("update firmware of gateway")
                .withArgName("device> <srcaddr> <version")
                .hasArgs(3)
                .create();
        system =
            OptionBuilder
                .withLongOpt("system")
                .withDescription("run system cmd")
                .withArgName("device> <cmd")
                .hasArgs(2)
                .create();
        GetAppList =
            OptionBuilder
                .withLongOpt("GetAppList")
                .withDescription("get application list")
                .withArgName("device")
                .hasArgs(1)
                .create();
        StartApp =
            OptionBuilder
                .withLongOpt("StartApp")
                .withDescription("start application")
                .withArgName("device> <name> <params")
                .hasArgs(3)
                .create();
        StopApp =
            OptionBuilder
                .withLongOpt("StopApp")
                .withDescription("stop application")
                .withArgName("device> <name")
                .hasArgs(2)
                .create();
        InstallApp =
            OptionBuilder
                .withLongOpt("InstallApp")
                .withDescription("install application")
                .withArgName("device> <apppath")
                .hasArgs(2)
                .create();
        Reboot =
            OptionBuilder
                .withLongOpt("Reboot")
                .withDescription("reboot device")
                .withArgName("device")
                .hasArgs(1)
                .create();
        Filec2d =
            OptionBuilder
                .withLongOpt("Filec2d")
                .withDescription("tansfer file from cloud to device")
                .withArgName("device> <src> <dest")
                .hasArgs(3)
                .create();
        Filed2c =
            OptionBuilder
                .withLongOpt("Filed2c")
                .withDescription("tansfer file from device to cloud")
                .withArgName("device> <src> <dest")
                .hasArgs(3)
                .create();
        RpcCall =
            OptionBuilder
                .withLongOpt("RpcCall")
                .withDescription("call device's RPC")
                .withArgName("device> <cmd")
                .hasArgs(2)
                .create();

        ListProjects =
            OptionBuilder
                .withLongOpt("ListProjects")
                .withDescription("list existed projects")
                .create();
        AddProjects =
            OptionBuilder
                .withLongOpt("AddProjects")
                .withDescription("add projects")
                .withArgName("name> <vendor> <info> <tag")
                .hasArgs(4)
                .create();
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
        options.addOption(AddProjects);
        formatter = new HelpFormatter();
    }

    private void helpPage() {
        formatter.printHelp("Ecloud", options);
    }

    private ConsoleReader cons;
    private Options options;
    private CommandLine cl;
    private BasicParser parser;
    private HelpFormatter formatter;
    private DeviceControl dc;

    private Option help;
    private Option quit;
    private Option Login;

    private Option ListProjects;
    private Option AddProjects;
    private Option UpdateProjects;
    private Option GetProjectInfo;
    private Option DeleteProject;
    private Option GetProjectStats;
    private Option ListProjectUsers;

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
    private HelpFormatter hf;
}
