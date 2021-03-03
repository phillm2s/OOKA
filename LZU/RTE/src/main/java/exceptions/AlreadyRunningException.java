package exceptions;

public class AlreadyRunningException extends RuntimeException{

    public AlreadyRunningException(){
        super();
    }

    public AlreadyRunningException(String msg){
        super(msg);
    }
}
