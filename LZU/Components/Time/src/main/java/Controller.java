
import annotations.*;
import dtos.ComponentState;
import publishSubscribeServer.IComponentObserver;
import publishSubscribeServer.IPublishSubscriberServer;
import publishSubscribeServer.ITopic;
import publishSubscribeServer.events.*;

public class Controller implements ICommandLineInterpreter, IComponentObserver {
    private static Controller instance = null;
    private static CommandLineInterface cli = CommandLineInterface.getInstance();
    private static IPublishSubscriberServer iPublishSubscriberServer;

    private CurrentTime currentTime;
    private String componentID;


    private Controller(String componentID){
        this.componentID = componentID;
        cli.setTitle("Time Component::"+componentID);
        cli.subscribe(this);
        cli.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Controller.close();
                cli.dispose();
            }
        });
        currentTime= new CurrentTime();
    }

    @Instantiate
    public static void instantiate(String componentID){
        instance= new Controller(componentID);
        if (iPublishSubscriberServer != null)
            iPublishSubscriberServer.createTopic("stateChange")
                    .notify(new InstantiateEvent().setMessage("Component"+ instance.componentID+" instantiated."));
    }

    @Start
    public static void start() {
        if (!instance.currentTime.isRunning()) {
            if (iPublishSubscriberServer != null)
                iPublishSubscriberServer.createTopic("stateChange")
                        .notify(new StartEvent()
                                .setMessage("Component"+ instance.componentID+" started."));
            instance.currentTime.runAsync();
            cli.print("Component started");
        }
        else {
            cli.print("Component already running.");
        }
    }

    @Stop
    public static void stop(){
        if(instance.currentTime.isRunning()) {
            if (iPublishSubscriberServer != null)
                iPublishSubscriberServer.createTopic("stateChange")
                        .notify(new StopEvent().setMessage("Component" + instance.componentID + " stopped."));

            instance.currentTime.stop();
            cli.print("Component stopped");
        }
    }

    @Close
    public static void close(){
        stop();
        if (iPublishSubscriberServer != null)
            iPublishSubscriberServer.createTopic("stateChange")
                    .notify(new CloseEvent().setMessage("Component"+ instance.componentID+" closed."));
        cli.close();
    }

    @Subscribe
    public static void setPublishSubscribeServer(IPublishSubscriberServer iServer){
        iPublishSubscriberServer = iServer;
        //this component wants to know if other components changes there state
        //create stateChange topic IF NOT EXIST and subscribe
        ITopic topic = iPublishSubscriberServer.createTopic("stateChange");
        topic.subscribe((IComponentObserver) instance);

        cli.print("Subscribe topic: "+topic.getName());
    }

    @Log
    public static void setLogger(ILogger iLogger){

    }

    @State
    public static ComponentState getState(){
        return new ComponentState()
                .setIsRunning(instance.currentTime.isRunning())
                .setComponentName("Time")
                .setComponentID(instance.componentID);
    }



    @Override
    public void handleCommand(String input) {

    }

    @Override
    public void notify(Event e) {
        cli.print("Received notification: "+ e.getMessage());
    }

}
