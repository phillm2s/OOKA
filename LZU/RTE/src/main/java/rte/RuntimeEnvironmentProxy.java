package rte;
//Proxy for LZU

import dtos.ComponentState;
import exceptions.*;

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
    public boolean rteIsRunning() {
        return rte.rteIsRunning();
    }

    @Override
    public String deployComponent(String path, String componentName) throws UnderlyingComponentUnavailableException, ComponentNotFoundException, MissingAnnotationException {
        return rte.deployComponent(path,componentName);
    }

    @Override
    public void deleteComponent(String componentID) throws UnderlyingComponentUnavailableException, ComponentNotFoundException {
        rte.deleteComponent(componentID);
    }

    @Override
    public ArrayList<String> getComponentsID() throws UnderlyingComponentUnavailableException {
        return rte.getComponentsID();
    }

    @Override
    public ComponentState getComponentState(String componentID) throws UnderlyingComponentUnavailableException, ComponentNotFoundException, ComponentDelegateException {
        return rte.getComponentState(componentID);
    }

    @Override
    public void componentStart(String componentID) throws UnderlyingComponentUnavailableException, ComponentNotFoundException, AlreadyRunningException {
        rte.componentStart(componentID);
    }

    @Override
    public void componentStop(String componentID) throws UnderlyingComponentUnavailableException, ComponentNotFoundException, ComponentDelegateException {
        rte.componentStop(componentID);
    }


}
