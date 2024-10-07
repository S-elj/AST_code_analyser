package analysis;

import analysis.InfoModel.MethodInfo;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


// Classe pour creer et gerer la cli  permettant à l'utilisateur d'acceder facilment aux informations de l'analyses
public class CLIHandler {

    private CodeAnalyzer analyzer;            // Instance de CodeAnalyzer pour analyser le projet
    private MetricsCalculator metricsCalculator; // Instance de MetricsCalculator pour calculer les métriques

    public CLIHandler(String projectSourcePath) throws IOException {
        this.analyzer = new CodeAnalyzer(projectSourcePath);
        this.analyzer.analyze();
        this.metricsCalculator = new MetricsCalculator();

    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("Bienvenue dans l'analyseur de code ! Tapez 'exit' pour quitter.");

        while (true) {
            System.out.print("Choisissez une option : count / avg / top / all / call-graph \n");
            command = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(command)) {
                System.out.println("Au revoir !");
                break;
            }

            switch (command.toLowerCase()) {
                case "count":
                    handleCountOptions(scanner);
                    break;
                case "avg":
                    handleAvgOptions(scanner);
                    break;
                case "top":
                    handleTopOptions(scanner);
                    break;
                case "all":
                    displayAllMetrics();
                    break;
                case "call-graph":
                    System.out.println("Generation du graphe d'appel");
                    generateCallGraph();
                    break;

                default:
                    System.out.println("Commande inconnue. Essayez : count, avg, top, all, exit");
                    break;
            }
        }

