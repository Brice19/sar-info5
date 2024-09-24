package task1.test;

import task1.impl.MyBroker;
import task1.abstact.Channel;
import task1.impl.BrokerManager;

public class Maintest {

    public static void main(String[] args) {
        // Taille du buffer et données à envoyer
        int bufferSize = 1024;
        byte[] dataToSend = { 1, 2, 3, 4, 5 };

        // Création d'un gestionnaire de brokers
        BrokerManager brokerManager = new BrokerManager();

        // Création des brokers pour le serveur et le client
        MyBroker serverBroker = new MyBroker("ServerBroker");
        MyBroker clientBroker = new MyBroker("ClientBroker");

        // Enregistrement des brokers dans le BrokerManager
        brokerManager.registerBroker(serverBroker);
        brokerManager.registerBroker(clientBroker);

        // Debug: Vérification de l'enregistrement des brokers
        System.out.println("ServerBroker and ClientBroker registered in BrokerManager.");

        // Définition du port de communication
        int port = 12345;

        try {
            // Le serveur accepte une connexion sur le port
            Channel serverChannel = brokerManager.acceptConnection("ServerBroker", port);

            // Le client se connecte au serveur sur le même port
            Channel clientChannel = brokerManager.connectToBroker("ClientBroker", port);

            // Création des instances de Sender et Receiver
            SenderRunnable sender = new SenderRunnable(clientChannel, dataToSend);
            ReceveirRunnable receiver = new ReceveirRunnable(serverChannel, bufferSize);

            // Création des threads pour exécuter Sender et Receiver
            Thread senderThread = new Thread(sender);
            Thread receiverThread = new Thread(receiver);

            // Lancement des Threads
            senderThread.start();
            receiverThread.start();

            // Attendre que les threads se terminent
            senderThread.join();
            receiverThread.join();

            System.out.println("Test completed.");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
