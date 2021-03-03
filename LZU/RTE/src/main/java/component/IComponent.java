package component;

import dtos.ComponentState;
import publishSubscribeServer.IPublishSubscriberServer;

public interface IComponent {

    void instantiate(String id);
    void startAsync();
    void stop();
    void close();
    void subscribe(IPublishSubscriberServer iPublishSubscriberServer);
    boolean isSubscribable();

    ComponentState getState();

}
