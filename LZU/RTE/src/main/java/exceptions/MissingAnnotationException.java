package exceptions;

public class MissingAnnotationException extends Exception {

    public MissingAnnotationException(){
        super();
    }

    public MissingAnnotationException(String msg){
        super(msg);
    }
}
