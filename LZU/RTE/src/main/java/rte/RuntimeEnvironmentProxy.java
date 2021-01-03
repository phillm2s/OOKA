package rte;
//Proxy for LZU

import component.Component;
import exceptions.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
    public String deployComponent(String path, String componentName) throws UnderlyingComponentUnavailableException, ComponentNotFoundException {
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
    public String getComponentState(String componentID) throws UnderlyingComponentUnavailableException, ComponentNotFoundException {
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
