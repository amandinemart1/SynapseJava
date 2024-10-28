package synapse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Node {
    private final String nodeId;
    private final String ipAddress;
    private final Set<Network> networks; // Réseaux auxquels ce noeud appartient
    private final Map<String, String> keyValueStore; // Stockage de paires clé-valeur
    private final RoutingTable routingTable; // Table de routage pour inter-overlay
    private final Set<RequestTag> activeTags; // Tags de requêtes pour éviter les doublons

    // Constructeur de SynapseNode avec une adresse IP
    public Node(String nodeId, String ipAddress, int defaultTTL) {
        this.nodeId = nodeId;
        this.ipAddress = ipAddress;
        this.networks = new HashSet<>();
        this.keyValueStore = new HashMap<>();
        this.routingTable = new RoutingTable(defaultTTL);
        this.activeTags = new HashSet<>();
    }

    // Méthode pour se joindre à un réseau
    public void joinNetwork(Network network) {
        networks.add(network);
        network.addNode(this);
    }

    // Méthode pour insérer une paire clé-valeur dans un réseau
    public void put(String key, String value, Network network, int mrr) {
        if (networks.contains(network)) {
            network.storeKeyValue(this, key, value);
            keyValueStore.put(key, value); // Ajout au stockage local
            System.out.println("PUT: Valeur insérée dans le réseau " + network.getNetworkId());

            // Vérifier la réplication si MRR est supérieur à 0
            if (mrr > 0) {
                replicateToOtherNetworks(key, value, mrr - 1);
            }
        } else {
            System.out.println("Erreur : ce noeud n'appartient pas au réseau " + network.getNetworkId());
        }
    }

   // Méthode pour répliquer vers d'autres réseaux en fonction de MRR
   private void replicateToOtherNetworks(String key, String value, int mrr) {
    for (Network network : networks) {
        // Ne réplique que si la clé n'existe pas dans le réseau cible
        if (!network.getKeyValueStore().contains(key)) {
            Message replicationMessage = new Message("PUT", key, value, this, this, routingTable.initializeTTL(), mrr);
            this.routeMessage(replicationMessage); // Route la réplication avec MRR décrémenté
        }
    }
}

// Méthode pour traiter les messages reçus avec MRR
public void processMessage(Message message) {
    if (message.isExpired() || isMessageProcessed(message)) {
        System.out.println("Message expiré ou déjà traité.");
        return;
    }

    switch (message.getOperationCode()) {
        case "PUT":
            if (goodDeal(message.getReceiver())) {
                if (message.isReplicable()) {
                    // Réplique si possible en décrémentant MRR
                    put(message.getKey(), message.getValue(), message.getReceiver().getNetworks().iterator().next(), message.getMrr() - 1);
                }
            }
            break;
        case "GET":
            String value = get(message.getKey(), message.getReceiver().getNetworks().iterator().next());
            if (value != null) {
                found(message.getKey(), value, message.getSender()); // Notifie l'expéditeur si trouvé
            } else {
                System.out.println("Valeur non trouvée pour la clé: " + message.getKey());
            }
            break;
        default:
            System.out.println("Opération inconnue.");
    }
}
    

    // Méthode pour récupérer la valeur d'une clé
    public String get(String key, Network network) {
        if (networks.contains(network)) {
            return network.retrieveKeyValue(this, key);
        } else {
            System.out.println("Erreur : ce noeud n'appartient pas au réseau " + network.getNetworkId());
            return null;
        }
    }

    // Méthode pour inviter un autre noeud à rejoindre un réseau
    public void inviteToNetwork(Node node, Network network) {
        if (!node.networks.contains(network)) {
            node.joinNetwork(network);
            System.out.println("Invitation acceptée par le noeud " + node.nodeId + " dans le réseau " + network.getNetworkId());
        } else {
            System.out.println("Le noeud est déjà dans le réseau " + network.getNetworkId());
        }
    }

    // Vérifie si le message a déjà été traité
    private boolean isMessageProcessed(Message message) {
        RequestTag tag = new RequestTag();
        boolean processed = tag.isNodeProcessed(this);
        if (!processed) {
            tag.addProcessedNode(this); // Ajout du noeud comme traité
            activeTags.add(tag); // Stocker le tag pour les futurs traitements
        }
        return processed;
    }

    // Gère les opérations en fonction du code de l'opération
    private void handleOperation(Message message) {
        switch (message.getOperationCode()) {
            case "PUT":
                if (goodDeal(message.getReceiver())) { // Vérifie si le noeud est un bon candidat
                    put(message.getKey(), message.getValue(), message.getReceiver().getNetworks().iterator().next(), message.getMrr());
                }
                break;
            case "GET":
                String value = get(message.getKey(), message.getReceiver().getNetworks().iterator().next());
                if (value != null) {
                    found(message.getKey(), value, message.getSender()); // Notification si trouvé
                } else {
                    System.out.println("Valeur non trouvée pour la clé: " + message.getKey());
                }
                break;
            default:
                System.out.println("Opération inconnue.");
        }
    }

    // Méthode pour router le message vers le prochain noeud
    public void routeMessage(Message message) {
        Node nextHop = routingTable.routeToNextHop(message.getKey(), this);
        message.decreaseTtl();
        
        if (nextHop != null && !message.isExpired()) {
            nextHop.processMessage(message);
        } else {
            System.out.println("Routage échoué ou TTL expiré pour la clé: " + message.getKey());
        }
    }

    // Fonction goodDeal pour déterminer si un échange est favorable (renvoie toujours true pour le moment)
    public boolean goodDeal(Node otherNode) {
        return true; // Pour l'instant, on suppose que tous les noeuds sont favorables
    }

    // Fonction found pour notifier un noeud de l'obtention d'une valeur
    public void found(String key, String value, Node sender) {
        System.out.println("FOUND: Clé " + key + " trouvée avec valeur " + value + " pour le noeud " + sender.getNodeId());
    }

    // Getter pour récupérer l'adresse IP du noeud
    public String getIpAddress() {
        return ipAddress;
    }

    // Autres getters et setters
    public Set<Network> getNetworks() {
        return networks;
    }

    public String getNodeId() {
        return nodeId;
    }
}
