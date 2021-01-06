package dtos;

public class ComponentState {   //dto
    public boolean isRunning = false;

    public ComponentState setIsRunning(boolean isRunning){
        this.isRunning = isRunning;
        return this;
    }
}
