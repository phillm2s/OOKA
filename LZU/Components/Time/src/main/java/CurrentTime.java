import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CurrentTime {
    private EventWaitHandle waitHandle = new EventWaitHandle(true); //true means not running

    public void runAsync() {
        new Thread(() -> {
            waitHandle.reset();
            do {
                String timeStamp = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
                Controller.cli().print("Test annotations.Component running. "+ timeStamp);
            }while(!waitHandle.waitFor(2000));
        }).start();
    }

    public void stop(){
        waitHandle.set();
    }

    public boolean isRunning(){
        return !waitHandle.isSet();
    }
}
