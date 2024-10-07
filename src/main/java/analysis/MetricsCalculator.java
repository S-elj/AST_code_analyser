package analysis;

import analysis.InfoModel.ClassInfo;
import analysis.InfoModel.MethodInfo;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Classe utilitaire pour calculer diverses métriques sur les classes et méthodes
public class MetricsCalculator {

    // Récupère le nombre total de classes
    public int getTotalClasses(List<ClassInfo> classInfos) {
        return classInfos.size();
    }

    // nombre total de méthodes
    public int getTotalMethods(List<ClassInfo> classInfos) {
        return classInfos.stream().mapToInt(c -> c.getMethods().size()).sum();
    }

    //nombre moyen de méthodes par classe
    public double getAvgMethodsPerClass(List<ClassInfo> classInfos) {
        return classInfos.isEmpty() ? 0 : (double) getTotalMethods(classInfos) / classInfos.size();
    }

    // nombre total d'attributs
    public int getTotalAttributes(List<ClassInfo> classInfos) {
        return classInfos.stream().mapToInt(ClassInfo::getAttributeCount).sum();
    }

    // nombre moyen d'attributs par classe
    public double getAvgAttributesPerClass(List<ClassInfo> classInfos) {
        return classInfos.isEmpty() ? 0 : (double) getTotalAttributes(classInfos) / classInfos.size();
    }

    //  les 10% des classes avec le plus grand nombre de méthodes
    public List<ClassInfo> getTop10PercentByMethods(List<ClassInfo> classInfos) {
        int topPercentCount = (int) Math.ceil(classInfos.size() * 0.1);
        return classInfos.stream()
                .sorted(Comparator.comparingInt(c -> -c.getMethods().size()))
                .limit(topPercentCount)
                .collect(Collectors.toList());
    }

    //  les 10% des classes avec le plus grand nombre d'attributs
    public List<ClassInfo> getTop10PercentByAttributes(List<ClassInfo> classInfos) {
        int topPercentCount = (int) Math.ceil(classInfos.size() * 0.1);
        return classInfos.stream()
                .sorted(Comparator.comparingInt(c -> -c.getAttributeCount()))
                .limit(topPercentCount)
                .collect(Collectors.toList());
    }

    //  l'intersection de deux listes de classes
    public List<ClassInfo> getIntersection(List<ClassInfo> list1, List<ClassInfo> list2) {
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }

    //  les classes ayant plus de X méthodes
    public List<ClassInfo> getClassesWithMoreThanXMethods(List<ClassInfo> classInfos, int x) {
        return classInfos.stream()
                .filter(classInfo -> classInfo.getMethods().size() > x)
                .collect(Collectors.toList());
    }

    // Récupère les méthodes ayant le plus grand nombre de lignes de code (top 10%) par classe
    public Map<String, List<MethodInfo>> getTop10PercentByLocPerClass(List<ClassInfo> classesInfo) {
        Map<String, List<MethodInfo>> topMethodsPerClass = new HashMap<>();

        for (ClassInfo classInfo : classesInfo) {
            List<MethodInfo> methods = classInfo.getMethods();
            int count = methods.size();
            int threshold = (int) Math.ceil(count * 0.1); //seuil pour les 10%

            // Tri des méthodes par nombre de lignes de code
            List<MethodInfo> topMethods = methods.stream()
                    .sorted(Comparator.comparingInt(MethodInfo::getLoc).reversed())
                    .limit(threshold)
                    .collect(Collectors.toList());

            topMethodsPerClass.put(classInfo.getClassName(), topMethods);
        }

        return topMethodsPerClass;
    }

    //  méthode ayant le plus grand nombre de paramètres (tableau d'objet en type retour pour aussi renvoyer le nom de la classe et de la methode)
    public Object[] getMaxParameters(List<ClassInfo> classesInfo) {
        String className = null;
        String methodName = null;
        int maxParameters = 0;

        for (ClassInfo classInfo : classesInfo) {
            for (MethodInfo methodInfo : classInfo.getMethods()) {
                int parameterCount = methodInfo.getParameterCount();
                if (parameterCount > maxParameters) {
                    maxParameters = parameterCount;
                    className = classInfo.getClassName();
                    methodName = methodInfo.getMethodName();
                }
            }
        }

        return new Object[]{className, methodName, maxParameters};
    }
}