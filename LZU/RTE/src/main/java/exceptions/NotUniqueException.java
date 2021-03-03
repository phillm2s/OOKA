package exceptions;

public class NotUniqueException extends RuntimeException {

    public NotUniqueException(){
        super();
    }

    public NotUniqueException(String msg) {
        super(msg);
    }
}
