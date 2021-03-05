package rte;

import dtos.ComponentState;
import exceptions.*;
import userInterfaces.RTEState;

import java.io.IOException;
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
    String deployComponent(String path, String componentName) throws MissingAnnotationException, ComponentNotFoundException;

    /**
     * Remove Component instance.
     */
    void removeComponent(String componentID);

    /**
     * Get A list of IDs of all deployed Components
     */
    ArrayList<String> getComponentsID();
    ComponentState getComponentState(String componentID);

    void componentStart(String componentID) throws AlreadyRunningException;
    void componentStop(String componentID);

    /**
     * Return the path to json file or null if failed
     */
    String saveConfiguration();

    /**
     * @Param: fileName , the name including the type extension
     */
    void loadConfiguration(String fileName) throws IOException;

}
