package analysis.visitors;

import analysis.InfoModel.ClassInfo;
import analysis.InfoModel.MethodInfo;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

// Visiteur permettant de collecter des informations sur une classes, ses méthodes et attributs
public class ClassVisitor extends ASTVisitor {
    private CompilationUnit compilationUnit;
    //compteurs:
    private int classCount = 0;
    private int methodCount = 0;
    private int fieldCount = 0;

    // On fait une Liste de Classinfo pour stocker les données de chaque classes
    private List<ClassInfo> classesInfo = new ArrayList<>();

    public ClassVisitor(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    // Visite chaque déclaration de type (classe)
    @Override
    public boolean visit(TypeDeclaration node) {
        String className = node.getName().getFullyQualifiedName();

        //parcours des methodes
        List<MethodInfo> methods = new ArrayList<>();

        for (MethodDeclaration method : node.getMethods()) {

            String methodName = method.getName().getFullyQualifiedName();//nom complet
            int parameterCount = method.parameters().size(); //nombre de paramètres
            int loc = countLinesOfCode(method); //nombre de lignes de code

            methods.add(new MethodInfo(methodName, parameterCount, loc));
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

    // méthode utilitaire  pour compter le nombre de lignes de code dans une méthode
    private int countLinesOfCode(MethodDeclaration method) {
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
            return loc;
        }
        return 0;
    }

    // + des guetteurs

    public int getFieldCount() {
        return fieldCount;
    }

    public int getClassCount() {
        return classCount;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public List<ClassInfo> getClassesInfo() {
        return classesInfo;
    }
}
