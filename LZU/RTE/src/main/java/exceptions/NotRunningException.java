package exceptions;

public class NotRunningException extends RuntimeException{

    public NotRunningException(){
        super();
    }

    public NotRunningException(String msg){
        super(msg);
    }
}
