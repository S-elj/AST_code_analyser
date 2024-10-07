package analysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CallGraph {
    // Ensemble des nœuds (méthodes)
    private Set<String> nodes = new HashSet<>();

    // Ensemble des arcs (appel entre deux méthodes)
    private Set<String> edges = new HashSet<>();

    // Ajouter un nœud (méthode)
    public void addNode(String method) {
        nodes.add(method);
    }

    // Ajouter un arc (appel de méthode)
    public void addEdge(String caller, String callee) {
        nodes.add(caller);  // Assurez-vous que le nœud existe
        nodes.add(callee);  // Assurez-vous que la méthode appelée existe
        edges.add(caller + " -> " + callee); // Représentation de l'arc
    }

    // Méthode pour afficher le graphe
    public void printGraph() {
        // Affichage des noeuds
        System.out.println("=== Call Graph ===");

        if (nodes.isEmpty()) {
            System.out.println("No nodes (methods) in the graph.");
        } else {
            System.out.println("Nodes (Methods):");
            for (String node : nodes) {
                System.out.println("  - " + node); // Ajout d'un tiret et d'une indentation
            }
        }

        // Séparateur entre les noeuds et les arcs
        System.out.println("\n------------------\n");

        // Affichage des arcs
        if (edges.isEmpty()) {
            System.out.println("No edges (method calls) in the graph.");
        } else {
            System.out.println("Edges (Method Calls):");
            for (String edge : edges) {
                System.out.println("  - " + edge);
            }
        }

        System.out.println("==================\n\n");
    }


    public void exportToDot(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write("digraph CallGraph {\n");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String caller = "\"" + parts[0] + "\"";
            String callee = "\"" + parts[1] + "\"";
            writer.write("  " + caller + " -> " + callee + ";\n");
        }

        writer.write("}\n");
        writer.close();
    }

}
