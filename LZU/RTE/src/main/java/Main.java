import logger.LogReceivedHandler;
import logger.Logger;
import rte.IRuntimeEnvironment;
import rte.RuntimeEnvironmentProxy;
import rte.publishSubscribeServer.PublishSubscriberServer;
import userInterfaces.console.CommandlineInterpreter;
import userInterfaces.gui.RteGui;

public class Main {

    public static void main(String []args){
        IRuntimeEnvironment rte = new RuntimeEnvironmentProxy();
        new RteGui(rte);
        new CommandlineInterpreter( rte ).listen();
    }
}
