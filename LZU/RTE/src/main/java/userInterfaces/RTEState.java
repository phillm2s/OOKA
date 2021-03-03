package userInterfaces;
/**
 * DTO to wrap the state of the Runtime.
 */
public class RTEState {
    public boolean running =false;

    public RTEState setRunning(Boolean running){
        this.running = running;
        return this;
    }


}
