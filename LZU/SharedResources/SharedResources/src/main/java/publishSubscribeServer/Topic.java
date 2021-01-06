package publishSubscribeServer;

import publishSubscribeServer.IComponentObserver;
import publishSubscribeServer.events.Event;

import java.util.ArrayList;

public class Topic implements ITopic{

    private ArrayList<IComponentObserver> subscriber = new ArrayList<>();
    private String name ="";

    public Topic(String name){
        this.name = name;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public void subscribe(IComponentObserver observer){
        subscriber.add(observer);
    }

    @Override
    public void unsubscribe(IComponentObserver observer){
        subscriber.remove(observer);
    }

    @Override
    public void notify(Event e) { //delegate notification to all subscribers
        System.out.println("Received notification:" +e.getMessage()+"\n" +
                "Delegated to all "+ name +" subscriber");
        for(IComponentObserver subscriber : subscriber)
            subscriber.notify(e);
    }
}
