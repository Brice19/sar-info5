package task1.impl;

import task1.abstact.Broker;
import task1.abstact.Channel;

import java.util.HashMap;
import java.util.Map;

public class BrokerManager {

    private final Map<String, Broker> brokers; // Stocker les brokers par nom
    private final Map<Integer, RDV> rdvMap;    // Gérer les RDV par port

    public BrokerManager() {
        this.brokers = new HashMap<>();
        this.rdvMap = new HashMap<>();
    }

    /**
     * Enregistrer un nouveau broker.
     */
    public synchronized void registerBroker(Broker broker) {
        System.out.println("Registering broker: " + broker.getName()); // Debug log
        brokers.put(broker.getName(), broker);
    }

    /**
     * Vérifier l'enregistrement des brokers.
     */
    public synchronized void listRegisteredBrokers() {
        System.out.println("Currently registered brokers: " + brokers.keySet());
    }

    /**
     * Accepter une connexion à un broker enregistré.
     */
    public synchronized Channel acceptConnection(String brokerName, int port) {
        // Vérification des brokers enregistrés
        listRegisteredBrokers(); // Debug log

        Broker broker = brokers.get(brokerName);
        if (broker == null) {
            throw new IllegalStateException("Broker not found: " + brokerName);
        }

        // Gérer les RDV à travers plusieurs brokers
        RDV rdv = rdvMap.get(port);
        if (rdv == null) {
            rdv = new RDV(broker);
            rdvMap.put(port, rdv);
        }

        rdv.come(broker);

        while (!rdv.isComplete()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return new MyChannel(1024);
    }

    /**
     * Se connecter à un autre broker.
     */
    public synchronized Channel connectToBroker(String brokerName, int port) {
        // Vérification des brokers enregistrés
        listRegisteredBrokers(); // Debug log

        Broker broker = brokers.get(brokerName);
        if (broker == null) {
            throw new IllegalStateException("Broker not found: " + brokerName);
        }

        // Gérer les RDV entre plusieurs brokers
        RDV rdv = rdvMap.get(port);
        if (rdv == null) {
            rdv = new RDV(broker);
            rdvMap.put(port, rdv);
        }

        rdv.come(broker);

        while (!rdv.isComplete()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return new MyChannel(1024);
    }
}
