package analysis.InfoModel;

import java.util.List;

//classe modelisant les informations relatives aux methodes pour faciliter leur collecte via le visitor. On a simplement besoin d'attribut et de guetteurs

public class MethodInfo {
    private String methodName;
    private List<MethodCallInfo> methodCalls;
    private int nbLoc; // Nombre de lignes de code
    private int parameterCount;

    public MethodInfo(String methodName, List<MethodCallInfo> methodCalls, int parameterCount, int loc) {
        this.methodName = methodName;
        this.methodCalls = methodCalls;
        this.parameterCount = parameterCount;
        this.nbLoc = loc; // Initialisation des lignes de code
    }

    public String getMethodName() {
        return methodName;
    }

    public List<MethodCallInfo> getMethodCalls() {
        return methodCalls;
    }

    public int getLoc() {
        return nbLoc; // Getter pour le nombre de lignes de code
    }

    public int getParameterCount() {
        return parameterCount;
    }

    @Override
    public String toString() {
        return "Method: " + methodName + ",  nombre de parametres: " + parameterCount + ", LOC number: " + nbLoc + ", Calls: " + methodCalls;
    }
}
