package rte;

import component.Component;
import dtos.ComponentState;
import exceptions.*;
import publishSubscribeServer.IPublishSubscriberServer;
import rte.publishSubscribeServer.PublishSubscriberServer;
import userInterfaces.RTEState;

import java.io.File;
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
                component.close();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        running=false;
    }

    @Override
    public RTEState rteGetState() {
        return new RTEState().setRunning(running);
    }

    @Override
    public ArrayList<String> getDeployableComponents(String path) {
        //list all .jar files inside directory
        String[] fileNames = new File(path).list((dir, name) -> name.toLowerCase().endsWith(".jar"));
        //Try to deploy each file. if its possible its valid. but dont store the deployed file
        ArrayList<String> validComponents = new ArrayList<>();
        for(String f : fileNames){
            f= f.replace(".jar","");
            try{
                ReflectionClassLoader.loadComponentFromFilesystem(path, f);
                validComponents.add(f);
            }catch(Exception e){
                System.out.println(f+ "i s no valid Component");
            }
        }
        return validComponents;
    }

    @Override
    public String deployComponent(String path, String componentName) throws ComponentNotFoundException, ComponentUnavailableException, MissingAnnotationException {
        if (!running)
            throw new ComponentUnavailableException();
        String id = componentName;
        int i=0;
        //unique component id
        while(components.containsKey(id))
            id = componentName + ++i;

        try {
            Component newComponent = ReflectionClassLoader.loadComponentFromFilesystem(path, componentName);
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
    public void removeComponent(String componentID) throws ComponentNotFoundException, ComponentUnavailableException {
        if (!running)
            throw new ComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException();

        try {
            components.get(componentID).close();
        }catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        components.remove(componentID);
    }

    @Override
    public ArrayList<String> getComponentsID() {
        ArrayList<String> ids = new ArrayList<>();
        for(String id : components.keySet())
            ids.add(id);
        return ids;
    }

    @Override
    public ComponentState getComponentState(String componentID) throws ComponentNotFoundException, ComponentUnavailableException, ComponentDelegateException {
        if (!running)
            throw new ComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException();
        return components.get(componentID).getState();
    }

    @Override
    public void componentStart(String componentID) throws ComponentNotFoundException, AlreadyRunningException, ComponentUnavailableException {
        if (!running)
            throw new ComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException(componentID);
        components.get(componentID).startAsync();
    }

    @Override
    public void componentStop(String componentID) throws ComponentNotFoundException, ComponentUnavailableException, ComponentDelegateException {
        if (!running)
            throw new ComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException(componentID);
        try {
            components.get(componentID).stop();
        } catch (InvocationTargetException |IllegalAccessException e) {
            throw new ComponentDelegateException();
        }

    }

}
