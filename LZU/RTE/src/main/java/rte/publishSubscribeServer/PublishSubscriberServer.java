package rte.publishSubscribeServer;

import component.IComponent;
import publishSubscribeServer.IPublishSubscriberServer;
import publishSubscribeServer.ITopic;

import java.util.ArrayList;
import java.util.HashMap;

public class PublishSubscriberServer implements IPublishSubscriberServer {
    private HashMap<String,Topic> topics = new HashMap<>();

    @Override
    public ArrayList<ITopic> getTopics() {
        ArrayList<ITopic> itopics = new ArrayList<>();
        for(Topic t : topics.values())
            itopics.add((ITopic) t);
        return itopics;
    }

    @Override
    public ITopic createTopic(String name) {
        if (!topics.containsKey(name))
            topics.put(name,new Topic(name));
        return (ITopic)topics.get(name);
    }

    @Override
    public boolean isTopic(String name) {
        return topics.containsKey(name);
    }

    @Override
    public ITopic getTopic(String name) {
        return topics.get(name);
    }
}
