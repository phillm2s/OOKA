package exceptions;

public class ComponentUnavailableException extends RuntimeException{

    public ComponentUnavailableException(){
        super();
    }
    public ComponentUnavailableException(String msg){
        super(msg);
    }

}
