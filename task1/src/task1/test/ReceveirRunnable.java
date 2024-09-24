package task1.test;

import task1.abstact.Channel;

public class ReceveirRunnable implements Runnable {

    private final Channel channel; // Le canal de communication
    private final byte[] buffer; // Buffer pour stocker les données reçues

    public ReceveirRunnable(Channel channel, int bufferSize) {
        this.channel = channel;
        this.buffer = new byte[bufferSize]; // Initialiser un buffer pour stocker les données reçues
    }

    @Override
    public void run() {
        try {
            receiveData();
        } catch (Exception e) {
            System.err.println("Error during receiving data: " + e.getMessage());
        }
    }

    /**
     * Reçoit les données à travers le canal.
     */
    private void receiveData() {
        if (channel != null) {
            // Lire les données du canal
            int offset = 0;
            int length = buffer.length;

            try {
                // Appel à la méthode read du canal
                int bytesRead = channel.read(buffer, offset, length);

                if (bytesRead > 0) {
                    System.out.println("Receiver: " + bytesRead + " bytes received.");

                    // Afficher les données reçues pour vérification
                    System.out.print("Data received: ");
                    for (int i = 0; i < bytesRead; i++) {
                        System.out.print(buffer[i] + " ");
                    }
                    System.out.println();
                } else {
                    System.out.println("Receiver: No data received or channel disconnected.");
                }

            } catch (IllegalStateException e) {
                System.err.println("Receiver: Channel disconnected while reading.");
            }

            // Déconnecter après réception
            channel.disconnect();

        } else {
            System.err.println("Receiver: Channel is null.");
        }
    }
}
