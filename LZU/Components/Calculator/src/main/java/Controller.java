import annotations.*;
import dtos.ComponentState;
import logger.ILogger;
import logger.LogReceivedHandler;
import logger.LoggerFactory;
import publishSubscribeServer.IComponentObserver;
import publishSubscribeServer.IPublishSubscriberServer;
import publishSubscribeServer.ITopic;
import publishSubscribeServer.events.*;

public class Controller implements ICommandLineInterpreter, IComponentObserver {
    private static Controller instance = null;
    private static CommandLineInterface cli= null;
    private static IPublishSubscriberServer iPublishSubscriberServer= null;
    private static ILogger iLogger= null;


    private String componentID;
    private String componentName= "Calculator";
    private Calculator calculator;

    private Controller(String componentID){
        this.componentID = componentID;
        cli = new CommandLineInterface();
        cli.setTitle(componentName+" Component::"+componentID);
        cli.subscribe(this);
        cli.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Controller.close();
                cli.dispose();
            }
        });
//        //all logger created with this factory will trigger this event.
//        LoggerFactory.addLogReceivedHandler(new LogReceivedHandler() {
//            @Override
//            public void logReceivedEvent(String log) {
//                cli.print(log);
//            }
//        });
        calculator= new Calculator();
    }

    public static IcommandLineInterface cli(){
        return cli;
    }

    @PostConstruct
    public static void postConstruct(String componentID) {

        instance = new Controller(componentID);
        if (iPublishSubscriberServer != null)
            iPublishSubscriberServer.createTopic("stateChange")
                    .notify(new InstantiateEvent().setMessage("Component" + instance.componentID + " instantiated."));
        if(iLogger!=null)
            iLogger.sendLog("Component instanzieiert");
    }

    @Start
    public static void start() {
        if (!instance.calculator.isRunning()) {
            if (iPublishSubscriberServer != null)
                iPublishSubscriberServer.createTopic("stateChange")
                        .notify(new StartEvent()
                                .setMessage("Component"+ instance.componentID+" started."));
            if(iLogger!=null)
                iLogger.sendLog("Prozess gestartet");

            instance.calculator.enable();
            cli.print("Component started");
        }
        else {
            cli.print("Component already running.");
        }
    }

    @Stop
    public static void stop(){
        if(instance.calculator.isRunning()) {
            if (iPublishSubscriberServer != null)
                iPublishSubscriberServer.createTopic("stateChange")
                        .notify(new StopEvent().setMessage("Component" + instance.componentID + " stopped."));
            if(iLogger!=null)
                iLogger.sendLog("Component gestoppt");

            instance.calculator.disable();
            cli.print("Component stopped");
        }
    }

    @PreDestroy
    public static void close(){
        stop();
        if (iPublishSubscriberServer != null)
            iPublishSubscriberServer.createTopic("stateChange")
                    .notify(new CloseEvent().setMessage("Component"+ instance.componentID+" closed."));
        if(iLogger!=null)
            iLogger.sendLog("Component beendet");
        cli.close();
    }

    @Subscribe
    public static void setPublishSubscribeServer(IPublishSubscriberServer iServer){
        iPublishSubscriberServer = iServer;
        //this component wants to know if other components changes there state
        //create stateChange topic IF NOT EXIST and subscribe
        ITopic topic = iPublishSubscriberServer.createTopic("stateChange");
        topic.subscribe((IComponentObserver) instance);

        if(iLogger!=null)
            iLogger.sendLog("PubsubServer injected");

        cli.print("Subscribe topic: "+topic.getName());
    }

    @Log
    public static void setLogger(ILogger iLogger){
        Controller.iLogger = iLogger;
        cli.print("logger injected.");
    }

    @State
    public static ComponentState getState(){
        if(iLogger!=null)
            iLogger.sendLog("Component Status requested");
        return new ComponentState()
                .setIsRunning(instance.calculator.isRunning())
                .setComponentName("Time")
                .setComponentID(instance.componentID);
    }


    @Override
    public void handleCommand(String input) {
        if (!calculator.isRunning()){
            cli.print("Calculator is not Running!");
            return;
        }
        if(!(input.contains("+")
                || input.contains("-")
                || input.contains("*")
                || input.contains("/")))
            return;

        String[] values=input.split("(\\+)|(-)|(\\*)|(/)");
        int v1 = Integer.parseInt(values[0]);
        int v2 = Integer.parseInt(values[1]);

        if(input.contains("+"))
            cli.print( "=" +calculator.sum(v1,v2) );
        else if(input.contains("-"))
            cli.print( "=" +calculator.sub(v1,v2) );
        else if(input.contains("*"))
            cli.print( "=" +calculator.mul(v1,v2) );
        else if(input.contains("/"))
            cli.print( "=" +calculator.div(v1,v2) );
    }

    @Override
    public void notify(Event e) {
        cli.print("Received notification: "+ e.getMessage());
    }

}
