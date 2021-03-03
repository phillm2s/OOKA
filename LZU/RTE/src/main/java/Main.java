import rte.IRuntimeEnvironment;
import rte.RuntimeEnvironmentProxy;
import userInterfaces.console.CommandlineInterpreter;
import userInterfaces.gui.RteGui;

public class Main {

    public static void main(String[] args){
        IRuntimeEnvironment iRte = new RuntimeEnvironmentProxy();
        new RteGui(iRte);
        new CommandlineInterpreter(iRte)
//                .interpret("rte -start")
//                .interpret("rte -deploy Time")
//                .interpret("rte -deploy Time")
                .listen();
    }




}
