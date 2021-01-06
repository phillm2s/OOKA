package publishSubscribeServer;

import publishSubscribeServer.events.Event;

public interface ITopic {
    public String getName();
    public void subscribe(IComponentObserver observer);
    public void unsubscribe(IComponentObserver observer);
    public void notify(Event e);
}
