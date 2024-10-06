package analysis.visitors;

import analysis.InfoModel.ClassInfo;
import analysis.InfoModel.MethodInfo;
import analysis.InfoModel.MethodCallInfo;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class ClassVisitor extends ASTVisitor {
    private CompilationUnit compilationUnit;
    private int classCount = 0; // Compteur pour le nombre de classes
    private int methodCount = 0; // Compteur pour le nombre de méthodes
    private int fieldCount = 0; // Compteur pour le nombre d'attributs

    // Liste des informations de classes trouvées
    private List<ClassInfo> classesInfo = new ArrayList<>();

    public ClassVisitor(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        String className = node.getName().getFullyQualifiedName();
        List<MethodInfo> methods = new ArrayList<>();

        for (MethodDeclaration method : node.getMethods()) {
            String methodName = method.getName().getFullyQualifiedName();
            int parameterCount = method.parameters().size(); // Récupérer le nombre de paramètres

            // Analyser les appels de méthode dans cette méthode
            MethodCallVisitor methodCallVisitor = new MethodCallVisitor();
            method.accept(methodCallVisitor);
            List<MethodCallInfo> methodCalls = methodCallVisitor.getMethodCalls();

            int loc = countLinesOfCode(method);

            methods.add(new MethodInfo(methodName, methodCalls, parameterCount, loc)); // Passer le nombre de paramètres
            methodCount++;
        }
        int attributeCount = 0;
        for (FieldDeclaration field : node.getFields()) {
            attributeCount += field.fragments().size();
        }

        ClassInfo classInfo = new ClassInfo(className, methods, attributeCount);
        classesInfo.add(classInfo);

        classCount++;
        return super.visit(node);
    }

    // Nouvelle méthode pour compter le nombre de lignes de code dans une méthode
    private int countLinesOfCode(MethodDeclaration method) {
        // Compter les lignes de code dans le corps de la méthode
        if (method.getBody() != null) {
            // Compter chaque ligne non vide et non uniquement des espaces
            String bodySource = method.getBody().toString();
            String[] lines = bodySource.split("\n");
            int loc = 0;
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    loc++;
                }
            }
            return loc; // Retourner le nombre de lignes de code
        }
        return 0; // Si le corps de la méthode est vide
    }

    // Méthode pour récupérer le nombre total d'attributs
    public int getFieldCount() {
        return fieldCount;
    }

    public int getClassCount() {
        return classCount;
    }

    public int getMethodCount() {
        return methodCount;
    }

    // Méthode pour récupérer la liste des informations de classes trouvées
    public List<ClassInfo> getClassesInfo() {
        return classesInfo;
    }
}
