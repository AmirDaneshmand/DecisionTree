import java.util.ArrayList;
import java.util.List;

public class Node {

    private int featureIndex;
    private double threshold;
    private List<Node> childrenNodes = null;
    private double infoGain;
    private double[] value;
    private List<double[]> childrenList = null;

    public Node(double[] value) {
        this.value = value;
        this.childrenNodes = new ArrayList<>();
        this.childrenList = new ArrayList<>();
        this.genChildren(this.childrenNodes);
        this.setValue(value);
    }

    public Node(double threshold, double infoGain, double[] value) {
        this.threshold = threshold;
        this.infoGain = infoGain;
        this.value = value;
        this.childrenNodes = new ArrayList<>();
        this.childrenList = new ArrayList<>();
        this.genChildren(this.childrenNodes);
        this.setValue(value);
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public void setValue(double[] value) {
        System.arraycopy(value, 0, this.value, 0, value.length);
    }

    private void genChildren(List<Node> childrenNodes) {
        for (Node child : childrenNodes) {
            this.childrenList.add(child.getValue());
        }
    }

    public void addChild(Node child) {
        try {
            this.childrenNodes.add(child);
        } catch (RuntimeException exception) {
            throw new RuntimeException("couldn't add to childNodes List.");
        }
    }

    public double getInfoGain() {
        return infoGain;
    }

    public void setInfoGain(double infoGain) {
        this.infoGain = infoGain;
    }

    public double[] getValue() {
        return value;
    }

    public List<Node> getChildrenNodes() {
        return childrenNodes;
    }

    public void setChildrenNodes(List<Node> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }
}
