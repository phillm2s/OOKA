package component;

import dtos.ComponentState;
import exceptions.AlreadyRunningException;
import exceptions.ComponentDelegateException;
import publishSubscribeServer.IPublishSubscriberServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;


public class Component implements IComponent{
    private HashMap<String, Class> classes= new HashMap<>();

    private Method instantiate;
    Method startMethod;
    Method stopMethod;
    private Method closeMethod;
    private Method subscribeMethod;
    private Method getStateMethod;

    private String name;
    private String id;
    private IComponentState currentState;



    Component (String name, String id){
        this.name = name;
        this.id=id;
        currentState = new StateDeployed(this);
    }

    @Override
    public void instantiate(String id) {
        try {
            instantiate.invoke(null,id);
        } catch (IllegalAccessException |InvocationTargetException e) {
            throw new ComponentDelegateException();
        }
    }

    @Override
    public void startAsync() throws AlreadyRunningException {
        currentState.start();
    }

    @Override
    public void stop() {
        currentState.stop();
    }

    @Override
    public void close(){
        try {
            closeMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentDelegateException();
        }
    }

    @Override
    public ComponentState getState() {
        try{
            return ((ComponentState) getStateMethod.invoke(null));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentDelegateException();
        }
    }

    @Override
    public void subscribe(IPublishSubscriberServer iPublishSubscriberServer) throws ComponentDelegateException {
        try {
            subscribeMethod.invoke(null,iPublishSubscriberServer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentDelegateException("Subscription failed");
        }
    }

    @Override
    public boolean isSubscribable() {
        if (this.subscribeMethod!=null)
            return true;
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getID() {
        return this.id;
    }


    public void addClass(Class c){
        classes.put(c.getName(), c);
    }

    public void setCurrentState(IComponentState currentState){
        this.currentState =currentState;
    }

    void setInstantiateMethod(Method instantiateMethod){
        this.instantiate=instantiateMethod;
    }

    void setStartMethod(Method start){
        this.startMethod = start;
    }

    void setStopMethod(Method stop){
        this.stopMethod = stop;
    }

    void setCloseMethod(Method close){
        this.closeMethod = close;
    }

    void setSubscribeMethod(Method subscribe) {
        this.subscribeMethod = subscribe;
    }

    void setGetStateMethod(Method getState){
        this.getStateMethod = getState;
    }


}