package task1.abst;

public abstract class Broker {
	protected String name;

	public Broker(String name) {
		this.name = name;
	}

	public abstract Channel accept(int port);

	public abstract Channel connect(String name, int port);

	public abstract String getName();

}

