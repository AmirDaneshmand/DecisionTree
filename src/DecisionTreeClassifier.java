//public class DecisionTreeClassifier {
//
//    private Node root;
//    private int minSamplesSplit;
//    private int maxDepth;
//
//    public DecisionTreeClassifier(int minSamplesSplit, int maxDepth) {
//        this.root = null;
//        this.minSamplesSplit = minSamplesSplit;
//        this.maxDepth = maxDepth;
//    }
//
//    public DecisionTreeClassifier(float [] [] data , float [] labels) {
//
//    }
//
//    public double[] predict(double[][] X) {
//        double[] predictions = new double[X.length];
//        for (int i = 0; i < X.length; i++) {
//            predictions[i] = makePrediction(X[i], root);
//        }
//        return predictions;
//    }
//
//    public double makePrediction(double[] x, Node tree) {
//        if (tree.getValue() != null) {
//            return tree.getValue();
//        }
//
//        double featureVal = x[tree.getFeatureIndex()];
//        if (featureVal <= tree.getThreshold()) {
//            return makePrediction(x, tree.getLeft());
//        } else {
//            return makePrediction(x, tree.getRight());
//        }
//    }
//
//    public void printTree(Node tree, String indent) {
//        if (tree.getValue() != null) {
//            System.out.println(tree.getValue());
//        } else {
//            System.out.println("X_" + tree.getFeatureIndex() + " <= " + tree.getThreshold() + " ? " + tree.getInfoGain());
//            System.out.print(indent + "left: ");
//            printTree(tree.getLeft(), indent + "  ");
//            System.out.print(indent + "right: ");
//            printTree(tree.getRight(), indent + "  ");
//        }
//    }
//
//    public void fit(double[][] X, double[] Y) {
//        double[][] dataset = new double[X.length][X[0].length + 1];
//        for (int i = 0; i < X.length; i++) {
//            System.arraycopy(X[i], 0, dataset[i], 0, X[i].length);
//            dataset[i][X[i].length] = Y[i];
//        }
//
//        root = buildTree(dataset, 0);
//    }
//
//    public float[] predictAll(float[][] data, int depth) {
//        return new float[0];
//    }
//
//    public float accuracy(int[] labels, int[] labels_predicted) {
//        return 0;
//    }
//}
