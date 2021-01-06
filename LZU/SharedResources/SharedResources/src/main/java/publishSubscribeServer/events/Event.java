package publishSubscribeServer.events;

public class Event {
    private String message = "";

    public Event setMessage(String msg){
        this.message = msg;
        return this;
    }

    public String getMessage() {
        return message;
    }
}
