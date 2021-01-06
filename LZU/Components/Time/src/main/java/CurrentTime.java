import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CurrentTime {
    private static CommandLineInterface cli = CommandLineInterface.getInstance();
    private boolean running = false;

    public void run() { //blocking
        running =true;
        while(running) {
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
            cli.print("Test annotations.Component running. "+ timeStamp);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        running = false;
    }

    public boolean isRunning(){
        return running;
    }
}
