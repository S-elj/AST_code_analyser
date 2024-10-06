package analysis;

import analysis.InfoModel.ClassInfo;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java Main <path_to_project>");
            return;
        }
        String projectSourcePath = args[0];

        CodeAnalyzer analyzer = new CodeAnalyzer(projectSourcePath);
        analyzer.analyze();

        // 1. Nombre de classes
        int totalClasses = analyzer.getClassCount();
        System.out.println("Nombre de classes : " + totalClasses);

        // 2. Nombre total de lignes de code
        int totalLines = analyzer.getLineCount();
        System.out.println("Nombre total de lignes de code : " + totalLines);

        // 3. Nombre total de méthodes
        int totalMethods = analyzer.getMethodCount();
        System.out.println("Nombre total de méthodes : " + totalMethods);

        // 4. Nombre total de packages (non calculé, à implémenter si nécessaire)
        // Si nécessaire, il faut ajouter un moyen de déterminer les packages

        // 5. Nombre moyen de méthodes par classe
        double avgMethodsPerClass = totalClasses > 0 ? (double) totalMethods / totalClasses : 0;
        System.out.println("Nombre moyen de méthodes par classe : " + avgMethodsPerClass);

        // 6. Nombre moyen de lignes de code par méthode
        double avgLinesPerMethod = totalMethods > 0 ? (double) totalLines / totalMethods : 0;
        System.out.println("Nombre moyen de lignes de code par méthode : " + avgLinesPerMethod);

        // 7. Nombre moyen d'attributs par classe
        List<ClassInfo> classInfos = analyzer.getClassesInfo();
        int totalAttributes = classInfos.stream().mapToInt(ClassInfo::getAttributeCount).sum();
        double avgAttributesPerClass = totalClasses > 0 ? (double) totalAttributes / totalClasses : 0;
        System.out.println("Nombre moyen d'attributs par classe : " + avgAttributesPerClass);

        // 8. Les 10% des classes qui possèdent le plus grand nombre de méthodes
        List<ClassInfo> top10PercentMethods = getTop10PercentByMethods(classInfos);
        System.out.println("\nLes 10% des classes avec le plus grand nombre de méthodes :");
        top10PercentMethods.forEach(classInfo ->
                System.out.println("Classe : " + classInfo.getClassName() + ", Nombre de méthodes : " + classInfo.getMethods().size()));

        // 9. Les 10% des classes qui possèdent le plus grand nombre d'attributs
        List<ClassInfo> top10PercentAttributes = getTop10PercentByAttributes(classInfos);
        System.out.println("\nLes 10% des classes avec le plus grand nombre d'attributs :");
        top10PercentAttributes.forEach(classInfo ->
                System.out.println("Classe : " + classInfo.getClassName() + ", Nombre d'attributs : " + classInfo.getAttributeCount()));

        // 10. Classes présentes dans les deux catégories
        List<ClassInfo> intersection = getIntersection(top10PercentMethods, top10PercentAttributes);
        System.out.println("\nClasses présentes dans les deux catégories :");
        intersection.forEach(classInfo ->
                System.out.println("Classe : " + classInfo.getClassName()));
    }

    // Fonction pour récupérer les 10% des classes avec le plus de méthodes
    private static List<ClassInfo> getTop10PercentByMethods(List<ClassInfo> classInfos) {
        int topPercentCount = (int) Math.ceil(classInfos.size() * 0.1);
        return classInfos.stream()
                .sorted(Comparator.comparingInt(c -> -c.getMethods().size()))  // Tri par nombre de méthodes (ordre décroissant)
                .limit(topPercentCount)
                .collect(Collectors.toList());
    }

    // Fonction pour récupérer les 10% des classes avec le plus d'attributs
    private static List<ClassInfo> getTop10PercentByAttributes(List<ClassInfo> classInfos) {
        int topPercentCount = (int) Math.ceil(classInfos.size() * 0.1);
        return classInfos.stream()
                .sorted(Comparator.comparingInt(c -> -c.getAttributeCount()))  // Tri par nombre d'attributs (ordre décroissant)
                .limit(topPercentCount)
                .collect(Collectors.toList());
    }

    // Fonction pour trouver les classes présentes dans les deux catégories
    private static List<ClassInfo> getIntersection(List<ClassInfo> list1, List<ClassInfo> list2) {
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }
}
