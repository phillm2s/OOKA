
import annotations.Component;
import annotations.Start;
import annotations.Stop;
import annotations.Subscribe;
import observer.Event;
import observer.IComponentObserver;

import java.util.ArrayList;

@Component
public class Controller implements ICommandLineInterpreter {
    private static Thread thread = null;
    private static Controller instance = null;
    private static CommandLineInterface cli = CommandLineInterface.getInstance();
    private static ArrayList<IComponentObserver> componentObserver = new ArrayList<>();

    private CurrentTime currentTime;


    private Controller(){
        cli.setTitle("Test annotations.Component");
        cli.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Controller.stop();
            }
        });

        CurrentTime currentTime= new CurrentTime();
    }

    @Start //Entry point
    public static void start(){
        if(instance==null) {
            instance = new Controller();
            instance.currentTime.run();
            notifiyAll(new Event().setMessage("Time annotations.Component started."));
        }
        else {
            cli.print("Test annotations.Component already running.");
        }
    }

    @Stop
    public static void stop(){
        cli.print("annotations.Stop annotations.Component");
        instance.currentTime.stop();
    }

    @Subscribe
    public static void subscribe(IComponentObserver observer){
        componentObserver.add(observer);
    }

    private static void notifiyAll(Event e){
        for ( IComponentObserver obser : componentObserver )
            obser.notify(e);
    }




    //region ============ commandLineInterpreter Interface ================
    @Override
    public void handleCommand(String input) {

    }

    //endregion
}
