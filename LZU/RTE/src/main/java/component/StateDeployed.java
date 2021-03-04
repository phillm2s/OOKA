package component;
import exceptions.NotRunningException;
import java.lang.reflect.InvocationTargetException;

public class StateDeployed implements IComponentState {

    private Component component;
    public StateDeployed(Component component){
        this.component = component;
    }


    @Override
    public void start() {
        component.setCurrentState(new StateRunning(component));
        new Thread(() -> {
            try {
                component.startMethod.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void stop() {
        throw new NotRunningException();
    }

}
