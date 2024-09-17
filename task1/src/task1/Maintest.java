package task1;

public class Maintest {

	public static void main(String[] args) {
		// Taille du buffer et données à envoyer
		int bufferSize = 1024;
		byte[] dataToSend = { 1, 2, 3, 4, 5 };

		// Création des Brokers pour le serveur et le client
		Broker serverBroker = new Broker("ServerBroker");
		Broker clientBroker = new Broker("ClientBroker");

		// Création des Channels
		// Le serveur accepte une connexion sur un port donné
		int port = 12345;
		Channel serverChannel = serverBroker.accept(port);

		// Le client se connecte au serveur
		Channel clientChannel = clientBroker.connect("ServerBroker", port);

		// Création des instances de Sender et Receiver
		SenderRunnable sender = new SenderRunnable(clientChannel, dataToSend);
		ReceveirRunnable receiver = new ReceveirRunnable(serverChannel, bufferSize);

		// Création des Tasks pour exécuter Sender et Receiver
		Task senderTask = new Task(clientBroker, sender);
		Task receiverTask = new Task(serverBroker, receiver);

		// Lancement des Tasks
		senderTask.start();
		receiverTask.start();

		// Attendre que les tasks se terminent
		try {
			senderTask.join();
			receiverTask.join();
		} catch (InterruptedException e) {
			System.err.println("Test interrupted: " + e.getMessage());
		}

		System.out.println("Test completed.");
	}
}
