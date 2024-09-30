package task1.impl;

import java.util.HashMap;

import task1.abst.*;

public class MyBroker extends Broker {

    private BrokerManager manager;
    HashMap<Integer, RDV> accepts;
    public MyBroker(String name) {
        super(name);
        accepts = new HashMap<Integer, RDV>();
        manager = BrokerManager.getSelf();
        manager.addBroker(this);    
    }

    @Override
    public Channel connect(String name, int port){
        MyBroker b = (MyBroker) manager.getBroker(name);
        if (b == null) {
            throw new RuntimeException("Broker with name " + name + " does not exist");
        }
        return b.toConnect(this, port);
    }

    @Override
    public Channel accept(int port){
        RDV rdv = null;
        synchronized (accepts) {
            rdv = accepts.get(port);
            if (rdv != null) {
                throw new RuntimeException("Port " + port + " already in use");
            }
            rdv = new RDV();
            accepts.put(port, rdv);
            accepts.notifyAll();
        }
        Channel c;
        c = rdv.accept(this, port);
        return c;
    }

    private Channel toConnect(MyBroker b, int port) {
        RDV rdv = null;
        synchronized (accepts) {
            rdv = accepts.get(port);
            while (rdv == null) {
                try {
                    accepts.wait();
                } catch (InterruptedException e) {
                }
                rdv = accepts.get(port);
            }
        accepts.remove(port);
        }
        return rdv.connect(b, port);
    }

	@Override
	public String getName() {
		
		return this.name;
	}


}
