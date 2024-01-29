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
    public Node buildTree(double[][] dataset, int currDepth, int maxDepth) {
//        split(dataset, 0, 0, new double[]{0, 1});

        // Split the dataset
        Map<String, Object> bestSplit = getBestSplit(dataset, dataset.length, dataset[0].length);
        // Extract information from the best split
        int featureIndex = (int) bestSplit.get("feature_index");
        double[] thresholds = (double[]) bestSplit.get("threshold");
        double[][] dataset1 = (double[][]) bestSplit.get("dataset1");
        double[][] dataset2 = (double[][]) bestSplit.get("dataset2");
        double[][] dataset3 = (double[][]) bestSplit.get("dataset3");
        double[][] dataset4 = (double[][]) bestSplit.get("dataset4");
//        double[][] dataset5 = (double[][]) bestSplit.get("dataset5");
//        double[][] dataset6 = (double[][]) bestSplit.get("dataset6");
//        double[][] dataset7 = (double[][]) bestSplit.get("dataset7");
        double infoGain = (double) bestSplit.get("info_gain");

        // Check conditions for building subtrees
        if (infoGain > 0 && currDepth <= maxDepth) {
            Node SubTree1 = buildTree(dataset1 , currDepth + 1 , maxDepth);
            Node SubTree2 = buildTree(dataset2 , currDepth + 1 , maxDepth);
            Node SubTree3 = buildTree(dataset3 , currDepth + 1 , maxDepth);
            Node SubTree4 = buildTree(dataset4 , currDepth + 1 , maxDepth);
//            Node SubTree5 = buildTree(dataset5 , currDepth + 1 , maxDepth);
//            Node SubTree6 = buildTree(dataset6 , currDepth + 1 , maxDepth);
//            Node SubTree7 = buildTree(dataset7 , currDepth + 1 , maxDepth);

//            Node leftSubtree = buildTree(dataset2, currDepth + 1, minSamplesSplit, maxDepth);
//            Node rightSubtree = buildTree(dataset3, currDepth + 1, minSamplesSplit, maxDepth);

            // Return a non-leaf node
//            return new Node(thresholds, infoGain, featureIndex);
        } else {
            // Return a leaf node
            double[] X  = Arrays.copyOf(dataset[0] , dataset[0].length);
            double[] leafValue = calculateLeafValue(X);
            return new Node(leafValue);
        }

//        double[][] X = new double[dataset.length][dataset[0].length - 1];
//        double[] Y = new double[dataset.length];
//        for (int i = 0; i < dataset.length; i++) {
//            System.arraycopy(dataset[i], 0, X[i], 0, dataset[i].length - 1);
//            Y[i] = dataset[i][dataset[i].length - 1];
//        }
//        int numSamples = X.length;
//        int numFeatures = X[0].length + 1;
//        if (numSamples >= minSamplesSplit && currDepth <= maxDepth) {
//            Map<String, Object> bestSplit = getBestSplit(dataset, numSamples, numFeatures);
//
//            if ((double) bestSplit.get("info_gain") > 0) {
//                Node leftSubtree = buildTree((double[][]) bestSplit.get("dataset_left"), currDepth + 1,
//                        minSamplesSplit, maxDepth);
//                Node rightSubtree = buildTree((double[][]) bestSplit.get("dataset_right"), currDepth + 1,
//                        minSamplesSplit, maxDepth);
//
//                return new Node((int) bestSplit.get("threshold"), (double) bestSplit.get("info_gain"), (int) bestSplit.get("feature_index"));
//            }
//        }

//        double[] leafValue = calculateLeafValue(Y);
        return new Node((double[]) bestSplit.get("value"), (double) bestSplit.get("info_gain"), (int) bestSplit.get("threshold"));
    }

    //calculates Possible threshold and find the best split for a given dataset
    public Map<String, Object> getBestSplit(double[][] dataset, int numSamples, int numFeatures) {
        Map<String, Object> bestSplit = new HashMap<>();
        double maxInfoGain = Double.NEGATIVE_INFINITY;

        for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++) {
            double[] featureValues = new double[numSamples];
            for (int i = 0; i < numSamples; i++) {
                featureValues[i] = dataset[i][featureIndex];
            }

            double[] uniqueFeatureValues = new double[17];

            // Adding unique elements to uniqueFeatureValues
            for (int i = 0 ; i < featureValues.length ; i++) {
                uniqueFeatureValues = Arrays.stream(featureValues).distinct().toArray();
            }
            // Sort the uniqueFeatureValues array
            Arrays.sort(uniqueFeatureValues);
            double[] possibleThresholds = calculatethresholdvalues(uniqueFeatureValues);
            List<double[][]> splitResult = split(dataset, featureIndex, possibleThresholds);
            double[][] dataset1;
            if (splitResult.get(1) != null) {
                dataset1 = splitResult.get(1);
                Node parent = new Node(Arrays.copyOf(splitResult.get(1)[0], splitResult.get(1)[0].length));
                double[][] dataset2 = new double[numFeatures][];
                if (splitResult.get(2) != null) {
                    dataset2 = splitResult.get(2);
                    nodeAdder(splitResult.get(2), parent);
                }
                double[][] dataset3 = new double[numFeatures][];
                if (splitResult.get(3) != null) {
                    dataset3 = splitResult.get(3);
                    nodeAdder(splitResult.get(3), parent);
                }
                double[][] dataset4 = new double[numFeatures][];
                if (splitResult.get(4) != null) {
                    dataset4 = splitResult.get(4);
                    nodeAdder(splitResult.get(4), parent);
                }
//            double[][] dataset5 = splitResult.get(5);
//            double[][] dataset6 = splitResult.get(6);
//            double[][] dataset7 = splitResult.get(7);

//            if (splitResult.get(1) != null) {
//                double[] y = Arrays.copyOfRange(dataset[0], dataset[0].length - 1, numSamples);
//                Node parent = new Node(y);

//                nodeAdder(splitResult.get(5), parent);
//                nodeAdder(splitResult.get(6), parent);
//                nodeAdder(splitResult.get(7), parent);
                double currInfoGain = tree.informationGain(parent);

                if (currInfoGain > maxInfoGain) {
                    bestSplit.put("feature_index", featureIndex);
                    bestSplit.put("threshold", possibleThresholds);
                    if (dataset1.length > 0)
                        bestSplit.put("dataset1", dataset1);
                    if (dataset2.length > 0)
                        bestSplit.put("dataset2", dataset2);
                    else
                        bestSplit.put("dataset2" , null);
                    if (dataset3.length > 0)
                        bestSplit.put("dataset3", dataset3);
                    else
                        bestSplit.put("dataset3" , null);
                    if (dataset4.length > 0)
                        bestSplit.put("dataset4", dataset4);
                    else
                        bestSplit.put("dataset4" , null);
//                    if (dataset5.length > 0)
//                        bestSplit.put("dataset5", dataset5);
//                    else
//                        bestSplit.put("dataset5" , null);
//                    if (dataset6.length > 0)
//                        bestSplit.put("dataset6", dataset6);
//                    else
//                        bestSplit.put("dataset6" , null);
//                    if (dataset7.length > 0)
//                        bestSplit.put("dataset7", dataset7);
//                    else
//                        bestSplit.put("dataset7" , null);
                    bestSplit.put("info_gain", currInfoGain);
                    maxInfoGain = currInfoGain;
                }
            }
        }

        return bestSplit;
    }

    //calculate possible threshold
    private double[] calculatethresholdvalues(double[] featureValues) {
        double[] possibleThresholds = new double[7];
        // Select 3 items randomly from the selected items
        if (featureValues.length >= 2 && featureValues.length <= 3){
            possibleThresholds[0] = featureValues[0];
            possibleThresholds[1] = featureValues[1];
        }else if (featureValues.length >= 4 && featureValues.length <= 5){
            possibleThresholds[0] = featureValues[0];
            possibleThresholds[1] = featureValues[2];
            possibleThresholds[2] = featureValues[4];
        } else if (featureValues.length >= 6) {
            possibleThresholds[0] = featureValues[0];
            possibleThresholds[1] = featureValues[2];
            possibleThresholds[2] = featureValues[4];
            possibleThresholds[3] = featureValues[6];
        }
//        for (int i = 0; i < 7; i++) {
//            System.out.println("possibleThreshold = " + possibleThresholds[i]);
//        }
//        for (int i = 0; i < featureValues.length; i++) {
//            System.out.println("featureValues = " + featureValues[i]);
//        }
        return possibleThresholds;
    }

    //adds a dataset values to a child node and then add it to a parent node
    private void nodeAdder(double[][] dataset, Node parent) {
        if (dataset != null && dataset.length > 0) {
            double[] childNodeArray = Arrays.copyOfRange(dataset[0], dataset[0].length - 1, dataset.length);
            Node childNode = new Node(childNodeArray);
            parent.addChild(childNode);
        }
    }

    //splits 2-D dataset array and pass a List of 2-D arrays of split data's
    public List<double[][]> split(double[][] dataset, int featureIndex, double[] thresholdValues) {
        List<double[]> datasetList1 = new ArrayList<>();
        List<double[]> datasetList2 = new ArrayList<>();
        List<double[]> datasetList3 = new ArrayList<>();
        List<double[]> datasetList4 = new ArrayList<>();
//        List<double[]> datasetList5 = new ArrayList<>();
//        List<double[]> datasetList6 = new ArrayList<>();
//        List<double[]> datasetList7 = new ArrayList<>();

        System.out.println("featureIndex = " + featureIndex);

        for (double[] row : dataset) {
            if (row[featureIndex] <= thresholdValues[0])
                datasetList1.add(row);
            else if (row[featureIndex] > thresholdValues[0] && row[featureIndex] <= thresholdValues[1])
                datasetList2.add(row);
            else if (row[featureIndex] > thresholdValues[1] && row[featureIndex] <= thresholdValues[2])
                datasetList3.add(row);
            else if (row[featureIndex] > thresholdValues[2] && row[featureIndex] <= thresholdValues[3])
                datasetList4.add(row);
//            else if (row[featureIndex] > thresholdValues[3] && row[featureIndex] <= thresholdValues[4])
//                datasetList5.add(row);
//            else if (row[featureIndex] > thresholdValues[4] && row[featureIndex] <= thresholdValues[5])
//                datasetList6.add(row);
//            else if (row[featureIndex] > thresholdValues[5] && row[featureIndex] <= thresholdValues[6])
//                datasetList7.add(row);
        }

        //test for correct splitting dataset
        System.out.println("datasetList1 values = ");
        for (int j = 0; j < datasetList1.size(); j++) {
            for (int i = 0; i < 17; i++) {
                System.out.print(datasetList1.get(j)[i] + " ");
            }
            System.out.println();
        }
        List<double[][]> result = new ArrayList<>();

        double[][] dataset1 = new double[datasetList1.size()][];
        result.add(0, dataset1);
        double[][] dataset2 = new double[datasetList2.size()][];
        result.add(1, dataset2);
        double[][] dataset3;
        if (!datasetList3.isEmpty()) {
            dataset3 = new double[datasetList3.size()][];
            result.add(2, dataset3);
        }
        double[][] dataset4;
        if (!datasetList4.isEmpty()) {
            dataset4 = new double[datasetList4.size()][];
            result.add(3, dataset4);
        }
//        double[][] dataset5;
//        if (!datasetList5.isEmpty()) {
//            dataset5 = new double[datasetList5.size()][];
//            result.add(4, dataset5);
//        }
//        double[][] dataset6;
//        if (!datasetList6.isEmpty()) {
//            dataset6 = new double[datasetList6.size()][];
//            result.add(5, dataset6);
//        }
//        double[][] dataset7;
//        if (!datasetList7.isEmpty()) {
//            dataset7 = new double[datasetList7.size()][];
//            result.add(6, dataset7);
//        }
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
    //Not Implemented Yet .
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

    // Not Implemented Yet
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
