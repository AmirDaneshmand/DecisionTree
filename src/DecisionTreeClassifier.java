import java.util.*;

public class DecisionTreeClassifier {

    private Tree tree;
    private int minSamplesSplit;
    private int maxDepth;
    private double[][] data;
    private int[] labels;

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
    public Node buildTree(double[][] dataset, int currDepth, int maxDepth, ArrayList<Integer> featureIndexArray) {
        // Split the dataset
        Map<String, Object> bestSplit = getBestSplit(dataset, dataset.length, dataset[0].length, labels, featureIndexArray);
        // Extract information from the best split
        int featureIndex = 20;
        if (bestSplit.get("feature_index") != null)
            featureIndex = (int) bestSplit.get("feature_index");
        double infoGain = (double) bestSplit.get("info_gain");
        Node parent = (Node) bestSplit.get("parent_node");
        // Remove the bestSplit's index from featureIndexArray
        Iterator<Integer> iterator = featureIndexArray.iterator();
        while (iterator.hasNext()) {
            int feature = iterator.next();
            if (feature == featureIndex) {
                iterator.remove();
                System.out.println("Removed bestSplit's index: " + featureIndex);
            }
        }
//        double[][] dataset1 = new double[dataset.length][dataset[0].length];
//        if (bestSplit.size() > 0)
//            dataset1 = (double[][]) bestSplit.get("child_dataset1");
//        double[][] dataset2 = new double[dataset.length][dataset[0].length];
//        if (bestSplit.size() > 1)
//            dataset2 = (double[][]) bestSplit.get("child_dataset2");
//        double[][] dataset3 = new double[dataset.length][dataset[0].length];
//        if (bestSplit.size() > 2)
//            dataset3 = (double[][]) bestSplit.get("child_dataset3");
//        double[][] dataset4 = new double[dataset.length][dataset[0].length];
//        if (bestSplit.size() > 3)
//            dataset4 = (double[][]) bestSplit.get("child_dataset4");
//        double[][] dataset5 = new double[dataset.length][dataset[0].length];
//        if (bestSplit.size() > 4)
//            dataset5 = (double[][]) bestSplit.get("child_dataset5");
        System.out.println("currDepth = " + currDepth);
        System.out.println("currinfoGain = " + infoGain);
        // Check conditions for building subtrees
//        if (currDepth <= maxDepth) {
        for (int i = 0; i < parent.getChildrenNodes().size(); i++) {
            Node subTree;
            if (calculateLeafValue((double[][]) bestSplit.get(String.format("child_dataset%d", i + 1))) <= 0.9) {
                //whether child Node is a Decision Node or Leaf Node
                if (!parent.getChilrenByIndex(i).getLeaf()) {
                    System.out.println("GorgAliiiiiiiiiiii");
                    subTree = buildTree((double[][]) bestSplit.get(String.format("child_dataset%d", i + 1)), currDepth + 1, maxDepth, featureIndexArray);
                }
                // Return a non-leaf node
                return new Node(parent.getValue(), false);
            }
//            }
        }
        // Return a Leaf Node
        return new Node((double[]) bestSplit.get("value"), true);

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
    }

    //check for leaf or decision Node
    private boolean checkLeaf(double[][] splitResult) {
        double equals = splitResult[0][17];
        for (int j = 0; j < splitResult.length; j++) {
            if (splitResult[j][17] != equals) {
                return false;
            }
        }
        return true;
    }

    //calculates Possible threshold and find the best split for a given dataset
    public Map<String, Object> getBestSplit(double[][] dataset, int numSamples, int numFeatures, int[] labels, ArrayList<Integer> featureArr) {
        Map<String, Object> bestSplit = new HashMap<>();
        double maxInfoGain = Double.NEGATIVE_INFINITY;

        for (int featureIndex : featureArr) {
            double[] featureValues = new double[numSamples];
            for (int i = 0; i < dataset.length - 1; i++) {
                featureValues[i] = dataset[i][featureIndex];
            }
            double[] uniqueFeatureValues;
            //Adding unique elements to uniqueFeatureValues
            uniqueFeatureValues = Arrays.stream(featureValues).distinct().toArray();
            //Sort the uniqueFeatureValues array
            Arrays.sort(uniqueFeatureValues);
            double[] possibleThArr = new double[9];
            System.arraycopy(uniqueFeatureValues, 0, possibleThArr, 0, uniqueFeatureValues.length);
            for (int i = uniqueFeatureValues.length; i < 9; i++) {
                possibleThArr[i] = -1;
            }
            double[] parentValues = new double[numSamples];
            for (int i = 0; i < numSamples; i++) {
                parentValues[i] = dataset[i][featureIndex];
            }
            List<double[][]> splitResult = split(dataset, labels, featureIndex, possibleThArr, numFeatures);

            //add each child node to its parent
            Node parent = new Node(parentValues, false);
            buildChildren(splitResult, parent, featureIndex, numSamples);
            double currInfoGain = maxInfoGain;
            currInfoGain = tree.informationGain(parent);
            System.out.println("currInfoGain = " + currInfoGain);
            System.out.println("featureIndex = " + featureIndex);
            if (currInfoGain > maxInfoGain) {
                bestSplit.put("feature_index", featureIndex);
                bestSplit.put("parent_node", parent);
                bestSplit.put("child_dataset1", splitResult.get(0));
                if (splitResult.size() > 1)
                    bestSplit.put("child_dataset2", splitResult.get(1));
                if (splitResult.size() > 2)
                    bestSplit.put("child_dataset3", splitResult.get(2));
                if (splitResult.size() > 3)
                    bestSplit.put("child_dataset4", splitResult.get(3));
                if (splitResult.size() > 4)
                    bestSplit.put("child_dataset5", splitResult.get(4));
                if (splitResult.size() > 5)
                    bestSplit.put("child_dataset6", splitResult.get(5));
                bestSplit.put("info_gain", currInfoGain);
                maxInfoGain = currInfoGain;
            }
        }
        return bestSplit;
    }

