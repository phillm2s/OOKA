import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CurrentTime {
    private static CommandLineInterface cli = CommandLineInterface.getInstance();
    private boolean stopPerformed = false;

    public void run() { //blocking
        stopPerformed =false;
        while(!stopPerformed) {
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
            cli.print("Test annotations.Component running. "+ timeStamp);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        stopPerformed = true;
    }

}
