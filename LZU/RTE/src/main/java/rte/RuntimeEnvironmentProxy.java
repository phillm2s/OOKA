package rte;

import component.IComponent;
import dtos.ComponentState;
import logger.LogReceivedHandler;
import logger.Logger;
import publishSubscribeServer.IPublishSubscriberServer;
import rte.publishSubscribeServer.PublishSubscriberServer;
import userInterfaces.RTEState;

import java.io.IOException;
import java.util.ArrayList;

public class RuntimeEnvironmentProxy implements IRuntimeEnvironment { //ComponentAssembler

    private RuntimeEnvironment rte =new RuntimeEnvironment();
    private PublishSubscriberServer publishSubscriberServer = new PublishSubscriberServer();
    private Logger componentLogger;


    public RuntimeEnvironmentProxy(){
        componentLogger = new Logger()
                .addLogReceivedHandler(new LogReceivedHandler() {
                    @Override
                    public void logReceivedEvent(String log) {
                        System.out.println(log);
                    }
                });
    }

    @Override
    public void rteStart() {
        rte.rteStart();
    }

    @Override
    public void rteStop() {
        rte.rteStop();
    }

    @Override
    public RTEState rteGetState() {
        return rte.rteGetState();
    }

    @Override
    public ArrayList<String> getDeployableComponents(String path) {
        return rte.getDeployableComponents(path);
    }

    @Override
    public IComponent deployComponent(String path, String componentName){
        IComponent component= rte.deployComponent(path,componentName);
        if(component.isSubscribable())
            component.subscribe((IPublishSubscriberServer) publishSubscriberServer);
        if(component.isLoggable())
            component.log(componentLogger);
        return component;
    }

    @Override
    public void removeComponent(String componentID){
        rte.removeComponent(componentID);
    }

    @Override
    public ArrayList<String> getComponentsID() {
        return rte.getComponentsID();
    }

    @Override
    public ComponentState getComponentState(String componentID){
        return rte.getComponentState(componentID);
    }

    @Override
    public void componentStart(String componentID){
        rte.componentStart(componentID);
    }

    @Override
    public void componentStop(String componentID){
        rte.componentStop(componentID);
    }

    @Override
    public String saveConfiguration() {
        return rte.saveConfiguration();
    }

    @Override
    public void loadConfiguration(String fileName) throws IOException {
        rte.loadConfiguration(fileName);
    }


}
