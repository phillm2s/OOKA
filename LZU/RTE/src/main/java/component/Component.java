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


public class Component{
    public static final String JAR_DIRECTORY = "..\\componentJars";

    private HashMap<String, Class> classes= new HashMap<>();

    private ArrayList<IComponentObserver> iComponentObservers = new ArrayList<>();

    private Method startMethod;
    private Method stopMethod;
    private Method closeMethod;
    private Method subscribeMethod;
    private Method getStateMethod;

    private String name;
    //private boolean threadRunning=false;
    //private Thread thread;
    private boolean started = false;



    public Component (String name){
        this.name = name;
    }

    public void addClass(Class c){
        classes.put(c.getName(), c);
    }

    public void startAsync() throws AlreadyRunningException {
        if (!started) {
            new Thread(() -> {
                try {
                    System.out.println("thread started.");
                    startMethod.invoke(null);
                    System.out.println("thread finished.");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Exception e){

                }
            }).start();
            started=true;
        }
        else
            throw new AlreadyRunningException();
    }

    public void stop() throws InvocationTargetException, IllegalAccessException {
        if (started) {
            stopMethod.invoke(null);
            started=false;
        }
    }

    public void close() throws InvocationTargetException, IllegalAccessException {
        closeMethod.invoke(null);
    }

    public ComponentState getState() throws ComponentDelegateException {
        try{
            return (ComponentState) getStateMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentDelegateException();
        }
    }

    public void setStartMethod(Method start){
        this.startMethod = start;
    }

    public void setStopMethod(Method stop){
        this.stopMethod = stop;
    }

    public void setCloseMethod(Method close){
        this.closeMethod = close;
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

}