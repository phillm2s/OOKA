package rte;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import component.Config;
import component.IComponent;
import component.ReflectionClassLoader;
import dtos.ComponentState;
import exceptions.*;
import logger.LogReceivedHandler;
import logger.LoggerFactory;
import publishSubscribeServer.IPublishSubscriberServer;
import logger.Logger;
import rte.publishSubscribeServer.PublishSubscriberServer;
import userInterfaces.RTEState;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class RuntimeEnvironment implements IRuntimeEnvironment {
    private boolean running = false;

    private LinkedHashMap<String, IComponent> components = new LinkedHashMap<>(); //linked hashMap guarantee insert order

    public RuntimeEnvironment(){

    }


    @Override
    public String saveConfiguration() {
        /**
         * Store the current deployment order persistent to a Json File
         */
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
        JsonObject json = new JsonObject();
        json.addProperty("timestamp", timeStamp);
        JsonArray deployments = new JsonArray();
        for (IComponent c : components.values()) {
            deployments.add(c.getName());
        }
        json.add("deployments", deployments);

        FileWriter fw=null;
        try{
            fw = new FileWriter(Config.CONFIG_DIRECTORY + "/rte_config_" + timeStamp + ".json");
            fw.write(json.toString());
            return Config.CONFIG_DIRECTORY + "/rte_config_" + timeStamp + ".json";
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }finally {
            try {
                fw.flush();
                fw.close();
            }catch (Exception e){}

        }

    }

    @Override
    public void loadConfiguration(String fileName) throws IOException {
        /**
         * Restart RTE and deploy all components like specified in the given json file
         */
        if(running)
            throw new RuntimeException("Restore is only allowed if RTE is not running.");
        String s=   Files.readString( Paths.get(Config.CONFIG_DIRECTORY+"/"+fileName) );
        JsonObject json= JsonParser.parseString(s).getAsJsonObject();

        //start rte
        this.rteStart();
        //deploy components like stored in file
        JsonArray deployments = json.get("deployments").getAsJsonArray();
        for(int i=0; i<deployments.size(); i++){
            this.deployComponent(Config.JAR_DIRECTORY, deployments.get(i).getAsString());
        }
    }

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
    public IComponent deployComponent(String path, String componentName){
        if (!running)
            throw new NotRunningException("RTE not running.");
        String id = componentName;
        int i=0;
        //unique component id
        while(components.containsKey(id))
            id = componentName + ++i;

        try {
            IComponent newComponent = ReflectionClassLoader.loadComponentFromFilesystem(path, componentName, id);
            components.put(id,newComponent);
            newComponent.instantiate(id);
            return newComponent;
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
