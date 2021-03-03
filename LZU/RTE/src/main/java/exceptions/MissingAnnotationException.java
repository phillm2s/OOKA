package exceptions;

public class MissingAnnotationException extends RuntimeException {

    public MissingAnnotationException(){
        super();
    }

    public MissingAnnotationException(String msg){
        super(msg);
    }
}
