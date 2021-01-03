package exceptions;

public class AlreadyRunningException extends Exception{

    public AlreadyRunningException(){
        super();
    }

    public AlreadyRunningException(String msg){
        super(msg);
    }
}
