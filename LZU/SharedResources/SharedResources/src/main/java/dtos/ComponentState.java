package dtos;

/**
 * DTO to wrap the current Component state.
 */
public class ComponentState {
    public boolean isRunning = false;

    public ComponentState setIsRunning(boolean isRunning){
        this.isRunning = isRunning; 
        return this;
    }
}
