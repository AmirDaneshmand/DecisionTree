import java.util.*;

public class DecisionTreeClassifier {

    private Tree tree;
    private int minSamplesSplit;
    private int maxDepth;
    private double[][] data;
    private int[] labels;
    public double[][] replaceDataSet;

    public DecisionTreeClassifier(Tree tree, double[][] data, int[] labels) {
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
        // Split the dataset
        Map<String, Object> bestSplit = getBestSplit(dataset, dataset.length, dataset[0].length, labels);
        // Extract information from the best split
        int featureIndex = (int) bestSplit.get("feature_index");
        double[] thresholds = (double[]) bestSplit.get("threshold");

        double[][] replaceDataSet = new double[dataset.length][dataset[0].length-1];
        for (int i = 0; i < dataset.length; i++) {
            if (featureIndex >= 0) System.arraycopy(dataset[i], 0, replaceDataSet[i], 0, featureIndex);
            if (dataset[0].length - (featureIndex + 1) >= 0)
                System.arraycopy(dataset[i], featureIndex + 1, replaceDataSet[i], featureIndex + 1, dataset[0].length - (featureIndex + 1));
        }

        this.replaceDataSet = replaceDataSet;
        this.data = replaceDataSet;
        // delete the choosen featureIndex

//        double[][] dataset1 = (double[][]) bestSplit.get("dataset1");
//        double[][] dataset2 = (double[][]) bestSplit.get("dataset2");
//        double[][] dataset3 = (double[][]) bestSplit.get("dataset3");
//        double[][] dataset4 = (double[][]) bestSplit.get("dataset4");
        double infoGain = (double) bestSplit.get("info_gain");

        // Check conditions for building subtrees
        if (infoGain > 0 && currDepth <= maxDepth) {
//            Node leftSubtree = buildTree(dataset1, currDepth + 1, maxDepth);
//            Node rightSubtree = buildTree(dataset2, currDepth + 1, maxDepth);
//            Node midLeftSubtree = buildTree(dataset3, currDepth + 1, maxDepth);
//            Node SubTree4 = buildTree(dataset4, currDepth + 1, maxDepth);
            // Return a non-leaf node
//            return new Node(thresholds, infoGain, featureIndex);
        } else {
            // Return a leaf node
            double[] X = Arrays.copyOf(dataset[0], dataset[0].length);
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

    //check for leaf or decision Node
    private boolean checkLeaf(double[][] splitResult, int featureIndex) {
        double equals = splitResult[0][17];
        for (int j = 0; j < splitResult.length; j++) {
            if (splitResult[j][17] != equals) {
                return false;
            }
        }
        return true;
    }

    //calculates Possible threshold and find the best split for a given dataset
    public Map<String, Object> getBestSplit(double[][] dataset, int numSamples, int numFeatures, int[] labels) {
        Map<String, Object> bestSplit = new HashMap<>();
        double maxInfoGain = Double.NEGATIVE_INFINITY;

        for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++) {
            double[] featureValues = new double[numSamples];
            for (int i = 0; i < dataset.length - 1; i++) {
                featureValues[i] = dataset[i][featureIndex];
            }

            double[] uniqueFeatureValues = new double[17];
            // Adding unique elements to uniqueFeatureValues
            for (int i = 0; i < featureValues.length; i++) {
                uniqueFeatureValues = Arrays.stream(featureValues).distinct().toArray();
            }
            // Sort the uniqueFeatureValues array
            Arrays.sort(uniqueFeatureValues);
            double[] possibleThresholds = calculateThValues(uniqueFeatureValues);

            //small temporary test on detaset
            double[][] temp = new double[10][17];
            for (int i = 0; i < 10; i++) {
                System.arraycopy(dataset[i], 0, temp[i], 0, 17);
                //                    System.out.print(temp[i][j] + " ");

//                System.out.println();
            }
            //****************************//number 10 here should have edited later !!!! //*****************************//
            //  here it was a small test on dataset, and later it should've change to numSamples
            double[] parentValues = new double[10];
            boolean flag = false;
            for (int i = 0; i < 10; i++) {
                parentValues[i] = temp[i][featureIndex];
                if (parentValues[i] != parentValues[0])
                    flag = true;
            }
            List<double[][]> splitResult = split(temp, labels, featureIndex, possibleThresholds, numFeatures);

            //add each child node to its parent
            Node parent = new Node(parentValues, false);
            buildChildren(splitResult, parent, featureIndex, numSamples);
            double currInfoGain = maxInfoGain;
            System.out.println("flag = " + flag);
            if (flag)
                 currInfoGain = tree.informationGain(parent);
            System.out.println("currInfoGain = " + currInfoGain);
            System.out.println("featureIndex = " + featureIndex);
            if (currInfoGain > maxInfoGain) {
                bestSplit.put("feature_index", featureIndex);
                bestSplit.put("threshold", possibleThresholds);
                bestSplit.put("child_dataset1", splitResult.get(0));
                bestSplit.put("child_dataset2", splitResult.get(1));
                bestSplit.put("info_gain", currInfoGain);
                maxInfoGain = currInfoGain;
            }
        }
        return bestSplit;
    }

    //gets split result and add them to parent Node
    private void buildChildren(List<double[][]> splitResult, Node parent, int featureIndex, int numSamples) {
        if (!splitResult.isEmpty()) {
            for (double[][] doubles : splitResult) {
                Node childNode;
                if (doubles != null) {
                    double[] childValues = new double[numSamples];
                    for (int j = 0; j < doubles.length; j++) {
                        childValues[j] = doubles[j][featureIndex];
                    }
                    if (checkLeaf(doubles, featureIndex)) {
                        //childNode is a leaf Node
                        childNode = new Node(childValues, true);
                        System.out.println("Gorgali leaf");
                    } else {
                        //childNode is a Decision Node
                        childNode = new Node(childValues, false);
                        System.out.println("Gorgali Decision Node");
                    }
                    parent.addChild(childNode);
                }
            }
        }
    }

//    public void fit(double[][] X, double[] Y) {
//        double[][] dataset = new double[X.length][X[0].length + 1];
//        for (int i = 0; i < X.length; i++) {
//            System.arraycopy(X[i], 0, dataset[i], 0, X[i].length);
//            dataset[i][X[i].length] = Y[i];
//        }
//
//        root = buildTree(dataset, 0);
//    }

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

    //calculate possible threshold
    private double[] calculateThValues(double[] featureValues) {
        double[] possibleThresholds = new double[7];
        // Select 3 items randomly from the selected items
        if (featureValues.length >= 2 && featureValues.length <= 3) {
            possibleThresholds[0] = featureValues[0];
            possibleThresholds[1] = featureValues[1];
        } else if (featureValues.length >= 4 && featureValues.length <= 5) {
            possibleThresholds[0] = featureValues[0];
            possibleThresholds[1] = featureValues[2];
            possibleThresholds[2] = featureValues[4];
        } else if (featureValues.length >= 6) {
            possibleThresholds[0] = featureValues[0];
            possibleThresholds[1] = featureValues[2];
            possibleThresholds[2] = featureValues[4];
            possibleThresholds[3] = featureValues[5];
        }
        return possibleThresholds;
    }

    //adds a dataset values to a child node and then add it to a parent node
    private void nodeAdder(double[] dataset, Node parent) {
        if (dataset != null && dataset.length > 0) {
            Node childNode = new Node(dataset);
            parent.addChild(childNode);
        }
    }

    //splits 2-D dataset array and pass a List of 2-D arrays of split data's
    public List<double[][]> split(double[][] dataset, int[] labels, int featureIndex, double[] thresholdValues, int numFeatures) {
        List<double[]> datasetlist1 = new ArrayList<>();
        List<double[]> datasetlist2 = new ArrayList<>();
        List<double[]> datasetlist3 = new ArrayList<>();
        List<double[]> datasetlist4 = new ArrayList<>();

        for (int i = 0; i < dataset.length; i++) {
            double[] temp = new double[numFeatures + 1];
            System.arraycopy(dataset[i], 0, temp, 0, numFeatures);
            temp[numFeatures] = labels[i];
            if (dataset[i][featureIndex] <= thresholdValues[0]) {
                datasetlist1.add(temp);
            } else if (dataset[i][featureIndex] > thresholdValues[0] && dataset[i][featureIndex] <= thresholdValues[1]) {
                datasetlist2.add(temp);
            } else if (dataset[i][featureIndex] > thresholdValues[1] && dataset[i][featureIndex] <= thresholdValues[2]) {
                datasetlist3.add(temp);
            } else if (dataset[i][featureIndex] > thresholdValues[2] && dataset[i][featureIndex] <= thresholdValues[3]) {
                datasetlist4.add(temp);
            }
        }
        List<double[][]> datasetListresult = new ArrayList<>();
        pourList(datasetListresult, datasetlist1, numFeatures);
        pourList(datasetListresult, datasetlist2, numFeatures);
        pourList(datasetListresult, datasetlist3, numFeatures);
        pourList(datasetListresult, datasetlist4, numFeatures);
        return datasetListresult;
    }

    private void pourList(List<double[][]> datasetListresult, List<double[]> datasetlist, int numFeatures) {
        if (!datasetlist.isEmpty()) {
            double[][] temp = new double[datasetlist.size()][numFeatures + 1];
            for (int j = 0; j < datasetlist.size(); j++) {
                for (int l = 0; l < numFeatures + 1; l++) {
                    temp[j][l] = datasetlist.get(j)[l];
                    System.out.print(" " + temp[j][l] + " ");
                }
                System.out.println();
            }
            datasetListresult.add(temp);
        }
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
