package analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import analysis.InfoModel.ClassInfo;
import analysis.visitors.ClassVisitor;
import analysis.visitors.GraphVisitor;
import analysis.visitors.LineVisitor;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/*Classe principale d'analyse de code qui parcourt les fichiers source d'un projet,crée un AST (Abstract Syntax Tree) pour chaque fichier,
et instancie les différents visiteurs pour collecter les informations nécessaires à l'analyse  ou generer un graph d'appel*/
public class CodeAnalyzer {

    private String projectSourcePath;

    // Compteurs
    private int classCount = 0;
    private int lineCount = 0;
    private int methodCount = 0;
    private Set<String> packages = new HashSet<>();  // Utiliser un Set pour éviter les doublons de packages

    // Liste des informations de classes
    private List<ClassInfo> classesInfo = new ArrayList<>();

    public CodeAnalyzer(String projectSourcePath) {
        this.projectSourcePath = projectSourcePath;
    }

    public void analyze() throws IOException {
        // Récupérer tous les fichiers .java
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

        // Parcourir chaque fichier pour le parser
        for (File fileEntry : javaFiles) {
            String content = FileUtils.readFileToString(fileEntry, "UTF-8");

            CompilationUnit parse = parse(content.toCharArray());

            //Visite de la classe
            ClassVisitor classVisitor = new ClassVisitor(parse);
            parse.accept(classVisitor);

            classCount += classVisitor.getClassCount();
            methodCount += classVisitor.getMethodCount();

            if (parse.getPackage() != null) {
                packages.add(parse.getPackage().getName().getFullyQualifiedName());
            }

            // Récupération des informations de classes analysées
            classesInfo.addAll(classVisitor.getClassesInfo());

            // Analyse des lignes de code
            LineVisitor lineVisitor = new LineVisitor(parse);
            parse.accept(lineVisitor);
            lineCount += lineVisitor.getLineCount();
        }
    }


    public void buildCallGraph(CallGraph callGraph) throws IOException {
        // Récupérer tous les fichiers .java
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

        // Parcourir chaque fichier .java
        for (File fileEntry : javaFiles) {
            String content = FileUtils.readFileToString(fileEntry, "UTF-8");

            // Créer l'AST pour le fichier courant
            CompilationUnit parse = parse(content.toCharArray());

            // Analyse des classes et ajout des informations d'appel au graphe
            GraphVisitor classVisitor = new GraphVisitor(callGraph);  // Passer le CallGraph
            parse.accept(classVisitor);

        }
    }


    // Fonction pour lister les fichiers .java
    private ArrayList<File> listJavaFilesForFolder(final File folder) {
        ArrayList<File> javaFiles = new ArrayList<File>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                javaFiles.addAll(listJavaFilesForFolder(fileEntry));
            } else if (fileEntry.getName().endsWith(".java")) {
                javaFiles.add(fileEntry);
            }
        }
        return javaFiles;
    }

    // Fonction pour créer l'AST à partir du contenu d'un fichier
    private CompilationUnit parse(char[] classSource) {
        ASTParser parser = ASTParser.newParser(AST.JLS4); // Utiliser JLS8 pour la compatibilité avec Java 8+

        // Activer la résolution des bindings pour pouvoir obtenir les types lors d'appel de methode
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);

        Map<?, ?> options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        String[] classpathEntries = {};
        String[] sourceFolders = {projectSourcePath}; //source du projet a analyser
        parser.setEnvironment(classpathEntries, sourceFolders, null, true);

        parser.setUnitName("");
        parser.setSource(classSource);

        return (CompilationUnit) parser.createAST(null);
    }

    // Fonction utilitaires et guetteurs

    public double getAverageMethodsPerClass() {
        if (classCount == 0) return 0;
        return (double) methodCount / classCount;
    }

    public double getAverageLinesPerMethod() {
        if (methodCount == 0) return 0;
        return (double) lineCount / methodCount;
    }

    public int getPackageCount() {
        return packages.size();
    }

    public int getClassCount() {
        return classCount;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public List<ClassInfo> getClassesInfo() {
        return classesInfo;
    }
}
