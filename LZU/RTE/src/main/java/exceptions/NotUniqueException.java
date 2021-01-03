package exceptions;

public class NotUniqueException extends Exception {

    public NotUniqueException(){
        super();
    }

    public NotUniqueException(String msg) {
        super(msg);
    }
}
