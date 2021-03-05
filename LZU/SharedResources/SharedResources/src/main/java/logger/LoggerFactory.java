package logger;

import java.util.ArrayList;

public abstract class LoggerFactory {
//    public static ArrayList<LogReceivedHandler> logReceivedHandlers = new ArrayList<>();

//    public static void addLogReceivedHandler(LogReceivedHandler eventHandler){
//        logReceivedHandlers.add(eventHandler);
//    }
//
//    public static void removeLogReceivedHandler(LogReceivedHandler eventHandler){
//        logReceivedHandlers.remove(eventHandler);
//    }
//
//    public static void removeAllLogReceivedHandler(){
//        logReceivedHandlers.clear();
//    }

    public static Logger createLogger(){
        return new Logger();
//        Logger logger = new Logger();
//        for(LogReceivedHandler event : logReceivedHandlers)
//            logger.addLogReceivedHandler(event);
//        return logger;
    }

}
