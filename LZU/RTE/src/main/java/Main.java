import rte.IRuntimeEnvironment;
import rte.RuntimeEnvironmentProxy;
import userInterfaces.console.CommandlineInterpreter;

public class Main {

    public static void main(String[] args){
        IRuntimeEnvironment iRte = new RuntimeEnvironmentProxy();
        new CommandlineInterpreter(iRte)
                .interpret("rte -start")
                .interpret("rte -deploy Time")
                .interpret("rte -deploy Time")
                .listen();
    }




}
