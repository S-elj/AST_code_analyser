package analysis.InfoModel;


// classe pour representer les informations d'attributs, pas utilisés dans les faits puisqu'on se contente de les compter
public class AttributeInfo {
    private String attributeName;
    private String encapsulation;

    public AttributeInfo(String attributeName, String encapsulation) {
        this.attributeName = attributeName;
        this.encapsulation = encapsulation;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getEncapsulation() {
        return encapsulation;
    }

    @Override
    public String toString() {
        return attributeName + " (" + encapsulation + ")";
    }
}