        scanner.close();
    }


    private void handleCountOptions(Scanner scanner) {
        String choice;
        while (true) {
            System.out.print("Compter : classes / loc / methods / attributes / all \n(ou 'back' pour revenir) > ");
            choice = scanner.nextLine().trim();

            if ("back".equalsIgnoreCase(choice)) {
                return; // Retour au menu principal
            }

            switch (choice.toLowerCase()) {
                case "classes":
                    System.out.println("Nombre total de classes : " + metricsCalculator.getTotalClasses(analyzer.getClassesInfo()));
                    break;
                case "loc":
                    System.out.println("Nombre total de lignes de code : " + analyzer.getLineCount());
                    break;
                case "methods":
                    System.out.println("Nombre total de méthodes : " + metricsCalculator.getTotalMethods(analyzer.getClassesInfo()));
                    break;
                case "attributes":
                    System.out.println("Nombre total d'attributs : " + metricsCalculator.getTotalAttributes(analyzer.getClassesInfo()));
                    break;
                case "all":
                    System.out.println("Nombre total de classes : " + metricsCalculator.getTotalClasses(analyzer.getClassesInfo()));
                    System.out.println("Nombre total de lignes de code : " + analyzer.getLineCount());
                    System.out.println("Nombre total de méthodes : " + metricsCalculator.getTotalMethods(analyzer.getClassesInfo()));
                    System.out.println("Nombre total d'attributs : " + metricsCalculator.getTotalAttributes(analyzer.getClassesInfo()));
                    break;
                default:
                    System.out.println("Option inconnue. Essayez : classes, methods, attributes, all");
                    break;
            }
        }
    }

    private void handleAvgOptions(Scanner scanner) {
        String choice;
        while (true) {
            System.out.print("Moyenne : methods / attributes / all\n(ou 'back' pour revenir) > ");
            choice = scanner.nextLine().trim();

            if ("back".equalsIgnoreCase(choice)) {
                return; // Retour au menu principal
            }

            switch (choice.toLowerCase()) {
                case "methods":
                    System.out.println("Nombre moyen de méthodes par classe : " + metricsCalculator.getAvgMethodsPerClass(analyzer.getClassesInfo()));
                    break;
                case "attributes":
                    System.out.println("Nombre moyen d'attributs par classe : " + metricsCalculator.getAvgAttributesPerClass(analyzer.getClassesInfo()));
                    break;
                case "all":
                    System.out.println("Nombre moyen de méthodes par classe : " + metricsCalculator.getAvgMethodsPerClass(analyzer.getClassesInfo()));
                    System.out.println("Nombre moyen d'attributs par classe : " + metricsCalculator.getAvgAttributesPerClass(analyzer.getClassesInfo()));
                    break;
                default:
                    System.out.println("Option inconnue. Essayez : methods, attributes");
                    break;
            }
        }
    }

    private void handleTopOptions(Scanner scanner) {
        String choice;
        while (true) {
            System.out.print("Top : methods / attributes / cross / more_than_x / methods_loc / max-params \n(ou 'back' pour revenir) > ");
            choice = scanner.nextLine().trim();

            if ("back".equalsIgnoreCase(choice)) {
                return; // Retour au menu principal
            }

            switch (choice.toLowerCase()) {
                case "methods":
                    System.out.println("\nLes 10% des classes avec le plus grand nombre de méthodes :");
                    metricsCalculator.getTop10PercentByMethods(analyzer.getClassesInfo()).forEach(
                            classInfo -> System.out.println("Classe : " + classInfo.getClassName() + ", Nombre de méthodes : " + classInfo.getMethods().size())
                    );
                    break;
                case "attributes":
                    System.out.println("\nLes 10% des classes avec le plus grand nombre d'attributs :");
                    metricsCalculator.getTop10PercentByAttributes(analyzer.getClassesInfo()).forEach(
                            classInfo -> System.out.println("Classe : " + classInfo.getClassName() + ", Nombre d'attributs : " + classInfo.getAttributeCount())
                    );
                    break;
                case "cross":
                    System.out.println("\nClasses présentes dans les deux catégories :");
                    metricsCalculator.getIntersection(
                            metricsCalculator.getTop10PercentByMethods(analyzer.getClassesInfo()),
                            metricsCalculator.getTop10PercentByAttributes(analyzer.getClassesInfo())
                    ).forEach(classInfo -> System.out.println("Classe : " + classInfo.getClassName()));
                    break;

                case "more_than_x":
                    System.out.print("Entrez la valeur de X (nombre minimal de méthodes) : ");
                    int x = Integer.parseInt(scanner.nextLine().trim());
                    System.out.println("\nLes classes qui possèdent plus de " + x + " méthodes :");
                    metricsCalculator.getClassesWithMoreThanXMethods(analyzer.getClassesInfo(), x).forEach(
                            classInfo -> System.out.println("Classe : " + classInfo.getClassName() + ", Nombre de méthodes : " + classInfo.getMethods().size())
                    );
                    break;

                case "methods_loc":
                    System.out.println("\nLes 10% des méthodes avec le plus grand nombre de lignes de code (par classe) :");
                    Map<String, List<MethodInfo>> topMethodsPerClass = metricsCalculator.getTop10PercentByLocPerClass(analyzer.getClassesInfo());
                    for (Map.Entry<String, List<MethodInfo>> entry : topMethodsPerClass.entrySet()) {
                        System.out.println("\nClasse : " + entry.getKey());
                        entry.getValue().forEach(methodInfo ->
                                System.out.println("    Méthode : " + methodInfo.getMethodName() + ", nombre de lignes : " + methodInfo.getLoc())
                        );
                    }
                    break;
                case "max-params":
                    Object[] maxParamInfo = metricsCalculator.getMaxParameters(analyzer.getClassesInfo());
                    if (maxParamInfo[0] != null) {
                        System.out.println("Methode avec le nombre maximal de paramètres : ");
                        System.out.println("Classe : " + maxParamInfo[0] + ", Méthode : " + maxParamInfo[1] + ", nombre de paramètres : " + maxParamInfo[2]);
                    } else {
                        System.out.println("Aucune méthode trouvée.");
                    }
                    break;
                default:
                    System.out.println("Option inconnue. Essayez : methods, attributes, cross");
                    break;
            }
        }
    }

    private void displayAllMetrics() {
        System.out.println("\nInformations complètes sur l'application :");
        System.out.println("Nombre total de classes : " + metricsCalculator.getTotalClasses(analyzer.getClassesInfo()));
        System.out.println("Nombre total de lignes de code : " + analyzer.getLineCount());
        System.out.println("Nombre total de méthodes : " + metricsCalculator.getTotalMethods(analyzer.getClassesInfo()));
        System.out.println("Nombre total d'attributs : " + metricsCalculator.getTotalAttributes(analyzer.getClassesInfo()));
        System.out.println("Nombre moyen de méthodes par classe : " + metricsCalculator.getAvgMethodsPerClass(analyzer.getClassesInfo()));
        System.out.println("Nombre moyen d'attributs par classe : " + metricsCalculator.getAvgAttributesPerClass(analyzer.getClassesInfo()));

        System.out.println("\nLes 10% des classes avec le plus grand nombre de méthodes :");
        metricsCalculator.getTop10PercentByMethods(analyzer.getClassesInfo()).forEach(
                classInfo -> System.out.println("Classe : " + classInfo.getClassName() + ", Nombre de méthodes : " + classInfo.getMethods().size())
        );

        System.out.println("\nLes 10% des classes avec le plus grand nombre d'attributs :");
        metricsCalculator.getTop10PercentByAttributes(analyzer.getClassesInfo()).forEach(
                classInfo -> System.out.println("Classe : " + classInfo.getClassName() + ", Nombre d'attributs : " + classInfo.getAttributeCount())
        );

        System.out.println("\nClasses présentes dans les deux catégories :");
        metricsCalculator.getIntersection(
                metricsCalculator.getTop10PercentByMethods(analyzer.getClassesInfo()),
                metricsCalculator.getTop10PercentByAttributes(analyzer.getClassesInfo())
        ).forEach(classInfo -> System.out.println("Classe : " + classInfo.getClassName()));

        System.out.println("\nLes 10% des méthodes avec le plus grand nombre de lignes de code (par classe) :");
        Map<String, List<MethodInfo>> topMethodsPerClass = metricsCalculator.getTop10PercentByLocPerClass(analyzer.getClassesInfo());
        for (Map.Entry<String, List<MethodInfo>> entry : topMethodsPerClass.entrySet()) {
            System.out.println("\nClasse : " + entry.getKey());
            entry.getValue().forEach(methodInfo ->
                    System.out.println("    Méthode : " + methodInfo.getMethodName() + ", nombre de lignes : " + methodInfo.getLoc())
            );
        }
        System.out.println("Methode avec le nombre maximal de paramètres : ");
        Object[] maxParamInfo = metricsCalculator.getMaxParameters(analyzer.getClassesInfo());
        if (maxParamInfo[0] != null) {
            System.out.println("Classe : " + maxParamInfo[0] + ", Méthode : " + maxParamInfo[1] + ", nombre de paramètres : " + maxParamInfo[2]);
        } else {
            System.out.println("Aucune méthode trouvée.");
        }

        System.out.println("\n");

    }

    private void generateCallGraph() throws IOException {
        // Créer le graphe d'appel
        CallGraph callGraph = new CallGraph();

        // Analyser le projet et construire le graphe
        analyzer.buildCallGraph(callGraph); // Passez le graphe à l'analyseur

        callGraph.printGraph();

        String outputDir = "output";
        File dir = new File(outputDir);

        // Créer le dossier s'il n'existe pas
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Exporter le graphe dans le fichier .dot
        String dotFilePath = outputDir + "/callgraph.dot";
        callGraph.exportToDot(dotFilePath);
        System.out.println("Le fichier .dot a été exporté vers : " + dotFilePath);


        // Convertir le fichier .dot en fichier .svg en utilisant Graphviz
        String svgFilePath = outputDir + "/callgraph.svg";
        convertDotToSvg(dotFilePath, svgFilePath);
        System.out.println("Le fichier .svg a été exporté vers : " + svgFilePath);


        // Ouvrir le fichier SVG dans le navigateur
        openSvgInBrowser(svgFilePath);
    }

    private static void convertDotToSvg(String dotFilePath, String svgFilePath) throws IOException {
        // Commande pour convertir le fichier DOT en SVG
        String[] command = {"dot", "-Tsvg", dotFilePath, "-o", svgFilePath};
        Process process = Runtime.getRuntime().exec(command);

        try {
            process.waitFor();  // Attendre la fin du processus
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (process.exitValue() == 0) {
            System.out.println("Le fichier SVG a été généré avec succès : " + svgFilePath);
        } else {
            System.err.println("Erreur lors de la génération du fichier SVG.");
        }
    }

    // Méthode pour ouvrir le fichier SVG dans le navigateur
    private static void openSvgInBrowser(String svgFilePath) throws IOException {
        File svgFile = new File(svgFilePath);

        // Vérifier si le bureau est pris en charge pour ouvrir le fichier
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();

            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                // Ouvrir le fichier SVG dans le navigateur par défaut
                desktop.browse(svgFile.toURI());
                System.out.println("Le fichier SVG a été ouvert dans le navigateur.");
            } else {
                System.err.println("Ouvrir dans le navigateur n'est pas pris en charge.");
            }
        } else {
            System.err.println("Desktop n'est pas pris en charge sur ce système.");
        }
    }


    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java CLIHandler <path_to_project_source>");
            System.exit(1);
        }

        try {
            CLIHandler cliHandler = new CLIHandler(args[0]);
            cliHandler.start();
        } catch (IOException e) {
            System.err.println("Erreur lors de l'analyse : " + e.getMessage());
            System.exit(1);
        }
    }
}
