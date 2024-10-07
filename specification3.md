## Class `QueueBroker` :

### Interface `AcceptListener`

- **Méthode :**
  void accepted(MessageQueue queue);
  Cette méthode est appelée lorsqu'une connexion est acceptée.

### Méthodes de `QueueBroker` :

- **`boolean bind(int port, AcceptListener listener)` :**

  - Associe un `AcceptListener` à un port donné.
  - **Retourne :** `true` si la connexion a été acceptée, `false` si elle a échoué (par exemple, si le port est déjà pris).

- **`boolean unbind(int port)` :**
  - Déconnecte et libère le port donné.
  - **Retourne :** `true` si la déconnexion a réussi, `false` si elle a échoué.

### Interface `ConnectListener`

- **Méthodes :**
  void connected(MessageQueue queue);

  - Appelée lorsqu'une connexion a été établie avec succès.

  void refused();

  - Appelée lorsque la tentative de connexion a été refusée.

### Méthodes de `QueueBroker` :

- **`boolean connect(String name, int port, ConnectListener listener)` :**
  - Établit une connexion à un autre `QueueBroker` par nom et port.
  - **Retourne :** `true` si la connexion est en cours, `false` si elle a échoué.

---

## Class `MessageQueue` :

### Interface `Listener`

- **Méthodes :**
  void received(byte[] message);

  - Appelée lorsqu'un message complet est reçu.

  void closed();

  - Appelée lorsque la file de messages est fermée.

### Méthodes de `MessageQueue` :

- **`void setListener(Listener listener)` :**

  - Associe un `Listener` à cette file de messages pour être notifié des nouveaux messages reçus et de la fermeture de la file.

- **`boolean send(byte[] message)` :**

  - Envoie un message complet via la file.
  - **Retourne :** `true` si l'envoi a réussi, `false` sinon.

  soit il prend un byte array et l autre oublie, il prend l ownership, sinon copie

- **`boolean send(byte[] message, int offset, int length)` :**

  - Envoie une partie d'un message spécifiée par un décalage (`offset`) et une longueur (`length`).
  - **Retourne :** `true` si l'envoi a réussi, `false` sinon.

- **`void close()` :**

  - Ferme la file de messages, empêchant tout nouvel envoi ou réception.

- **`boolean closed()` :**
  - **Retourne :** `true` si la file est fermée, `false` sinon.
