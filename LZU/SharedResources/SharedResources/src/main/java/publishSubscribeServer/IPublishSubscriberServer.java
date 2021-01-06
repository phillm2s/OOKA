package publishSubscribeServer;

import java.util.ArrayList;

public interface IPublishSubscriberServer {

    public ArrayList<ITopic> getTopics();
    public ITopic createTopic(String name);
    public boolean isTopic(String name);
    public ITopic getTopic(String name);
}
