package task1.abst;

import task2.abst.BaseBrokerAbstract;

public abstract class Broker extends BaseBrokerAbstract{
	protected String name;

	public Broker(String name) {
		this.name = name;
	}

	public abstract Channel accept(int port);

	public abstract Channel connect(String name, int port);

	public abstract String getName();

}