    //gets split result and add them to parent Node
    private void buildChildren(List<double[][]> splitResult, Node parent, int featureIndex, int numSamples) {
        if (!splitResult.isEmpty()) {
            for (int i = 0; i < splitResult.size(); i++) {
                Node childNode;
                if (splitResult.get(i) != null) {
                    double[] childValues = new double[numSamples];
                    for (int j = 0; j < splitResult.get(i).length; j++) {
                        childValues[j] = splitResult.get(i)[j][featureIndex];
                    }
                    if (checkLeaf(splitResult.get(i))) {
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
        List<double[]> datasetlist5 = new ArrayList<>();
        List<double[]> datasetlist6 = new ArrayList<>();
        List<double[]> datasetlist7 = new ArrayList<>();

        for (int i = 0; i < dataset.length; i++) {
            if (dataset[i][featureIndex] <= thresholdValues[0]) {
                datasetlist1.add(dataset[i]);
            } else if (dataset[i][featureIndex] > thresholdValues[0] && dataset[i][featureIndex] <= thresholdValues[1]) {
                datasetlist2.add(dataset[i]);
            } else if (dataset[i][featureIndex] > thresholdValues[1] && dataset[i][featureIndex] <= thresholdValues[2]) {
                datasetlist3.add(dataset[i]);
            } else if (dataset[i][featureIndex] > thresholdValues[2] && dataset[i][featureIndex] <= thresholdValues[3]) {
                datasetlist4.add(dataset[i]);
            } else if (dataset[i][featureIndex] > thresholdValues[3] && dataset[i][featureIndex] <= thresholdValues[4]) {
                datasetlist5.add(dataset[i]);
            } else if (dataset[i][featureIndex] > thresholdValues[4] && dataset[i][featureIndex] <= thresholdValues[5]) {
                datasetlist6.add(dataset[i]);
            } else if (dataset[i][featureIndex] > thresholdValues[5] && dataset[i][featureIndex] <= thresholdValues[6]) {
                datasetlist7.add(dataset[i]);
            }
        }
        List<double[][]> datasetListresult = new ArrayList<>();
        pourList(datasetListresult, datasetlist1, numFeatures);
        pourList(datasetListresult, datasetlist2, numFeatures);
        pourList(datasetListresult, datasetlist3, numFeatures);
        pourList(datasetListresult, datasetlist4, numFeatures);
        pourList(datasetListresult, datasetlist5, numFeatures);
        pourList(datasetListresult, datasetlist6, numFeatures);
        pourList(datasetListresult, datasetlist7, numFeatures);
        return datasetListresult;
    }

    private void pourList(List<double[][]> datasetListresult, List<double[]> datasetlist, int numFeatures) {
        if (!datasetlist.isEmpty()) {
            double[][] temp = new double[datasetlist.size()][numFeatures];
            for (int j = 0; j < datasetlist.size(); j++) {
                for (int l = 0; l < numFeatures; l++) {
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
//    public double calculateLeafValue(double[][] dataset) {
//        double equals = dataset[0][17];
//        double purity;
//        for (int j = 0; j < dataset.length; j++) {
//            if (dataset[j][17] == equals) {
//
//            }
//        }
//
//        Map<Double, Integer> counts = new HashMap<>();
//        for (double value : Y) {
//            counts.put(value, counts.getOrDefault(value, 0) + 1);
//        }
//
//        double maxCount = Double.NEGATIVE_INFINITY;
//        double leafValue = 0;
//
//        for (Map.Entry<Double, Integer> entry : counts.entrySet()) {
//            if (entry.getValue() > maxCount) {
//                maxCount = entry.getValue();
//                leafValue = entry.getKey();
//            }
//        }
//
//        return new double[]{leafValue};
//    }
    public double calculateLeafValue(double[][] dataset) {
        int lastIndex = dataset[0].length - 1; // Assuming the last column is at index 17

        // Count occurrences of each unique element in the last column
        Map<Double, Integer> countMap = new HashMap<>();
        for (int j = 0; j < dataset.length; j++) {
            double element = dataset[j][lastIndex];
            countMap.put(element, countMap.getOrDefault(element, 0) + 1);
        }

        // Find the element with the maximum occurrences
        double mostRepeatedElement = 0;
        int maxOccurrences = 0;
        for (Map.Entry<Double, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxOccurrences) {
                maxOccurrences = entry.getValue();
                mostRepeatedElement = entry.getKey();
            }
        }

        // Calculate purity (occurrences of most repeated element / total elements)
        double purity = (double) maxOccurrences / dataset.length;

        System.out.println("Most Repeated Element: " + mostRepeatedElement);
        System.out.println("Purity: " + purity);

        return purity;
    }

    // Not Implemented Yet
    // compare correctLabels and predictedLabels and calculate the accuracy of the algorithm
    public double accuracyScore(double[] correctLabels, double[] predictedLabels) {
        int correctPredicted = 0;
        for (int i = 0; i < correctLabels.length; i++) {
            if (correctLabels[i] == predictedLabels[i])
                correctPredicted++;
        }
        return (double) correctPredicted / correctLabels.length * 100.0;
    }
}
