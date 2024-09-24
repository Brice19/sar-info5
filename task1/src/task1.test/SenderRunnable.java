package task1;


public class SenderRunnable implements Runnable {

    private Channel channel; // Le canal de communication
    private byte[] dataToSend; // Les données à envoyer

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
            // Écrire les données sur le canal
            int offset = 0;
            int length = dataToSend.length;

            // Appel à la méthode write du canal
            int bytesWritten = channel.write(dataToSend, offset, length);
            System.out.println("Sender: " + bytesWritten + " bytes sent.");
        } else {
            System.err.println("Sender: Channel or data is null.");
        }
    }
}
