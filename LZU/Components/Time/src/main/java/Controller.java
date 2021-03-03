
import annotations.*;
import dtos.ComponentState;
import publishSubscribeServer.IComponentObserver;
import publishSubscribeServer.IPublishSubscriberServer;
import publishSubscribeServer.ITopic;
import publishSubscribeServer.events.Event;
import publishSubscribeServer.events.StartEvent;
import publishSubscribeServer.events.StopEvent;

public class Controller implements ICommandLineInterpreter, IComponentObserver {
    private static Controller instance = null;
    private static CommandLineInterface cli = CommandLineInterface.getInstance();
    private static IPublishSubscriberServer iPublishSubscriberServer;

    private CurrentTime currentTime;


    private Controller(){
        cli.setTitle("Time Component");
        cli.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Controller.stop();
            }
        });

        currentTime= new CurrentTime();
    }

    public static Controller getInstance(){
        if(instance==null)
            instance = new Controller();
        return instance;
    }

    @Start
    public static void start() {
        if (!getInstance().currentTime.isRunning()) {
            if (iPublishSubscriberServer != null) {
                //This components also want to notify all "stateChange" subscribers that its state changes to "started" in this moment
                iPublishSubscriberServer.createTopic("stateChange").notify(new StartEvent().setMessage("Component started."));
            }
            instance.currentTime.run();
        }
        else {
            cli.print("Component already running.");
        }
    }

    @Stop
    public static void stop(){
        if (getInstance().currentTime.isRunning()) {
            cli.print("stop component...");
            if (iPublishSubscriberServer != null)
                iPublishSubscriberServer.createTopic("stateChange").notify(new StopEvent().setMessage("Component stopped."));
            getInstance().currentTime.stop();
        } else
            cli.print("Component not started.");
    }

    @Close
    public static void close(){
        stop();
        cli.close();
    }


    @Subscribe
    public static void setPublishSubscribeServer(IPublishSubscriberServer iServer){
        iPublishSubscriberServer = iServer;
        //this component wants to know if other components changes there state
        //create stateChange topic IF NOT EXIST and subscribe
        ITopic topic = iPublishSubscriberServer.createTopic("stateChange");
        topic.subscribe((IComponentObserver) getInstance());

        cli.print("Subscribe topic: "+topic.getName());
    }

    @State
    public static ComponentState getState(){
        return new ComponentState().setIsRunning(instance.currentTime.isRunning());
    }

    @Override
    public void handleCommand(String input) {

    }

    @Override
    public void notify(Event e) {
        cli.print("Received notification: "+ e.getMessage());
    }

}
