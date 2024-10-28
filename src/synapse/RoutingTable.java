package synapse;

import java.util.HashMap;
import java.util.Map;

public class RoutingTable {
    private final Map<String, Node> routes; // Stocke les routes par clé
    private final int defaultTTL;

    public RoutingTable(int defaultTTL) {
        this.routes = new HashMap<>();
        this.defaultTTL = defaultTTL;
    }

    // Ajoute une route vers un autre noeud pour une clé spécifique
    public void addRoute(String key, Node node) {
        routes.put(key, node);
    }

    // Récupère la route pour une clé donnée
    public Node getRoute(String key) {
        return routes.get(key);
    }

    // Initialise le TTL pour un nouveau message
    public int initializeTTL() {
        return defaultTTL;
    }

    // Méthode de gestion de routage inter-overlay (simplifiée)
    public Node routeToNextHop(String key, Node currentNode) {
        return routes.getOrDefault(key, currentNode);
    }
}
