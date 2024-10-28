package synapse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Network {
    private final String networkId;
    private final Map<String, String> keyValueStore;  // Stockage pour toutes les paires clé-valeur
    private final Set<Node> nodes;

    public Network(String networkId) {
        this.networkId = networkId;
        this.keyValueStore = new HashMap<>();
        this.nodes = new HashSet<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    // Stocke la paire clé-valeur pour un noeud
    public void storeKeyValue(Node node, String key, String value) {
        keyValueStore.put(key, value);
    }

    // Récupère la valeur pour une clé donnée
    public String retrieveKeyValue(Node node, String key) {
        return keyValueStore.getOrDefault(key, "Valeur introuvable");
    }

    public String getNetworkId() {
        return networkId;
    }

    public String getKeyValueStore() {
        return keyValueStore.toString();
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    // Retourne une chaîne contenant l'ID et l'adresse IP de chaque noeud
    public String getNodesInfo() {
        StringBuilder info = new StringBuilder();
        for (Node node : nodes) {
            info.append("ID: ").append(node.getNodeId()).append(", IP: ").append(node.getIpAddress()).append("\n");
        }
        return info.toString();
    }
}