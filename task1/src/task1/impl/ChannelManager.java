package task1.impl;

import task1.abstact.Channel;
import java.util.HashMap;
import java.util.Map;

public class ChannelManager {

    // Map pour gérer les canaux par broker ou connexion
    private final Map<Integer, Channel[]> channelsMap;

    public ChannelManager() {
        this.channelsMap = new HashMap<>();
    }

    /**
     * Créer un duplex de canaux entre deux tâches, un pour l'envoi et un pour la réception.
     * @param port Le port où les deux canaux seront associés.
     * @return Un tableau contenant deux canaux [writeChannel, readChannel].
     */
    public synchronized Channel[] createDuplexChannel(int port) {
        if (channelsMap.containsKey(port)) {
            throw new IllegalStateException("Channels already exist on this port.");
        }

        // Créer deux canaux pour la communication full-duplex
        Channel writeChannel = new MyChannel(1024); // Buffer de taille 1024 pour l'écriture
        Channel readChannel = new MyChannel(1024); // Buffer de taille 1024 pour la lecture

        Channel[] duplexChannels = new Channel[]{writeChannel, readChannel};
        channelsMap.put(port, duplexChannels);

        return duplexChannels;
    }

    /**
     * Récupérer les canaux associés à un port spécifique.
     * @param port Le port pour lequel récupérer les canaux.
     * @return Un tableau contenant les deux canaux associés [writeChannel, readChannel].
     */
    public synchronized Channel[] getChannels(int port) {
        if (!channelsMap.containsKey(port)) {
            throw new IllegalStateException("No channels exist on this port.");
        }
        return channelsMap.get(port);
    }

    /**
     * Supprimer les canaux associés à un port une fois qu'ils ne sont plus nécessaires.
     * @param port Le port où les canaux doivent être supprimés.
     */
    public synchronized void removeChannels(int port) {
        if (channelsMap.containsKey(port)) {
            channelsMap.remove(port);
        } else {
            throw new IllegalStateException("No channels to remove on this port.");
        }
    }
}
