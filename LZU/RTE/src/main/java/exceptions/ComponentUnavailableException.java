package exceptions;

public class ComponentUnavailableException extends Exception{

    public ComponentUnavailableException(){
        super();
    }
    public ComponentUnavailableException(String msg){
        super(msg);
    }

}
