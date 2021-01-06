package rte;

import component.Component;
import dtos.ComponentState;
import exceptions.*;
import publishSubscribeServer.IPublishSubscriberServer;
import publishSubscribeServer.PublishSubscriberServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class RuntimeEnvironment implements IRuntimeEnvironment{
    private boolean running = false;
    private PublishSubscriberServer publishSubscriberServer = new PublishSubscriberServer();

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
    public String deployComponent(String path, String componentName) throws ComponentNotFoundException, UnderlyingComponentUnavailableException, MissingAnnotationException {
        if (!running)
            throw new UnderlyingComponentUnavailableException();
        String id = componentName;
        int i=0;
        //unique component id
        while(components.containsKey(id))
            id = componentName + ++i;

        try {
            Component newComponent = new ReflectionClassLoader().loadComponentFromFilesystem(Component.JAR_DIRECTORY, componentName);
            components.put(id,newComponent);
            if(newComponent.isSubscribable())
                try {
                    newComponent.subscribe((IPublishSubscriberServer) publishSubscriberServer);
                }catch (ComponentDelegateException e){
                    e.printStackTrace();
                }
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
    public ComponentState getComponentState(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException, ComponentDelegateException {
        if (!running)
            throw new UnderlyingComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException();
        return components.get(componentID).getState();
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

}
