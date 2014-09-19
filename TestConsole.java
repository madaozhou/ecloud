import java.io.Console;
import java.io.PrintWriter;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.builder.*;
import org.apache.commons.cli2.commandline.*;

public class TestConsole {
    public static void main(String[] args) throws Exception{
        TestConsole cli = new TestConsole();
        Console cons = System.console();
        if (cons != null) {
            cons.printf("Hi, welcome to Ecloud!\n");
            PrintWriter writer = cons.writer();
            while (true) {
                writer.write("Ecloud ");
                cons.flush();
                String str1 = cons.readLine();
                String[] inputArgs = str1.split(" ");
                if (cli.engine(inputArgs) == 0)
                    break;
            }
        }
    }

    public int engine(String[] args) throws Exception{
        try {
            cl = parser.parse(args);
            if (cl.hasOption(help)) {
                System.out.println("help information");
                return 1;
            }
            if (cl.hasOption(quit)) {
                System.out.println("bye");
                return 0;
            }
            if (cl.hasOption(GetVer)) {
                String jsonStr = (String)cl.getValue(GetVer);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(GetCfg)) {
                String jsonStr = (String)cl.getValue(GetCfg);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(SetCfg)) {
                String jsonStr = (String)cl.getValue(SetCfg);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(Ping)) {
                String jsonStr = (String)cl.getValue(Ping);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(UpdateFirmware)) {
                String jsonStr = (String)cl.getValue(UpdateFirmware);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(system)) {
                String jsonStr = (String)cl.getValue(system);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(GetAppList)) {
                String jsonStr = (String)cl.getValue(GetAppList);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(StartApp)) {
                String jsonStr = (String)cl.getValue(StartApp);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(StopApp)) {
                String jsonStr = (String)cl.getValue(StopApp);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(InstallApp)) {
                String jsonStr = (String)cl.getValue(InstallApp);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(Reboot)) {
                String jsonStr = (String)cl.getValue(Reboot);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(Filec2d)) {
                String jsonStr = (String)cl.getValue(Filec2d);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(Filed2c)) {
                String jsonStr = (String)cl.getValue(Filed2c);
                dc.GetVer(jsonStr);
                return 1;
            }
            if (cl.hasOption(RpcCall)) {
                String jsonStr = (String)cl.getValue(RpcCall);
                dc.GetVer(jsonStr);
                return 1;
            }
        } catch (OptionException e) {
            e.printStackTrace();
        }
        System.out.println("Unknown cmd");
        return 1;
    }

    public TestConsole() {
        oBuilder = new DefaultOptionBuilder();
        aBuilder = new ArgumentBuilder();
        gBuilder = new GroupBuilder();
        dc = new DeviceControl();
        help =
            oBuilder
                .withShortName("help")
                .withShortName("h")
                .withDescription("print help information")
                .create();
        quit =
            oBuilder
                .withShortName("quit")
                .withShortName("q")
                .withDescription("quit cli")
                .create();
        GetVer =
            oBuilder
                .withShortName("GetVer")
                .withDescription("get version of gateway")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        GetCfg =
            oBuilder
                .withShortName("GetCfg")
                .withDescription("get configuration of gateway")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        SetCfg =
            oBuilder
                .withShortName("SetCfg")
                .withDescription("configure gateway")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        Ping =
            oBuilder
                .withShortName("Ping")
                .withDescription("detect device's status")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        UpdateFirmware =
            oBuilder
                .withShortName("UpdateFirmware")
                .withDescription("update firmware of gateway")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        system =
            oBuilder
                .withShortName("system")
                .withDescription("run system cmd")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        GetAppList =
            oBuilder
                .withShortName("GetAppList")
                .withDescription("get application list")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        StartApp =
            oBuilder
                .withShortName("StartApp")
                .withDescription("start application")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        StopApp =
            oBuilder
                .withShortName("StopApp")
                .withDescription("stop application")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        InstallApp =
            oBuilder
                .withShortName("InstallApp")
                .withDescription("install application")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        Reboot =
            oBuilder
                .withShortName("Reboot")
                .withDescription("reboot device")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        Filec2d =
            oBuilder
                .withShortName("Filec2d")
                .withDescription("tansfer file from cloud to device")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        Filed2c =
            oBuilder
                .withShortName("Filed2c")
                .withDescription("tansfer file from device to cloud")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        RpcCall =
            oBuilder
                .withShortName("RpcCall")
                .withDescription("call device's RPC")
                .withArgument(
                        aBuilder
                            .withName("jsonStr")
                            .withMinimum(1)
                            .withMaximum(1)
                            .create())
                .create();
        options =
            gBuilder
                .withName("options")
                .withOption(help)
                .withOption(quit)
                .withOption(GetVer)
                .withOption(GetCfg)
                .withOption(SetCfg)
                .withOption(Ping)
                .withOption(UpdateFirmware)
                .withOption(system)
                .withOption(GetAppList)
                .withOption(StartApp)
                .withOption(StopApp)
                .withOption(InstallApp)
                .withOption(Reboot)
                .withOption(Filec2d)
                .withOption(Filed2c)
                .withOption(RpcCall)
                .create();
        parser = new Parser();
        parser.setGroup(options);
    }

    private DefaultOptionBuilder oBuilder;
    private ArgumentBuilder aBuilder;
    private GroupBuilder gBuilder;
    private Group options;
    private Parser parser;
    private CommandLine cl;
    private DeviceControl dc;
    private Option help;
    private Option quit;
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
}
