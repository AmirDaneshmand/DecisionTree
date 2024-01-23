import java.util.*;

public class DecisionTreeClassifier {

    private Tree tree;
    private int minSamplesSplit;
    private int maxDepth;
    private double[][] data;
    private double[] labels;

    public DecisionTreeClassifier(Tree tree, double[][] data, double[] labels) {
        this.tree = tree;
        this.data = data;
        this.labels = labels;
    }

//    public double[] predict(double[][] X) {
//        double[] predictions = new double[X.length];
//        for (int i = 0; i < X.length; i++) {
//            predictions[i] = makePrediction(X[i], tree.root);
//        }
//        return predictions;
//    }
//
//    public double makePrediction(double[] x, Tree tree) {
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

    public Node buildTree(double[][] dataset, int currDepth, int minSamplesSplit, int maxDepth) {
        double[][] X = new double[dataset.length][dataset[0].length - 1];
        double[] Y = new double[dataset.length];
        for (int i = 0; i < dataset.length; i++) {
            System.arraycopy(dataset[i], 0, X[i], 0, dataset[i].length - 1);
            Y[i] = dataset[i][dataset[i].length - 1];
        }

        int numSamples = X.length;
        int numFeatures = X[0].length;

        if (numSamples >= minSamplesSplit && currDepth <= maxDepth) {
            Map<String, Object> bestSplit = getBestSplit(dataset, numSamples, numFeatures);

            if ((double) bestSplit.get("info_gain") > 0) {
                Node leftSubtree = buildTree((double[][]) bestSplit.get("dataset_left"), currDepth + 1,
                        minSamplesSplit, maxDepth);
                Node rightSubtree = buildTree((double[][]) bestSplit.get("dataset_right"), currDepth + 1,
                        minSamplesSplit, maxDepth);

                return new Node((int) bestSplit.get("threshold"), (double) bestSplit.get("info_gain"), (int) bestSplit.get("feature_index"));
//                return new Node(
//                        (int) bestSplit.get("feature_index"),
//                        (double) bestSplit.get("threshold"),
//                        leftSubtree,
//                        rightSubtree,
//                        (double) bestSplit.get("info_gain")
//                );
            }
        }

        double[] leafValue = calculateLeafValue(Y);
        return new Node(leafValue);
    }

    public Map<String, Object> getBestSplit(double[][] dataset, int numSamples, int numFeatures) {
        Map<String, Object> bestSplit = new HashMap<>();
        double maxInfoGain = Double.NEGATIVE_INFINITY;

        for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++) {
            double[] featureValues = new double[numSamples];
            for (int i = 0; i < numSamples; i++) {
                featureValues[i] = dataset[i][featureIndex];
            }

            double[] possibleThresholds = Arrays.stream(featureValues).distinct().toArray();

            for (double threshold : possibleThresholds) {
                List<double[][]> splitResult = split(dataset, featureIndex, threshold);
                double[][] datasetLeft = splitResult.get(0);
                double[][] datasetRight = splitResult.get(1);

                if (datasetLeft.length > 0 && datasetRight.length > 0) {
                    double[] y = Arrays.copyOfRange(dataset[0], dataset[0].length - 1, numSamples);
                    double[] leftY = Arrays.copyOfRange(datasetLeft[0], datasetLeft[0].length - 1, datasetLeft.length);
                    double[] rightY = Arrays.copyOfRange(datasetRight[0], datasetRight[0].length - 1, datasetRight.length);

                    Node parent = new Node(y);
                    Node leftNode = new Node(leftY);
                    Node rightNode = new Node(rightY);
                    parent.addChild(leftNode);
                    parent.addChild(rightNode);
                    double currInfoGain = tree.informationGain(parent);

                    if (currInfoGain > maxInfoGain) {
                        bestSplit.put("feature_index", featureIndex);
                        bestSplit.put("threshold", threshold);
                        bestSplit.put("dataset_left", datasetLeft);
                        bestSplit.put("dataset_right", datasetRight);
                        bestSplit.put("info_gain", currInfoGain);
                        maxInfoGain = currInfoGain;
                    }
                }
            }
        }

        return bestSplit;
    }

    public List<double[][]> split(double[][] dataset, int featureIndex, double threshold) {
        List<double[]> datasetLeftList = new ArrayList<>();
        List<double[]> datasetRightList = new ArrayList<>();

        for (double[] row : dataset) {
            if (row[featureIndex] <= threshold) {
                datasetLeftList.add(row);
            } else {
                datasetRightList.add(row);
            }
        }

        double[][] datasetLeft = new double[datasetLeftList.size()][];
        double[][] datasetRight = new double[datasetRightList.size()][];

        List<double[][]> result = new ArrayList<>();
        result.add(0, datasetLeft);
        result.add(1, datasetRight);
        return result;
    }

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

//    public void fit(double[][] X, double[] Y) {
//        double[][] dataset = new double[X.length][X[0].length + 1];
//        for (int i = 0; i < X.length; i++) {
//            System.arraycopy(X[i], 0, dataset[i], 0, X[i].length);
//            dataset[i][X[i].length] = Y[i];
//        }
//
//        root = buildTree(dataset, 0);
//    }

    //    public float[] predictAll(float[][] data, int depth) {
//        return new float[0];
//    }
    public double[] calculateLeafValue(double[] Y) {
        Map<Double, Integer> counts = new HashMap<>();
        for (double value : Y) {
            counts.put(value, counts.getOrDefault(value, 0) + 1);
        }

        double maxCount = Double.NEGATIVE_INFINITY;
        double leafValue = 0;

        for (Map.Entry<Double, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                leafValue = entry.getKey();
            }
        }

        return new double[]{leafValue};
    }

    // compare correctLabels and predictedLabels and calculate the accuracy of algorithm
    public double accuracyScore(double[] correctLabels, double[] predictedLabels) {
        int correctPredicted = 0;
        for (int i = 0; i < correctLabels.length; i++) {
            if (correctLabels[i] == predictedLabels[i])
                correctPredicted++;
        }
        return (double) correctPredicted / correctLabels.length * 100.0;
    }
}
