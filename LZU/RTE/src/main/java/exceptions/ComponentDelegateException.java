package exceptions;

public class ComponentDelegateException extends RuntimeException {

    public ComponentDelegateException() {
        super();
    }

    public ComponentDelegateException(String msg){
        super(msg);
    }
}
