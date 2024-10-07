package analysis.visitors;

import analysis.CallGraph;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

// Visiteur dédié à la construction du graphe d'appels de méthodes
public class GraphVisitor extends ASTVisitor {
    private CallGraph callGraph;

    public GraphVisitor(CallGraph callGraph) {
        this.callGraph = callGraph;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        String className = node.getName().getFullyQualifiedName();
        for (MethodDeclaration method : node.getMethods()) {
            String methodName = className + "." + method.getName().getFullyQualifiedName();
            callGraph.addNode(methodName);

            // Analyser les appels de méthode a l'aide d'un visiteur dédié
            MethodCallVisitor methodCallVisitor = new MethodCallVisitor(callGraph, methodName);
            method.accept(methodCallVisitor);
        }
        return super.visit(node);
    }


}