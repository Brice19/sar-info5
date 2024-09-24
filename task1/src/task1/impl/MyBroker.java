package task1.impl;

import task1.abstact.Broker;
import task1.abstact.Channel;
import task1.impl.MyChannel;

public class MyBroker extends Broker {

    public MyBroker(String name) {
        super(name);
    }

    @Override
    public synchronized Channel accept(int port) {
        // Gérer un RDV unique pour accepter une connexion
        RDV rdv = new RDV(this);
        rdv.come(this);

        // Créer un canal après l'acceptation de la connexion
        return new MyChannel(1024);
    }

    @Override
    public synchronized Channel connect(String remoteBrokerName, int port) {
        // Gérer un RDV unique pour se connecter à un autre Broker
        RDV rdv = new RDV(this);
        rdv.come(this);

        // Créer un canal pour la connexion
        return new MyChannel(1024);
    }
    
    @Override
    public String getName() {
        return this.name;  // Retourne le nom du broker
    }
}
