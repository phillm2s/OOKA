package exceptions;

public class ComponentNotFoundException extends Exception {

    public ComponentNotFoundException(){
        super();
    }

    public ComponentNotFoundException(String msg){
        super(msg);
    }
}
