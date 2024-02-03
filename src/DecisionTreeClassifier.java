import java.util.*;

public class DecisionTreeClassifier {

    private Tree tree;
    private Node root;
    private double[][] data;
    private int[] labels;

    //Constructor
    public DecisionTreeClassifier(Tree tree, double[][] data, int[] labels) {
        this.tree = tree;
        this.data = data;
        this.labels = labels;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    //Predicts the label of given dataset
    public double predict(double dataset[][], Node node, int featureValue, ArrayList<Integer> featureArr) {
        //if node is a Leaf Node
//        System.out.println("dataset[featureValue][dataset[0].length - 1] = " + dataset[featureValue][dataset[0].length - 1]);
        if (node.getLeaf()) {
            return dataset[featureValue][dataset[0].length - 1];
        }
        //if node is Decision Node
        else {
            // Remove the featureValue from featureArr
            deleteFeature(featureArr, featureValue);
//            for (Node child : node.getChildrenNodes()) {
            makePrediction(dataset, node, featureArr);
//            }
            return 0;
        }
    }

    //Takes dataset and tree and iterates over parent's child to until reach a leaf
    public double[] makePrediction(double[][] dataset, Node root, ArrayList<Integer> featureArr) {
//        Node root = tree.getRoot();
//        System.out.println("rootValue = " + Arrays.toString(this.root.getValue()));
        double predictedLabel;
        double[] predictedArray = new double[dataset.length];
        for (int i = 0; i < dataset.length; i++) {
//            System.out.println("dataset[i][root.getFeatureIndex()] = " + dataset[i][this.root.getFeatureIndex()]);
//            System.out.println(this.root.getChildrenNodes());
            for (Node child : this.root.getChildrenNodes()) {
                int featureValue = (int) dataset[i][this.root.getFeatureIndex()];
//                if (featureArrr.get(root.getFeatureIndex()) == featureValue) {
                if (containValue(child, featureValue)) {
                    predictedLabel = predict(dataset, child, featureValue, featureArr);
                    if (predictedLabel != 0)
                        predictedArray[i] = predictedLabel;
                }
//                }
            }
        }
        return predictedArray;
    }

    //checks to see if a given Value is on the child 's values or not
    private boolean containValue(Node child, int featureValue) {
        for (double childValue : child.getValue()) {
            if (childValue == featureValue)
                return true;
        }
        return false;
    }

    private boolean firstBuild = true;

    //Building tree based on calculating iGain and Entropy for each split
    public Node buildTree(double[][] dataset, int currDepth, ArrayList<Integer> featureIndexArray) {
        // Split the dataset
        if (dataset != null) {
            Map<String, Object> bestSplit = getBestSplit(dataset, dataset.length, dataset[0].length, featureIndexArray);
            if (bestSplit != null) {
                // Extract information from the best split
                int featureIndex = -1;
                if (bestSplit.get("feature_index") != null)
                    featureIndex = (int) bestSplit.get("feature_index");
                double infoGain = -10;
                if (bestSplit.get("feature_index") != null)
                    infoGain = (double) bestSplit.get("info_gain");
                Node parent = null;
                if (bestSplit.get("parent_node") != null)
                    parent = (Node) bestSplit.get("parent_node");
                System.out.println("featureIndex of best split = " + featureIndex);
                System.out.println("information gain of best split = " + infoGain);
                if (firstBuild) {
                    root = parent;
                    tree.setRoot(parent);
                    firstBuild = false;
                }
                // Remove the bestSplit's index from featureIndexArray
                deleteFeature(featureIndexArray, featureIndex);
                System.out.println("currDepth = " + currDepth);
//        System.out.println("currinfoGain = " + infoGain);
//                System.out.println("fit(parent , bestSplit , currDepth , featureIndexArray).getFeatureIndex() : " +
//                        fit(parent, bestSplit, currDepth, featureIndexArray).getFeatureIndex());
                if (parent != null)
                    return fit(parent, bestSplit, currDepth, featureIndexArray);
                // Return a non-leaf node
//        return new Node(parent.getValue(), false);
                // Return a Leaf Node
//        return new Node((double[]) bestSplit.get("value"), true);
            }
        }
        return null;
    }

    //Fits the Tree by Building its Subtrees'
    public Node fit(Node parent, Map<String, Object> bestsplit, int currDepth, ArrayList<Integer> fIndexArr) {
        //Stores Decision Nodes' Indexes
        ArrayList<Integer> dNodeIndex = new ArrayList<>();
//        System.out.println("parent.getChildrenNodes().size() : " + parent.getChildrenNodes().size());
        if (parent != null) {
            for (int i = 0; i < parent.getChildrenNodes().size(); i++) {
//            System.out.println("Gorgaliii " + (i + 1) + " is parent's child");
                if (bestsplit != null) {
                    //whether child Node is a Decision Node or Leaf Node
                    if (!checkLeaf((double[][]) bestsplit.get(String.format("child_dataset%d", i + 1)))) {
                        if (calculateLeafValue((double[][]) bestsplit.get(String.format("child_dataset%d", i + 1))) <= 0.9)
                            dNodeIndex.add(i + 1);
                    }
                }
            }
            // Check Conditions for Building Subtrees
            Node leftSubtree;
            if (dNodeIndex.size() > 0) {
                deleteFeature(fIndexArr, dNodeIndex.get(0));
                leftSubtree = buildTree((double[][]) bestsplit.get(String.format("child_dataset%d", dNodeIndex.get(0))), currDepth + 1, fIndexArr);
                parent.addChild(leftSubtree);
                Node rightSubtree;
                if (dNodeIndex.size() > 1) {
                    deleteFeature(fIndexArr, dNodeIndex.get(1));
                    rightSubtree = buildTree((double[][]) bestsplit.get(String.format("child_dataset%d", dNodeIndex.get(1))), currDepth + 1, fIndexArr);
                    parent.addChild(rightSubtree);
                    Node leftmidSubtree;
                    if (dNodeIndex.size() > 2) {
                        deleteFeature(fIndexArr, dNodeIndex.get(2));
                        leftmidSubtree = buildTree((double[][]) bestsplit.get(String.format("child_dataset%d", dNodeIndex.get(2))), currDepth + 1, fIndexArr);
                        parent.addChild(leftmidSubtree);
                        Node rightmidSubtree;
                        if (dNodeIndex.size() > 3) {
                            deleteFeature(fIndexArr, dNodeIndex.get(3));
                            rightmidSubtree = buildTree((double[][]) bestsplit.get(String.format("child_dataset%d", dNodeIndex.get(3))), currDepth + 1, fIndexArr);
                            parent.addChild(rightmidSubtree);
                        }
                    }
                }
            }
            return parent;
        }
        return null;
    }

    //check for leaf or decision Node
    private boolean checkLeaf(double[][] splitResult) {
        if (splitResult != null) {
            double equals = splitResult[0][17];
            for (int j = 0; j < splitResult.length; j++) {
                if (splitResult[j][17] != equals) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    //calculates Possible threshold and find the best split for a given dataset
    public Map<String, Object> getBestSplit(double[][] dataset, int numSamples, int numFeatures, ArrayList<Integer> featureArr) {
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
            List<double[][]> splitResult = split(dataset, featureIndex, possibleThArr, numFeatures);

            //add each child node to its parent
            Node parent = new Node(parentValues, false);
            buildChildren(splitResult, parent, featureIndex, numSamples);
            double currInfoGain = tree.informationGain(parent);
//            System.out.println("currInfoGain = " + currInfoGain);
//            System.out.println("featureIndex = " + featureIndex);
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
        if (splitResult != null) {
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
//                        System.out.println("Gorgali leaf");
                    } else {
                        //childNode is a Decision Node
                        childNode = new Node(childValues, false);
//                        System.out.println("Gorgali Decision Node");
                    }
                    parent.addChild(childNode);
                }
            }
        }
    }

    //Removes an element from an ArrayList and return it at the end
    private void deleteFeature(ArrayList<Integer> featureArray, int featuretoRemove) {
        Iterator<Integer> iterator = featureArray.iterator();
        while (iterator.hasNext()) {
            int feature = iterator.next();
            if (feature == featuretoRemove) {
                iterator.remove();
//                System.out.println("Removed bestSplit's index: " + featuretoRemove);
            }
        }
    }

    //splits 2-D dataset array and pass a List of 2-D arrays of split data's
    public List<double[][]> split(double[][] dataset, int featureIndex, double[] thresholdValues, int numFeatures) {
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

    //Pours dataset-result List by getting each row of dataset List
    private void pourList(List<double[][]> datasetListresult, List<double[]> datasetlist, int numFeatures) {
        if (!datasetlist.isEmpty()) {
            double[][] temp = new double[datasetlist.size()][numFeatures];
            for (int j = 0; j < datasetlist.size(); j++) {
                for (int l = 0; l < numFeatures; l++) {
                    temp[j][l] = datasetlist.get(j)[l];
//                    System.out.print(" " + temp[j][l] + " ");
                }
//                System.out.println();
            }
            datasetListresult.add(temp);
        }
    }

    //find the element with the maximum occurrences and calculate the purity
    public double calculateLeafValue(double[][] dataset) {
        if (dataset != null) {
            int lastIndex = dataset[0].length - 1; // Assuming the last column is at index 17
//        System.out.println("dataset.length = " + dataset.length);

            // Count occurrences of each unique element in the last column
            Map<Double, Integer> countMap = new HashMap<>();
            for (int j = 0; j < dataset.length; j++) {
                double element = dataset[j][lastIndex];
                countMap.put(element, countMap.getOrDefault(element, 0) + 1);
//            System.out.print(" element " + element);
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

//        System.out.println(" Most Repeated Element: " + mostRepeatedElement);
//        System.out.println(" Purity: " + purity);

            return purity;
        }
        return 0;
    }

    //Not Implemented Yet.
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

    // compares correctLabels and predictedLabels and calculates the accuracy of the Algorithm
    public double accuracyScore(int[] correctLabels, double[] predictedLabels) {
        int correctPredicted = 0;
        for (int i = 0; i < correctLabels.length; i++) {
            if (correctLabels[i] == predictedLabels[i])
                correctPredicted++;
        }
//        System.out.println("correct predicted = " + correctPredicted);
//        System.out.println("correctLabels.length = " + correctLabels.length);
        return (double) correctPredicted / correctLabels.length;
    }
}
