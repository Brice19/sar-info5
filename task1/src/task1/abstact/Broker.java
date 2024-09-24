package task1.abstact;

public abstract class Broker {
	protected String name; // Attribut commun à tous les brokers

    // Constructeur de la classe abstraite Broker
    public  Broker(String name) {
        this.name = name;
    }
	  public Channel accept(int port) {
		return null;
	}
	  public Channel connect(String name, int port) {
		return null;
	}
	public String getName() {
		return null;
	}
	}
