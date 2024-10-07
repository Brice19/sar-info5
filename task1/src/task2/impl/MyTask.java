package task2.impl;

import task1.abst.Broker;
import task2.abst.QueueBroker;
import task2.abst.BaseBrokerAbstract;
import task2.abst.Task;

public class MyTask extends Task {

	// Task has a UUID, a broker and a runnable
	private String uuid;
	private BaseBrokerAbstract broker;
	private Runnable runnable;
	private Thread thread;

	public MyTask(BaseBrokerAbstract b, Runnable r) {
        this.broker = b;
        this.runnable = r;
        this.uuid = java.util.UUID.randomUUID().toString();
        this.thread = new Thread(this.runnable, uuid);
        this.thread.start();
    }

	public static Broker getBroker() { 
		return (Broker) ((MyTask) Thread.currentThread()).broker;
	}
	
	public static QueueBroker getQueueBroker() { 
		return (QueueBroker) ((MyTask) Thread.currentThread()).broker;
	}
}