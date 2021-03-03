package exceptions;

public class ComponentNotFoundException extends RuntimeException {

    public ComponentNotFoundException(){
        super();
    }

    public ComponentNotFoundException(String msg){
        super(msg);
    }
}
