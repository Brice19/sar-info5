package task1.impl;

import task1.abstact.Broker;

public class RDV {

    private Broker broker1; // Premier broker
    private Broker broker2; // Second broker
    private int nexpected; // Nombre de brokers attendus au RDV

    // Constructeur, initialement on attend 2 brokers
    public RDV(Broker broker1) {
        this.broker1 = broker1;
        this.broker2 = null;
        this.nexpected = 1; // Un broker est déjà là, on en attend un autre
    }

    // Méthode appelée lorsqu'un broker arrive au RDV
    public synchronized void come(Broker broker) {
        // Si broker2 est vide, on enregistre ce broker comme le second
        if (broker2 == null) {
            broker2 = broker;
        }
        nexpected--; // Décrémenter le nombre de brokers attendus

        // Si tous les brokers sont arrivés, notifier tous les threads en attente
        if (nexpected == 0) {
            notifyAll();
        } else {
            // Sinon, attendre que le second broker arrive
            while (nexpected > 0) {
                try {
                    wait(); // Bloquer jusqu'à ce que l'autre broker arrive
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Bonne pratique en cas d'interruption
                }
            }
        }
    }

    // Vérifie si les deux brokers sont arrivés au RDV
    public synchronized boolean isComplete() {
        return broker1 != null && broker2 != null;
    }

    // Retourne le premier broker
    public Broker getBroker1() {
        return broker1;
    }

    // Retourne le second broker
    public Broker getBroker2() {
        return broker2;
    }
}
