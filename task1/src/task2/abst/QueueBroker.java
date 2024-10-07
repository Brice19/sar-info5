package task2.abst;

import task1.abst.Broker;

public abstract class QueueBroker extends BaseBrokerAbstract{

    protected Broker broker;

 

    public abstract String name();

    public abstract MessageQueue accept(int port);

    public abstract MessageQueue connect(String name, int port);
}

