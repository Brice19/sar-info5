package task3.abst;

import task1.abst.Broker;
import task1.impl.MyBroker;

public abstract class QueueBroker {

	public Broker broker;
	public String name;
	
	public QueueBroker(String name) throws Exception {
		this.broker = new MyBroker(name);
		this.name = name;
	}

	public interface AcceptListener {
		void accepted(MessageQueue queue);
	}

	public abstract boolean bind(int port, AcceptListener listener);

	public abstract boolean unbind(int port);

	public interface ConnectListener {
		void connected(MessageQueue queue);

		void refused();
	}

	public abstract boolean connect(String name, int port, ConnectListener listener);
}