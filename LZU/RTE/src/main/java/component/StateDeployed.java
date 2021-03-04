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
                System.out.println("thread started.");
                component.startMethod.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println("thread finished.");
            }
        }).start();
    }

    @Override
    public void stop() {
        throw new NotRunningException();
    }

}
