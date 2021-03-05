import logger.ILogger;
import logger.LogReceivedHandler;
import logger.LoggerFactory;

public class Calculator {

    private boolean enabled=false;
    private ILogger logger;

    public Calculator(){
        logger = LoggerFactory.createLogger().addLogReceivedHandler(new LogReceivedHandler() {
            @Override
            public void logReceivedEvent(String log) {
                Controller.cli().print(log);
            }
        });
    }

    public double sum(double a, double b){
        logger.sendLog("Calculate sum");
        return a+b;
    }

    public double sub(double a, double b){
        logger.sendLog("Calculate sub");
        return a-b;
    }

    public double mul(double a, double b){
        logger.sendLog("Calculate mul");
        return a*b;
    }

    public double div(double a, double b){
        logger.sendLog("Calculate div");
        return a/b;
    }

    public boolean isRunning(){
        return enabled;
    }

    public void enable(){
        enabled=true;
    }

    public void disable(){
        enabled=false;
    }
}
