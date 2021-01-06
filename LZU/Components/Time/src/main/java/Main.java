import publishSubscribeServer.IComponentObserver;
import publishSubscribeServer.IPublishSubscriberServer;
import publishSubscribeServer.PublishSubscriberServer;
import publishSubscribeServer.events.Event;

import javax.naming.ldap.Control;

public class Main {

    public static void main(String[] args){
        PublishSubscriberServer publishSubscriberServer = new PublishSubscriberServer();
        Controller.setPublishSubscribeServer((IPublishSubscriberServer)publishSubscriberServer);
        Controller.start();

    }
}
