package rte;

import component.Component;
import exceptions.*;
import userInterfaces.console.Command;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class RuntimeEnvironment implements IRuntimeEnvironment{
    private boolean running = false;

    private HashMap<String,Component> components = new HashMap<>();


    @Override
    public void rteStart() {
        running= true;
    }

    @Override
    public void rteStop() {
        for (Component component : components.values()) {
            try {
                component.stop();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        running=false;
    }

    @Override
    public boolean rteIsRunning() {
        return running;
    }

    @Override
    public String deployComponent(String path, String componentName) throws ComponentNotFoundException, UnderlyingComponentUnavailableException {
        if (!running)
            throw new UnderlyingComponentUnavailableException();

        String id = componentName;
        int i=0;
        while(components.containsKey(id))
            id = componentName + ++i;

        try {
            Component newComponent = new ReflectionClassLoader().loadComponentFromFilesystem(Component.JAR_DIRECTORY, componentName);
            components.put(id,newComponent);
            return id;
        } catch (IOException | ClassNotFoundException e) {
            throw new ComponentNotFoundException();
        }
    }

    @Override
    public void deleteComponent(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException {
        if (!running)
            throw new UnderlyingComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException();
        components.remove(componentID);
    }

    @Override
    public ArrayList<String> getComponentsID() throws UnderlyingComponentUnavailableException {
        if (!running)
            throw new UnderlyingComponentUnavailableException();
        ArrayList<String> ids = new ArrayList<>();
        for(String id : components.keySet())
            ids.add(id);
        return ids;
    }

    @Override
    public String getComponentState(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException {
        if (!running)
            throw new UnderlyingComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException();
        return ""; //todo: implement
    }

    @Override
    public void componentStart(String componentID) throws ComponentNotFoundException, AlreadyRunningException, UnderlyingComponentUnavailableException {
        if (!running)
            throw new UnderlyingComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException(componentID);
        components.get(componentID).start();
    }

    @Override
    public void componentStop(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException, ComponentDelegateException {
        if (!running)
            throw new UnderlyingComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException(componentID);
        try {
            components.get(componentID).stop();
        } catch (InvocationTargetException |IllegalAccessException e) {
            throw new ComponentDelegateException();
        }

    }

//    Component getComponent(String name) {
//        for(Component c : components)
//            if(c.getName().equals(name))
//                return c;
//        return null;
//    }
//
//    void startRTE() {
//        running= true;
//    }
//
//    void stopRTE() {
//        running= false;
//    }
//
//    boolean isRunning() {
//        return running;
//    }
//
//    void deployComponent(Component component) throws UnderlyingComponentUnavailableException, NotUniqueException {
//        if(this.running){
//            if(getComponent(component.getName())!=null)
//                throw new NotUniqueException("Component names have to be unique.");
//        }else
//            throw new UnderlyingComponentUnavailableException("RTE not aktive.");
//
//        components.add(component);
//    }
//
//    void deleteComponent(Component component) throws UnderlyingComponentUnavailableException {
//        if(this.running)
//            components.remove(component);
//        else
//            throw new UnderlyingComponentUnavailableException("RTE not aktive.");
//
//    }
//
//    void startComponent(Component component) throws InvocationTargetException, IllegalAccessException, UnderlyingComponentUnavailableException, AlreadyRunningException {
//        if(this.running)
//            component.start();
//        else
//            throw new UnderlyingComponentUnavailableException("RTE not aktive.");
//    }


}
