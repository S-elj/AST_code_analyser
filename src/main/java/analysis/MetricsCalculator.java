package analysis;

import analysis.InfoModel.ClassInfo;
import analysis.InfoModel.MethodInfo;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetricsCalculator {

    public int getTotalClasses(List<ClassInfo> classInfos) {
        return classInfos.size();
    }


    public int getTotalMethods(List<ClassInfo> classInfos) {
        return classInfos.stream().mapToInt(c -> c.getMethods().size()).sum();
    }

    public double getAvgMethodsPerClass(List<ClassInfo> classInfos) {
        return classInfos.isEmpty() ? 0 : (double) getTotalMethods(classInfos) / classInfos.size();
    }

    public int getTotalAttributes(List<ClassInfo> classInfos) {
        return classInfos.stream().mapToInt(ClassInfo::getAttributeCount).sum();
    }

    public double getAvgAttributesPerClass(List<ClassInfo> classInfos) {
        return classInfos.isEmpty() ? 0 : (double) getTotalAttributes(classInfos) / classInfos.size();
    }

    public List<ClassInfo> getTop10PercentByMethods(List<ClassInfo> classInfos) {
        int topPercentCount = (int) Math.ceil(classInfos.size() * 0.1);
        return classInfos.stream()
                .sorted(Comparator.comparingInt(c -> -c.getMethods().size()))
                .limit(topPercentCount)
                .collect(Collectors.toList());
    }

    public List<ClassInfo> getTop10PercentByAttributes(List<ClassInfo> classInfos) {
        int topPercentCount = (int) Math.ceil(classInfos.size() * 0.1);
        return classInfos.stream()
                .sorted(Comparator.comparingInt(c -> -c.getAttributeCount()))
                .limit(topPercentCount)
                .collect(Collectors.toList());
    }

    public List<ClassInfo> getIntersection(List<ClassInfo> list1, List<ClassInfo> list2) {
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }


    public List<ClassInfo> getClassesWithMoreThanXMethods(List<ClassInfo> classInfos, int x) {
        return classInfos.stream()
                .filter(classInfo -> classInfo.getMethods().size() > x)
                .collect(Collectors.toList());
    }
    // MetricsCalculator.java

    public Map<String, List<MethodInfo>> getTop10PercentByLocPerClass(List<ClassInfo> classesInfo) {
        Map<String, List<MethodInfo>> topMethodsPerClass = new HashMap<>();

        for (ClassInfo classInfo : classesInfo) {
            List<MethodInfo> methods = classInfo.getMethods();
            // Calculer le seuil pour les 10%
            int count = methods.size();
            int threshold = (int) Math.ceil(count * 0.1); // 10% des méthodes

            // Trier les méthodes par le nombre de lignes de code
            List<MethodInfo> topMethods = methods.stream()
                    .sorted(Comparator.comparingInt(MethodInfo::getLoc).reversed())
                    .limit(threshold)
                    .collect(Collectors.toList());

            // Ajouter à la carte avec le nom de la classe
            topMethodsPerClass.put(classInfo.getClassName(), topMethods);
        }

        return topMethodsPerClass;
    }

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

        return new Object[]{className, methodName, maxParameters}; // Renvoie un tableau avec les informations
    }


}
