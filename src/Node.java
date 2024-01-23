import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node {

    private Integer featureIndex;
    private Double threshold;
    private Node left;
    private Node right;
    private Double infoGain;
    private int value;
    private List<String> children;

    public Node(Integer featureIndex, Double threshold, Node left, Node right, Double infoGain, int value, Map<String, Object> dictionary) {
        this.featureIndex = featureIndex;
        this.threshold = threshold;
        this.left = left;
        this.right = right;
        this.infoGain = infoGain;
        this.value = -1;
        this.children = new ArrayList<>();
        this.genChildren(dictionary);
        this.setValue(value);
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public void setValue(int val) {
        this.value = val;
    }

    private void genChildren(Map<String, Object> dictionary) {
        if (dictionary instanceof Map) {
            this.children = new ArrayList<>(dictionary.keySet());
        }
    }
}
