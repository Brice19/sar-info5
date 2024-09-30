# Spécification

## QueueBroker

Le `QueueBroker` est une extension du broker classique. Il gère les connexions entre brokers et retourne des `MessageQueue` au lieu de `Channel`. Les brokers utilisent les `MessageQueue` comme structure principale pour l'échange de messages.

### Méthodes principales

- **`MessageQueue accept(int port)`**  
  Accepte une connexion entrante sur le port spécifié et retourne une `MessageQueue` pour permettre l'échange de messages complets.

- **`MessageQueue connect(String name, int port)`**  
  Tente de se connecter à un broker distant via le nom et le port indiqués. Renvoie une `MessageQueue` si la connexion est établie avec succès.

## MessageQueue

Le système de messagerie est basé sur les `MessageQueue`, qui remplacent les `Channel` traditionnels. Une `MessageQueue` est une file d'attente de messages où chaque message est traité comme un bloc distinct sous forme de tableau d'octets (`byte[]`). Les `MessageQueue` respectent un modèle FIFO (First In, First Out) et sont associées aux `Channel` pour les communications sous-jacentes.

### Méthodes principales

```java
void send(byte[] bytes, int offset, int length);
byte[] receive();
void close();
boolean closed();
```

- **`void send(byte[] bytes, int offset, int length)`**  
  Envoie un message complet (sous forme de tableau d'octets) à la file d'attente.

  - **Paramètres** :
    - `bytes` : Le tableau d'octets contenant le message à envoyer.
    - `offset` : L'indice de départ du message dans le tableau d'octets.
    - `length` : Le nombre d'octets à envoyer.  
      Cette méthode est **bloquante** : elle attendra qu'il y ait suffisamment d'espace disponible dans la file d'attente pour envoyer le message dans son intégralité.

- **`byte[] receive()`**  
  Reçoit un message complet de la file d'attente et le retourne sous forme de tableau d'octets. Cette méthode est **bloquante** : elle attendra qu'un message soit disponible avant de retourner le tableau d'octets.

- **`void close()`**  
  Ferme la `MessageQueue`. Une fois fermée, aucun autre message ne pourra être envoyé ou reçu via cette file.

- **`boolean closed()`**  
  Retourne `true` si la `MessageQueue` est fermée, `false` sinon.

  ## Task

Chaque `Task` est associée à un `QueueBroker`.

### Méthodes principales

```java
Task(Broker b, Runnable r);
Task(QueueBroker b, Runnable r);
Broker getBroker();
QueueBroker getQueueBroker();
static Task getTask();
```

### `Task(Broker b, Runnable r)`

Crée une nouvelle `Task` associée à un `Broker` et définit le `Runnable` qui sera exécuté par cette tâche.

### `Task(QueueBroker b, Runnable r)`

Crée une nouvelle `Task` associée à un `QueueBroker` et définit le `Runnable` qui sera exécuté.

### `Broker getBroker()`

Retourne le `Broker` associé à la `Task`.

### `QueueBroker getQueueBroker()`

Retourne le `QueueBroker` associé à la `Task`.

### `static Task getTask()`

Retourne la `Task` associée au thread en cours d'exécution, permettant d'accéder au contexte de la tâche courante.

## Points importants

- **Pas de réimplémentation depuis zéro** : Le `QueueBroker` utilise des `Brokers` pour la gestion des connexions, et `MessageQueue` s'appuie sur les `Channels`.
- **Structure d'agrégation** : Les `MessageQueue` agrègent des `Channels`, assurant une gestion fluide des échanges de messages.
- **Flux de messages** : Contrairement à un flux continu d'octets, chaque message dans une `MessageQueue` est une unité distincte, facilitant la lecture et l'écriture de blocs de données indépendants.
