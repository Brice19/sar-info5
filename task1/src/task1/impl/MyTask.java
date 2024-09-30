package task1.impl;


import task1.abst.*;


public class MyTask extends Task {

	// Task has a UUID, a broker and a runnable
	private String uuid;
	private Broker broker;
	private Runnable runnable;
	private Thread thread;

	public MyTask(Broker b, Runnable r) {
		this.broker = b;
		this.runnable = r;
		this.uuid = java.util.UUID.randomUUID().toString();
		this.thread = new Thread(this.runnable, uuid);
		this.thread.start();
	}

	public static Broker getBroker() { 
		return ((MyTask) Thread.currentThread()).broker;
	}
}
