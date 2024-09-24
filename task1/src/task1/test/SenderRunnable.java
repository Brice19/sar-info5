package task1.test;

import task1.abstact.Channel;

public class SenderRunnable implements Runnable {

    private final Channel channel; // Le canal de communication
    private final byte[] dataToSend; // Les données à envoyer

    public SenderRunnable(Channel channel, byte[] dataToSend) {
        this.channel = channel;
        this.dataToSend = dataToSend;
    }

    @Override
    public void run() {
        try {
            sendData();
        } catch (Exception e) {
            System.err.println("Error during sending data: " + e.getMessage());
        }
    }

    /**
     * Envoie les données à travers le canal.
     */
    private void sendData() {
        if (channel != null && dataToSend != null) {
            try {
                // Écrire les données sur le canal
                int offset = 0;
                int length = dataToSend.length;

                // Appel à la méthode write du canal
                int bytesWritten = channel.write(dataToSend, offset, length);
                System.out.println("Sender: " + bytesWritten + " bytes sent.");

                // Déconnexion après envoi
                channel.disconnect();
                System.out.println("Sender: Channel disconnected after sending data.");

            } catch (IllegalStateException e) {
                System.err.println("Sender: Error, channel is disconnected during writing.");
            }

        } else {
            System.err.println("Sender: Channel or data is null.");
        }
    }
}
