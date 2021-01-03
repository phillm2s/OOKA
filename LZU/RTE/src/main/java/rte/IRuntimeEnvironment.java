package rte;

import exceptions.AlreadyRunningException;
import exceptions.ComponentDelegateException;
import exceptions.ComponentNotFoundException;
import exceptions.UnderlyingComponentUnavailableException;

import java.util.ArrayList;

public interface IRuntimeEnvironment {


    public void rteStart();
    public void rteStop();
    public boolean rteIsRunning();

    public String deployComponent(String path, String componentName) throws ComponentNotFoundException, UnderlyingComponentUnavailableException; //return componentID
    public void deleteComponent(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException;
    public ArrayList<String> getComponentsID() throws UnderlyingComponentUnavailableException;
    public String getComponentState(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException;

    public void componentStart(String componentID) throws ComponentNotFoundException, AlreadyRunningException, UnderlyingComponentUnavailableException;
    public void componentStop(String componentID) throws ComponentNotFoundException, UnderlyingComponentUnavailableException, ComponentDelegateException;


}
