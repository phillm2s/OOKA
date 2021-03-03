package rte;
//Proxy for LZU

import dtos.ComponentState;
import exceptions.*;
import userInterfaces.RTEState;

import java.util.ArrayList;

public class RuntimeEnvironmentProxy implements IRuntimeEnvironment { //ComponentAssembler
    private boolean quit = false;
    private RuntimeEnvironment rte =new RuntimeEnvironment();



    public RuntimeEnvironmentProxy(){

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
    public String deployComponent(String path, String componentName) throws ComponentUnavailableException, ComponentNotFoundException, MissingAnnotationException {
        return rte.deployComponent(path,componentName);
    }

    @Override
    public void removeComponent(String componentID) throws ComponentUnavailableException, ComponentNotFoundException {
        rte.removeComponent(componentID);
    }

    @Override
    public ArrayList<String> getComponentsID() {
        return rte.getComponentsID();
    }

    @Override
    public ComponentState getComponentState(String componentID) throws ComponentUnavailableException, ComponentNotFoundException, ComponentDelegateException {
        return rte.getComponentState(componentID);
    }

    @Override
    public void componentStart(String componentID) throws ComponentUnavailableException, ComponentNotFoundException, AlreadyRunningException {
        rte.componentStart(componentID);
    }

    @Override
    public void componentStop(String componentID) throws ComponentUnavailableException, ComponentNotFoundException, ComponentDelegateException {
        rte.componentStop(componentID);
    }


}
