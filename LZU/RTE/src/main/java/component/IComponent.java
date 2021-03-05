package component;

import dtos.ComponentState;
import logger.ILogger;
import publishSubscribeServer.IPublishSubscriberServer;

public interface IComponent {

    void instantiate(String id);
    void startAsync();
    void stop();
    void close();
    void subscribe(IPublishSubscriberServer iPublishSubscriberServer);
    boolean isSubscribable();
    void log(ILogger ilogger);
    boolean isLoggable();

    public String getName();
    public String getID();

    ComponentState getState();

}
