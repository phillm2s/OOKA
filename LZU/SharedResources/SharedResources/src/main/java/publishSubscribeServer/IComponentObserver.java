package publishSubscribeServer;

import publishSubscribeServer.events.Event;

public interface IComponentObserver {

    public void notify(Event e);
}
