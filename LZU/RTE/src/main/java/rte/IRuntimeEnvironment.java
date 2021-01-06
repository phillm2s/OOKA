package rte;

import dtos.ComponentState;
import exceptions.*;

import java.util.ArrayList;

public interface IRuntimeEnvironment {


    public void rteStart();
    public void rteStop();
    public boolean rteIsRunning();

    public String deployComponent(String path, String componentName) throws ComponentNotFoundException, UnderlyingComponentUnavailableException, MissingAnnotationException; //return componentID
    public void deleteComponent(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException;
    public ArrayList<String> getComponentsID() throws UnderlyingComponentUnavailableException;
    public ComponentState getComponentState(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException, ComponentDelegateException;

    public void componentStart(String componentID) throws ComponentNotFoundException, AlreadyRunningException, UnderlyingComponentUnavailableException;
    public void componentStop(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException, ComponentDelegateException;


}
