package dtos;

/**
 * DTO to wrap the current Component state.
 */
public class ComponentState {
    public String componentID;
    public String componentName;
    public boolean isRunning = false;

    public ComponentState setIsRunning(boolean isRunning){
        this.isRunning = isRunning; 
        return this;
    }

    public ComponentState setComponentID(String id){
        this.componentID=id;
        return this;
    }

    public ComponentState setComponentName(String name){
        this.componentName= name;
        return this;
    }

}
