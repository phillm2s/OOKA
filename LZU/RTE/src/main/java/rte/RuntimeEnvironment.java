package rte;
import component.IComponent;
import component.ReflectionClassLoader;
import dtos.ComponentState;
import exceptions.*;
import publishSubscribeServer.IPublishSubscriberServer;
import rte.publishSubscribeServer.PublishSubscriberServer;
import userInterfaces.RTEState;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RuntimeEnvironment implements IRuntimeEnvironment{
    private boolean running = false;
    private PublishSubscriberServer publishSubscriberServer = new PublishSubscriberServer();

    private HashMap<String, IComponent> components = new HashMap<>();


    @Override
    public void rteStart() {
        running= true;
    }

    @Override
    public void rteStop() {
        for (IComponent component : components.values()) {
            component.close();
        }
        components.clear();
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
                ReflectionClassLoader.loadComponentFromFilesystem(path, f,null);
                validComponents.add(f);
            }catch(Exception e){
                System.out.println(f+ "i s no valid Component");
            }
        }
        return validComponents;
    }

    @Override
    public String deployComponent(String path, String componentName){
        if (!running)
            throw new ComponentUnavailableException();
        String id = componentName;
        int i=0;
        //unique component id
        while(components.containsKey(id))
            id = componentName + ++i;

        try {
            IComponent newComponent = ReflectionClassLoader.loadComponentFromFilesystem(path, componentName, id);
            components.put(id,newComponent);
            newComponent.instantiate(id);
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
    public void removeComponent(String componentID){
        if (!running)
            throw new ComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException();

        components.get(componentID).close();
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
    public ComponentState getComponentState(String componentID){
        if (!running)
            throw new ComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException();
        return components.get(componentID).getState();
    }

    @Override
    public void componentStart(String componentID){
        if (!running)
            throw new ComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException(componentID);
        components.get(componentID).startAsync();
    }

    @Override
    public void componentStop(String componentID) {
        if (!running)
            throw new ComponentUnavailableException();
        if(!components.containsKey(componentID))
            throw new ComponentNotFoundException(componentID);

        components.get(componentID).stop();
    }

}
