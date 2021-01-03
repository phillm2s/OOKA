package userInterfaces.console;

public class Command {
    private String command="";
    private String description="";

    Command(){ }

    Command setCommand(String command){
        this.command = command;
        return this;
    }

    Command setDescription(String description){
        this.description = description;
        return this;
    }


    public String getCommand(){
        return command;
    }

    public String getDescription(){
        return description;
    }

    public String[] getCommandWords(){
        return command.split(" ");
    }

    public String toString(){
        return command + "   || Description: "+ description;
    }

}