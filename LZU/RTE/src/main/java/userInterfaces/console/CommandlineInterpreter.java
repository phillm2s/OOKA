package userInterfaces.console;

import component.Component;
import exceptions.*;
import rte.IRuntimeEnvironment;

import java.util.Scanner;

public class CommandlineInterpreter {

    private IRuntimeEnvironment rte;
    private boolean listen= false;
    private Scanner keyboard = null;

    public CommandlineInterpreter(IRuntimeEnvironment rte){
        this.rte = rte;
    }

    public void quit(){
        listen=false;
        keyboard.close();
    }

    public void listen() { //blocking
        keyboard = new Scanner(System.in);
        System.out.println("Waiting for input...\n" +
                CommandlineCommands.getHelp() );

        while(listen ==false){
            String input = keyboard.nextLine();
            try {
                interpret(input);
            }catch (Exception e){
                e.printStackTrace(); //uncatched exception should never be reached
            }
        }
    }

    public CommandlineInterpreter interpret(String input){
        String[] words = input.split(" ");
        
        if(input.equals(CommandlineCommands.RTE_START.getCommand())) {
            rte.rteStart();
            if(rte.rteIsRunning())
                System.out.println("RTE started.");
            else
                System.out.println("Starting RTE failed.");
        }
        else if (input.equals(CommandlineCommands.RTE_STOP.getCommand())) {
            rte.rteStop();
            if(!rte.rteIsRunning())
                System.out.println("RTE stopped.");
            else
                System.out.println("Stopping RTE failed.");
        }
        else if (input.equals((CommandlineCommands.RTE_STATE).getCommand())) {
            String s = "RTE Running: " + rte.rteIsRunning() + "\n" +
                    "Deployed Components:\n";
            try {
                if(rte.getComponentsID().size()==0)
                    s+= "none";
            } catch (UnderlyingComponentUnavailableException e) {
                e.printStackTrace();
            }
            try {
                for (String c : rte.getComponentsID())
                    s+= c+"\n";
            } catch (UnderlyingComponentUnavailableException e) {
                e.printStackTrace();
            }
            System.out.println(s);
        }

        else if(input.startsWith(CommandlineCommands.RTE_DEPLOY.getCommand())){
            String componentName = input.substring(input.lastIndexOf(" ")+1);
            try {
                String componentID = rte.deployComponent( Component.JAR_DIRECTORY,componentName);
                System.out.println(componentName +" deployed with componentID: "+componentID);
            } catch (ComponentNotFoundException e) {
                e.printStackTrace();
            } catch (UnderlyingComponentUnavailableException e1){
                e1.printStackTrace();
            } catch (MissingAnnotationException e) {
                e.printStackTrace();
            }
        }

        else if (words.length==3 && input.startsWith(CommandlineCommands.COMPONENT_START.getCommand())) {
            try {
                rte.componentStart(words[2]);
            } catch (ComponentNotFoundException e) {
                e.printStackTrace();
            } catch (UnderlyingComponentUnavailableException e) {
                e.printStackTrace();
            } catch (AlreadyRunningException e) {
                e.printStackTrace();
            }
        }

        else if (words.length==3 && input.startsWith(CommandlineCommands.COMPONENT_STOP.getCommand())) {
            try {
                rte.componentStop(words[2]);
            } catch (ComponentNotFoundException e) {
                e.printStackTrace();
            } catch (UnderlyingComponentUnavailableException e) {
                e.printStackTrace();
            } catch (ComponentDelegateException e) {
                e.printStackTrace();
            }
        }

        else if (input.equals(CommandlineCommands.COMPONENTS_STATE.getCommand())) {
            try {
                String s = "Components:\n";
                for (String component : rte.getComponentsID()) {
                    try {
                        s +=  component+":\n";
                        if(rte.getComponentState(component).isRunning)
                            s +=  "Status: active\n";
                        else
                            s +=  "Status: inactive\n";
                    } catch (ComponentNotFoundException e) {
                        e.printStackTrace();
                    } catch (ComponentDelegateException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(s);
            } catch (UnderlyingComponentUnavailableException e) {
                e.printStackTrace();
            }
        }

        else
            System.out.println("Comment not found.");
        
        
        return this;
    }
}
