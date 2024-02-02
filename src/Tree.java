import java.util.Arrays;

public class Tree {
    int depth;
    int featureIndex;
    int threshold;
    Node root;
    public Tree(int featureIndex) {
        this.featureIndex = featureIndex;
    }

    public Tree(Node root) {
        this.root = root;
    }

    public int getDepth() {
        return depth;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }

    public void setFeatureIndex(int featureIndex) {
        this.featureIndex = featureIndex;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    // Calculate information gain for every child of the parent node
    public double informationGain(Node parentNode) {
        double[] weight = new double[parentNode.getChildrenNodes().size()];
        double sumEntropies = 0;
        double[] childValues;
        double gain;
        for (int i = 0; i < parentNode.getChildrenNodes().size(); i++) {
            childValues = parentNode.getChildrenNodes().get(i).getValue();
            weight[i] = calculateChildWeight(childValues, parentNode.getValue());
            sumEntropies += weight[i] * entropy(childValues);
        }
        if (sumEntropies == 0)
            System.out.println("this Node is leaf Node");
        gain = entropy(parentNode.getValue()) - sumEntropies;
        parentNode.setInfoGain(gain);
        return gain;
    }

    public double calculateChildWeight(double[] childValue, double[] parentValue) {
        return (double) childValue.length / parentValue.length;
    }

    //calculate entropy for a set of numbers
    public double entropy(double[] labels) {
        double[] classLabels = Arrays.stream(labels).distinct().toArray();
        double entropy = 0;

        for (double cls : classLabels) {
            double pCls = (double) Arrays.stream(labels).filter(value -> value == cls).count() / labels.length;
            entropy += -pCls * Math.log(pCls) / Math.log(2);
        }

        return entropy;
    }

//    public double giniIndex(double[] y) {
//        double[] classLabels = Arrays.stream(y).distinct().toArray();
//        double gini = 0;
//
//        for (double cls : classLabels) {
//            double pCls = (double) Arrays.stream(y).filter(value -> value == cls).count() / y.length;
//            gini += Math.pow(pCls, 2);
//        }
//
//        return 1 - gini;
//    }

}
