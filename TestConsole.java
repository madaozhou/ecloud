import java.io.Console;
import java.io.PrintWriter;
import org.apache.commons.cli2.*;

public class TestConsole {
    public static void main(String[] args) throws Exception{
        Console cons = System.console();
        String json_str;
        json_str = "{\"device\":\"/dev/ctl/demo01/gw01\",\"auth_token\":\"test\"}";
        DeviceControl devicecontrol = new DeviceControl();
        if (cons != null) {
            cons.printf("Hi, welcome to Ecloud!\n");
            PrintWriter writer = cons.writer();
            writer.write("Ecloud> ");
            cons.flush();
            String str1 = cons.readLine();
            if (str1.equals("GetVer")) {
                devicecontrol.GetVer(json_str);
            }
        }
    }
    public TestConsole() {
        oBuilder = new DefaultOptionBuilder();
        aBuilder = new ArgumentBuilder();
        gBuilder = new GroupBuilder();
    }
    private DefaultOptionBuilder oBuilder;
    private ArgumentBuilder aBuilder;
    private GroupBuilder gBuilder;
}
