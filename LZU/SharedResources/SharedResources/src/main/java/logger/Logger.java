package logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public  class Logger implements ILogger {

    private ArrayList<LogReceivedHandler> logReceivedHandlers = new ArrayList<>();

    public Logger addLogReceivedHandler(LogReceivedHandler eventHandler){
        this.logReceivedHandlers.add(eventHandler);
        return this;
    }

    public Logger removeLogReceivedHandler(LogReceivedHandler eventHandler){
        this.logReceivedHandlers.remove(eventHandler);
        return this;
    }

    public Logger removeAllLogReceivedHandler(){
        this.logReceivedHandlers.clear();
        return this;
    }

    @Override
    public void sendLog(String message) {
        String timeStamp = new SimpleDateFormat("dd:MM:yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
        String log= "++++ Runtime-Log: Meldung aus Component: "+message+" (["+timeStamp+"])";

        for(LogReceivedHandler handler : logReceivedHandlers)
            handler.logReceivedEvent(log);
    }
}
