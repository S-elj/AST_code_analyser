package analysis.InfoModel;

import java.util.List;

//classe modelisant les informations d'une classes pour faciliter leur collecte via le visitor. On a simplement besoin d'attribut et de guetteurs

public class ClassInfo {
    private String className;
    private List<MethodInfo> methods;
    private int attributeCount;  // Stocker uniquement le nombre d'attributs

    public ClassInfo(String className, List<MethodInfo> methods, int attributeCount) {
        this.className = className;
        this.methods = methods;
        this.attributeCount = attributeCount;
    }

    public String getClassName() {
        return className;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public int getAttributeCount() {
        return attributeCount;
    }

    @Override
    public String toString() {
        return "Class: " + className + ", Methods: " + methods + ", Attribute count: " + attributeCount;
    }
}
