package analysis.InfoModel;

//classe modelisant les informations relatives a un appel de methode pour faciliter leur collecte via le visitor. On a simplement besoin d'attribut et de guetteurs

public class MethodCallInfo {
    private String calledMethodName;
    private String receiverType;

    public MethodCallInfo(String calledMethodName, String receiverType) {
        this.calledMethodName = calledMethodName;
        this.receiverType = receiverType;
    }

    public String getCalledMethodName() {
        return calledMethodName;
    }

    public String getReceiverType() {
        return receiverType;
    }

    @Override
    public String toString() {
        return "Called method: " + calledMethodName + " (Receiver type: " + receiverType + ")";
    }
}