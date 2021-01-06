package userInterfaces.console;

public abstract class CommandlineCommands {

    public static final Command RTE_START = new Command().setCommand("rte -start");
    public static final Command RTE_STOP = new Command().setCommand("rte -stop");
    public static final Command RTE_STATE = new Command().setCommand("rte -state").setDescription("Display all RTE informations including deployed components.");
    public static final Command RTE_DEPLOY = new Command().setCommand("rte -deploy").setDescription("[name] Deploy the component with given name from default component directory.");

    public static final Command COMPONENT_START = new Command().setCommand("component -start").setDescription("[name]");
    public static final Command COMPONENT_STOP = new Command().setCommand("component -stop").setDescription("[name]");
    public static final Command COMPONENTS_STATE = new Command().setCommand("component -state").setDescription("Display the state of ALL components.");


    public static String getHelp(){
        return "possible options:\n"+
                CommandlineCommands.RTE_START.toString()+"\n" +
                CommandlineCommands.RTE_STOP.toString()+"\n" +
                CommandlineCommands.RTE_STATE.toString()+"\n" +
                CommandlineCommands.RTE_DEPLOY.toString()+"\n" +

                CommandlineCommands.COMPONENT_START.toString()+"\n" +
                CommandlineCommands.COMPONENT_STOP.toString()+"\n" +
                CommandlineCommands.COMPONENTS_STATE.toString();
    }
}
