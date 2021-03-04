package component;
import exceptions.AlreadyRunningException;
import exceptions.ComponentDelegateException;
import java.lang.reflect.InvocationTargetException;

public class StateRunning implements IComponentState {

    private Component component;

    public StateRunning(Component component){
        this.component = component;
    }

    @Override
    public void start() {
        throw new AlreadyRunningException();
    }

    @Override
    public void stop() {
        component.setCurrentState(new StateDeployed(component));
        try {
            component.stopMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentDelegateException();
        }
    }

}
