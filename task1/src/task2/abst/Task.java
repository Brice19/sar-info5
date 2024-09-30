package task2.abst;

import task1.abst.Broker;

public abstract class Task extends Thread {

    protected Broker broker;
    protected QueueBroker queueBroker;

    public Task(Broker b, Runnable r) {
        super(r);
        this.broker = b;
    }

    public Task(QueueBroker b, Runnable r) {
        super(r); 
        this.queueBroker = b;
    }


    public Broker getBroker() {
        return broker;
    }

    public QueueBroker getQueueBroker() {
        return queueBroker;
    }

    public static Task getTask() {
        // Implémentation à définir selon le contexte de l'application
        return null;
    }
}
