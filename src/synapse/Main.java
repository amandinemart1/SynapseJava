package synapse;

public class Main {
    public static void main(String[] args) {
        // Création des réseaux overlay
        Network network1 = new Network("Network1");
        Network network2 = new Network("Network2");
        Network network3 = new Network("Network3");

        // Création de noeuds Synapse avec une adresse IP et un TTL par défaut de 5
        Node nodeA = new Node("NodeA", "192.168.1.1", 5);
        Node nodeB = new Node("NodeB", "192.168.1.2", 5);
        Node nodeC = new Node("NodeC", "192.168.1.3", 5);

        // Ajout des noeuds aux réseaux
        nodeA.joinNetwork(network1);
        nodeA.joinNetwork(network2);
        
        nodeB.joinNetwork(network2);
        nodeB.joinNetwork(network3);

        nodeC.joinNetwork(network1);
        nodeC.joinNetwork(network3);

        // composition des réseaux
        System.out.println("Composition du réseaux 1 :");
        System.out.println(network1.getNodesInfo());

        System.out.println("Composition du réseaux 2 :");
        System.out.println(network2.getNodesInfo());

        System.out.println("Composition du réseaux 3 :");
        System.out.println(network3.getNodesInfo());

        // Newtork1 Key-Value Store: {}
        System.out.println("Network1 Key-Value Store: " + network1.getKeyValueStore());
        // Network2 Key-Value Store: {}
        System.out.println("Network2 Key-Value Store: " + network2.getKeyValueStore());
        // Network3 Key-Value Store: {}
        System.out.println("Network3 Key-Value Store: " + network3.getKeyValueStore());

        // Test de l'invitation de nodeC à rejoindre network2 par nodeA
        System.out.println("Test de l'invitation de nodeC à network2 :");
        nodeA.inviteToNetwork(nodeC, network2);
        System.out.println("Composition du réseaux 1 :");
        System.out.println(network1.getNodesInfo());

        System.out.println("Composition du réseaux 2 :");
        System.out.println(network2.getNodesInfo());

        System.out.println("Composition du réseaux 3 :");
        System.out.println(network3.getNodesInfo());


        // Test d'insertion d'une paire clé-valeur dans Network1 par nodeA
        System.out.println("\nTest d'insertion dans Network1 par nodeA :");
        nodeA.put("key1", "value1", network1);

        // Test de récupération de la valeur dans Network1 par nodeC
        System.out.println("\nTest de récupération dans Network1 par nodeC :");
        String retrievedValue = nodeC.get("key1", network1);
        System.out.println("Valeur récupérée pour 'key1' dans Network1 : " + (retrievedValue != null ? retrievedValue : "non trouvée"));

        // Test de routage inter-réseaux d'une requête GET depuis nodeB vers nodeA
        System.out.println("\nTest de routage inter-réseaux :");
        Message getMessage = new Message("GET", "key1", null, nodeB, nodeA, 5);
        nodeB.routeMessage(getMessage);

        // Test de mise à jour de la valeur pour 'key1' dans Network2 par nodeC
        System.out.println("\nTest de mise à jour de la clé 'key1' dans Network2 :");
        nodeC.put("key1", "updatedValue", network2);
        String updatedValue = nodeB.get("key1", network2);
        System.out.println("Valeur mise à jour pour 'key1' dans Network2 : " + (updatedValue != null ? updatedValue : "non trouvée"));

        // Test d'expiration du TTL en envoyant un message avec TTL = 1
        System.out.println("\nTest d'expiration du TTL :");
        Message expiringMessage = new Message("GET", "key1", null, nodeA, nodeC, 1); // TTL = 1
        nodeA.routeMessage(expiringMessage); 

        // Test de la fonction found : notification de la récupération d'une valeur
        System.out.println("\nTest de la fonction found (notification de récupération) :");
        nodeA.found("key1", "value1", nodeB);

        // Affichage final des données stockées dans les réseaux
        System.out.println("\nÉtat final des réseaux :");
        System.out.println("Network1 Key-Value Store: " + network1.getKeyValueStore());
        System.out.println("Network2 Key-Value Store: " + network2.getKeyValueStore());
        System.out.println("Network3 Key-Value Store: " + network3.getKeyValueStore());

        System.out.println("Composition du réseaux 1 :");
        System.out.println(network1.getNodesInfo());

        System.out.println("Composition du réseaux 2 :");
        System.out.println(network2.getNodesInfo());

        System.out.println("Composition du réseaux 3 :");
        System.out.println(network3.getNodesInfo());
    }
}
