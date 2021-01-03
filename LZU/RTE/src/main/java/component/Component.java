package component;

import exceptions.AlreadyRunningException;
import exceptions.ComponentDelegateException;
import observer.Event;
import observer.IComponentObserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


public class Component implements Runnable, IComponentObserver {
    public static final String JAR_DIRECTORY = "..\\componentJars";

    private Thread thread;

    private HashMap<String, Class> classes= new HashMap<>();

    private ArrayList<IComponentObserver> iComponentObservers = new ArrayList<>();

    private Method startMethod;
    private Method stopMethod;
    private Method subscribeMethod;

    private String name;

    private boolean isRunning = false;

    public Component (String name){
        this.name = name;
        thread = new Thread(this);
    }


    public void addClass(Class c){
        classes.put(c.getName(), c);
    }

    public void setStartMethod(Method start){
        this.startMethod = start;
    }

    public void start() throws AlreadyRunningException {
        if (!isRunning) {
            thread.start();
            isRunning = true;
        }
        else
            throw new AlreadyRunningException();
    }

    public void stop() throws InvocationTargetException, IllegalAccessException {
        if (isRunning) {
            stopMethod.invoke(null);
            isRunning = false;
        }
    }

    public void setStopMethod(Method stop){
        this.stopMethod = stop;
    }

    public void setSubscribeMethod(Method subscribe) {
        this.subscribeMethod = subscribe;
    }

    public void subscribe(IComponentObserver observer) throws ComponentDelegateException {
        if(!isSubscribable())
            throw new ComponentDelegateException("Component not subscibable.");
        try {
            subscribeMethod.invoke(null,"hi");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ComponentDelegateException();
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
            if(isSubscribable())
                subscribe((IComponentObserver)this);
            startMethod.invoke(null);
        } catch (IllegalAccessException | ComponentDelegateException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notify(Event e) {
        System.out.println("blub");
    }
}