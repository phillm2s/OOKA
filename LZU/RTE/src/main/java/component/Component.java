package component;

import dtos.ComponentState;
import exceptions.AlreadyRunningException;
import exceptions.ComponentDelegateException;
import publishSubscribeServer.IPublishSubscriberServer;
import publishSubscribeServer.IComponentObserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.HashMap;


class Component implements IComponent{
    private HashMap<String, Class> classes= new HashMap<>();

    private Method instantiate;
    private Method startMethod;
    private Method stopMethod;
    private Method closeMethod;
    private Method subscribeMethod;
    private Method getStateMethod;

    private String name;
    private String id;
    private boolean started = false;



    Component (String name, String id){
        this.name = name;
        this.id=id;
    }

    public void addClass(Class c){
        classes.put(c.getName(), c);
    }

    @Override
    public void instantiate(String id) {
        try {
            instantiate.invoke(null,id);
        } catch (IllegalAccessException |InvocationTargetException e) {
            throw new ComponentDelegateException();
        }
    }

    public void startAsync() throws AlreadyRunningException {
        new Thread(() -> {
            started=true;
            try {
                System.out.println("thread started.");
                startMethod.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } finally {
                started=false;
                System.out.println("thread finished.");
            }
        }).start();
    }

    @Override
    public void stop() {
        try {
            stopMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentDelegateException();
        }
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

}