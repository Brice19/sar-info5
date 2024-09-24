package task1;

public class ReceveirRunnable implements Runnable {

    private Channel channel; // Le canal de communication
    private byte[] buffer; // Buffer pour stocker les données reçues

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

            // Appel à la méthode read du canal
            int bytesRead = channel.read(buffer, offset, length);
            System.out.println("Receiver: " + bytesRead + " bytes received.");

            // Afficher les données reçues pour vérification
            System.out.print("Data received: ");
            for (int i = 0; i < bytesRead; i++) {
                System.out.print(buffer[i] + " ");
            }
            System.out.println();
        } else {
            System.err.println("Receiver: Channel is null.");
        }
    }
}
