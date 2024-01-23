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

    //building tree based on calculating iGain and Entropy for each split
    public Node buildTree(double[][] dataset, int currDepth, int minSamplesSplit, int maxDepth) {
        split(dataset, 0, 0, new double[]{0, 1});

        double[][] X = new double[dataset.length][dataset[0].length - 1];
        double[] Y = new double[dataset.length];
        for (int i = 0; i < dataset.length; i++) {
            System.arraycopy(dataset[i], 0, X[i], 0, dataset[i].length - 1);
            Y[i] = dataset[i][dataset[i].length - 1];
        }
        int numSamples = X.length;
        int numFeatures = X[0].length + 1;
        if (numSamples >= minSamplesSplit && currDepth <= maxDepth) {
            Map<String, Object> bestSplit = getBestSplit(dataset, numSamples, numFeatures);

            if ((double) bestSplit.get("info_gain") > 0) {
                Node leftSubtree = buildTree((double[][]) bestSplit.get("dataset_left"), currDepth + 1,
                        minSamplesSplit, maxDepth);
                Node rightSubtree = buildTree((double[][]) bestSplit.get("dataset_right"), currDepth + 1,
                        minSamplesSplit, maxDepth);

                return new Node((int) bestSplit.get("threshold"), (double) bestSplit.get("info_gain"), (int) bestSplit.get("feature_index"));
            }
        }

        double[] leafValue = calculateLeafValue(Y);
        return new Node(leafValue);
        return null;
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

//            for (double threshold : possibleThresholds) {
            List<double[][]> splitResult = split(dataset, featureIndex, possibleThresholds);
//                double[][] datasetLeft = splitResult.get(0);
//                double[][] datasetRight = splitResult.get(1);

            double[][] dataset1 = splitResult.get(1);
            double[][] dataset2 = splitResult.get(2);
            double[][] dataset3 = splitResult.get(3);
            double[][] dataset4 = splitResult.get(4);
            double[][] dataset5 = splitResult.get(5);
            double[][] dataset6 = splitResult.get(6);
            double[][] dataset7 = splitResult.get(7);

            if (dataset1.length > 0) {
                double[] y = Arrays.copyOfRange(dataset[0], dataset[0].length - 1, numSamples);
                Node parent = new Node(y);

                nodeAdder(dataset1, parent);
                nodeAdder(dataset2, parent);
                nodeAdder(dataset3, parent);
                nodeAdder(dataset4, parent);
                nodeAdder(dataset5, parent);
                nodeAdder(dataset6, parent);
                nodeAdder(dataset7, parent);
                double currInfoGain = tree.informationGain(parent);

                if (currInfoGain > maxInfoGain) {
                    bestSplit.put("feature_index", featureIndex);
                    bestSplit.put("threshold", possibleThresholds);
                    bestSplit.put("dataset1", dataset1);
                    if (dataset2.length > 0)
                        bestSplit.put("dataset2", dataset2);
                    if (dataset3.length > 0)
                        bestSplit.put("dataset3", dataset3);
                    if (dataset4.length > 0)
                        bestSplit.put("dataset4", dataset4);
                    if (dataset5.length > 0)
                        bestSplit.put("dataset5", dataset5);
                    if (dataset6.length > 0)
                        bestSplit.put("dataset6", dataset6);
                    if (dataset7.length > 0)
                        bestSplit.put("dataset7", dataset7);
                    bestSplit.put("info_gain", currInfoGain);
                    maxInfoGain = currInfoGain;
                }
//                if (datasetLeft.length > 0 && datasetRight.length > 0) {
//                    double[] y = Arrays.copyOfRange(dataset[0], dataset[0].length - 1, numSamples);
//                    double[] leftY = Arrays.copyOfRange(datasetLeft[0], datasetLeft[0].length - 1, datasetLeft.length);
//                    double[] rightY = Arrays.copyOfRange(datasetRight[0], datasetRight[0].length - 1, datasetRight.length);
//
//                    Node parent = new Node(y);
//                    Node leftNode = new Node(leftY);
//                    Node rightNode = new Node(rightY);
//                    parent.addChild(leftNode);
//                    parent.addChild(rightNode);
//                    double currInfoGain = tree.informationGain(parent);
//
//                    if (currInfoGain > maxInfoGain) {
//                        bestSplit.put("feature_index", featureIndex);
//                        bestSplit.put("threshold", threshold);
//                        bestSplit.put("dataset_left", datasetLeft);
//                        bestSplit.put("dataset_right", datasetRight);
//                        bestSplit.put("info_gain", currInfoGain);
//                        maxInfoGain = currInfoGain;
//                    }
//                }
            }
        }

        return bestSplit;
    }

    private Boolean nodeAdder(double[][] dataset, Node parent) {
        if (dataset.length > 0) {
            double[] childNodeArray = Arrays.copyOfRange(dataset[0], dataset[0].length - 1, dataset.length);
            Node childNode = new Node(childNodeArray);
            parent.addChild(childNode);
            return true;
        }
        return false;
    }

    public List<double[][]> split(double[][] dataset, int featureIndex, double[] thresholdValues) {
//        List<double[]> datasetLeftList = new ArrayList<>();
//        List<double[]> datasetRightList = new ArrayList<>();

        List<double[]> datasetList1 = new ArrayList<>();
        List<double[]> datasetList2 = new ArrayList<>();
        List<double[]> datasetList3 = new ArrayList<>();
        List<double[]> datasetList4 = new ArrayList<>();
        List<double[]> datasetList5 = new ArrayList<>();
        List<double[]> datasetList6 = new ArrayList<>();
        List<double[]> datasetList7 = new ArrayList<>();

        for (double[] row : dataset) {
//            if (row[featureIndex] <= threshold) {
//                datasetLeftList.add(row);
//            } else {
//                datasetRightList.add(row);
//            }

            if (row[featureIndex] <= thresholdValues[0])
                datasetList1.add(row);
            else if (row[featureIndex] > thresholdValues[0] && row[featureIndex] <= thresholdValues[1])
                datasetList2.add(row);
            else if (row[featureIndex] > thresholdValues[1] && row[featureIndex] <= thresholdValues[2])
                datasetList3.add(row);
            else if (row[featureIndex] > thresholdValues[2] && row[featureIndex] <= thresholdValues[3])
                datasetList4.add(row);
            else if (row[featureIndex] > thresholdValues[3] && row[featureIndex] <= thresholdValues[4])
                datasetList5.add(row);
            else if (row[featureIndex] > thresholdValues[4] && row[featureIndex] <= thresholdValues[5])
                datasetList6.add(row);
            else if (row[featureIndex] > thresholdValues[5] && row[featureIndex] <= thresholdValues[6])
                datasetList7.add(row);
        }

//        double[][] datasetLeft = new double[datasetLeftList.size()][];
//        double[][] datasetRight = new double[datasetRightList.size()][];
        //test for correct spliting dataset
//        System.out.println("datasetList2 values = ");
//        for (int j = 0; j < datasetList2.size(); j++) {
//            for (int i = 0; i < 17; i++) {
//                System.out.print(datasetList2.get(j)[i] + " ");
//            }
//            System.out.println();
//        }

        double[][] dataset1 = new double[datasetList1.size()][];
        double[][] dataset2 = new double[datasetList2.size()][];
        double[][] dataset3 = new double[datasetList3.size()][];
        double[][] dataset4 = new double[datasetList4.size()][];
        double[][] dataset5 = new double[datasetList5.size()][];
        double[][] dataset6 = new double[datasetList6.size()][];
        double[][] dataset7 = new double[datasetList7.size()][];

        List<double[][]> result = new ArrayList<>();
//        result.add(0, datasetLeft);
//        result.add(1, datasetRight);

        result.add(0, dataset1);
        result.add(1, dataset2);
        result.add(2, dataset3);
        result.add(3, dataset4);
        result.add(4, dataset5);
        result.add(5, dataset6);
        result.add(6, dataset7);
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
