package rte;

import dtos.ComponentState;
import exceptions.*;
import userInterfaces.RTEState;

import java.util.ArrayList;

public interface IRuntimeEnvironment {

    void rteStart();

    void rteStop();

    /**
     * Return the current state of the RTE wrapped as a DTO.
     */
    RTEState rteGetState();

    /**
     * Return the names of deployable Components in the given directory
     */
    ArrayList<String> getDeployableComponents(String path);

    /**
     * Deploy a new instance from the given Component and return its new unique id.
     */
    String deployComponent(String path, String componentName) throws ComponentNotFoundException, ComponentUnavailableException, MissingAnnotationException; //return componentID

    /**
     * Remove Component instance.
     * Running instances dont get interrupted!
     */
    void removeComponent(String componentID) throws ComponentNotFoundException, ComponentUnavailableException;

    /**
     * Get A list of IDs of all deployed Components
     */
    ArrayList<String> getComponentsID();
    ComponentState getComponentState(String componentID) throws ComponentNotFoundException, ComponentUnavailableException, ComponentDelegateException;

    void componentStart(String componentID) throws ComponentNotFoundException, AlreadyRunningException, ComponentUnavailableException;
    void componentStop(String componentID) throws ComponentNotFoundException, ComponentUnavailableException, ComponentDelegateException;


}
