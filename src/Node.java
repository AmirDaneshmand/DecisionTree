import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {

    private int featureIndex;
    private double[] threshold;
    private List<Node> childrenNodes;
    private double infoGain;
    private double[] value;
    private double[][] value2;
    private List<double[]> childrenList = null;
    Boolean isLeaf;

    public Node(double[] value) {
        this.value = value;
        this.childrenNodes = new ArrayList<>();
        this.childrenList = new ArrayList<>();
        this.genChildren(this.childrenNodes);
        this.setValue(value);
    }

    public Node(double[] value, Boolean isLeaf) {
        this.childrenNodes = new ArrayList<>();
        this.value = value;
        this.isLeaf = isLeaf;
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

    public Node getChilrenByIndex(int index){
        int i = 0;
        for (Node child : this.childrenNodes) {
            if (i == index)
                return child;
            i++;
        }
        return null;
    }

    public void setChildrenNodes(List<Node> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }

    public List<double[]> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<double[]> childrenList) {
        this.childrenList = childrenList;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    @Override
    public String toString() {
        return "Node{" +
                "childrenNodes=" + childrenNodes +
                ", value=" + Arrays.toString(value) +
                '}';
    }
}
