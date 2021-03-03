public class EventWaitHandle {

    private boolean isSet;
    private Object waitObject = new Object();


    public EventWaitHandle (boolean initWith){
        isSet=initWith;
    }
    public EventWaitHandle(){
        isSet=false;
    }

    public void set(){
        isSet=true;
        synchronized (waitObject) {
            waitObject.notifyAll();
        }
    }
    public void reset(){
        waitObject= new Object();
        isSet=false;
    }
    public boolean waitFor(){
        if(isSet)
            return true;
        synchronized (waitObject) {
            try {
                waitObject.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isSet)
                return true;
            else
                return false;
        }
    }

    public boolean waitFor(int timeoutMillis){
        if(isSet)
            return true;
        synchronized (waitObject) {
            try {
                waitObject.wait(timeoutMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(isSet)
                return true;
            else
                return false;
        }
    }


    public boolean isSet(){
        return isSet;
    }
}
