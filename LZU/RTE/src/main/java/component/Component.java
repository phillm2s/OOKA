package component;

import dtos.ComponentState;
import exceptions.AlreadyRunningException;
import exceptions.ComponentDelegateException;
import publishSubscribeServer.IPublishSubscriberServer;
import publishSubscribeServer.events.Event;
import publishSubscribeServer.IComponentObserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


public class Component implements Runnable {
    public static final String JAR_DIRECTORY = "..\\componentJars";

    private HashMap<String, Class> classes= new HashMap<>();

    private ArrayList<IComponentObserver> iComponentObservers = new ArrayList<>();

    private Method startMethod;
    private Method stopMethod;
    private Method subscribeMethod;
    private Method getStateMethod;

    private String name;

    private boolean isRunning = false;

    public Component (String name){
        this.name = name;
    }


    public void addClass(Class c){
        classes.put(c.getName(), c);
    }

    public void setStartMethod(Method start){
        this.startMethod = start;
    }

    public void start() throws AlreadyRunningException {
        if (!isRunning) {
            new Thread(this).start();
            isRunning = true;
        }
        else
            throw new AlreadyRunningException();
    }

    public void stop() throws InvocationTargetException, IllegalAccessException {
        if (isRunning) {
            stopMethod.invoke(null);
            isRunning = false;
            int i=0;
        }
    }

    public ComponentState getState() throws ComponentDelegateException {
        try{
            return (ComponentState) getStateMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentDelegateException();
        }
    }

    public void setStopMethod(Method stop){
        this.stopMethod = stop;
    }

    public void setSubscribeMethod(Method subscribe) {
        this.subscribeMethod = subscribe;
    }

    public void setGetStateMethod(Method getState){
        this.getStateMethod = getState;
    }

    public void subscribe(IPublishSubscriberServer iPublishSubscriberServer) throws ComponentDelegateException {
        try {
            subscribeMethod.invoke(null,iPublishSubscriberServer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentDelegateException("Subscription failed");
        }
    }

    public boolean isSubscribable() {
        if (this.subscribeMethod!=null)
            return true;
        return false;
    }

    public String getName() {
        return name;
    }


    @Override
    public void run() {
        try {
            startMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}