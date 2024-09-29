# Vue d'ensemble : QueueBroker, MessageQueue et Task

Ce framework permet l'envoi et la réception de messages entre tâches. Chaque message est transmis et reçu en une seule fois, et possède une charge utile de taille variable sous forme de tableau d'octets.

## QueueBroker

Le `QueueBroker` gère les connexions entre brokers et permet l'échange de messages via des files.

### Méthodes

- **MessageQueue accept(int port)**  
  Accepte une connexion entrante sur le port spécifié et retourne une `MessageQueue` pour échanger des messages.

- **MessageQueue connect(String name, int port)**  
  Tente de se connecter à un broker distant via le nom et le port spécifiés. Renvoie une `MessageQueue` si la connexion réussit.

## MessageQueue

La `MessageQueue` permet d'envoyer et de recevoir des messages entiers avec une charge utile variable.

### Méthodes

- **void send(byte[] bytes, int offset, int length)**  
  Envoie un message contenant les octets à partir de `offset` pour une longueur donnée.

- **byte[] receive()**  
  Reçoit un message complet sous forme de tableau d'octets. Bloque si aucun message n'est disponible.

- **void close()**  
  Ferme la file de messages.

- **boolean closed()**  
  Indique si la file est fermée.

## Task

La classe `Task` permet d'exécuter des tâches en parallèle, chaque tâche pouvant être associée à un `Broker` ou un `QueueBroker`.

### Méthodes

- **Broker getBroker()** / **QueueBroker getQueueBroker()**  
  Renvoie le broker ou la file de messages associés à cette tâche.

- **static Task getTask()**  
  Retourne la tâche actuellement en cours d'exécution.

# Fonctionnalités principales

- **Messages complets** : Chaque message est transmis comme un tout, avec une charge utile de taille variable.
- **Blocage des opérations** : Les méthodes `send` et `receive` sont bloquantes, attendant respectivement que de l'espace soit disponible ou qu'un message soit reçu.
- **Concurrence** : Les méthodes sont conçues pour permettre des échanges de messages sécurisés entre tâches, avec gestion des déconnexions.

NOTE IMPORTANTE :
Pas d implementeion from scartch
un crew broker utilise des broker
MessageQ aggrege des channels

Dans messageQ ...
on voit juste des broker, channel

brokerQ == Qbroker

tout travail dans messageQ

- il envoie/recoie un morceau , inverse du flux qui envoie un flux d oct
  ici c est un flux de message, chaque message fais une taille diff

mettre regle sur qui peut ecrire, lire ect... en gardant la coherence des channel
