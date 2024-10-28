package synapse;

import java.util.HashSet;
import java.util.Set;

public class RequestTag {
    private static long counter = 0;
    private final long tagId;
    private final Set<Node> processedNodes; // Noeuds déjà visités

    public RequestTag() {
        this.tagId = counter++;
        this.processedNodes = new HashSet<>();
    }

    public long getTagId() { return tagId; }

    // Ajoute un noeud à la liste des noeuds visités
    public void addProcessedNode(Node node) {
        processedNodes.add(node);
    }

    // Vérifie si le noeud a déjà traité cette requête
    public boolean isNodeProcessed(Node node) {
        return processedNodes.contains(node);
    }
}

