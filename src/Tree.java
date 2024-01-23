import java.util.Arrays;

public class Tree {
    int depth;

    public int getDepth() {
        return depth;
    }

//    public Node buildTree(double[][] dataset, int currDepth , int minSamplesSplit , int maxDepth) {
//        double[][] X = new double[dataset.length][dataset[0].length - 1];
//        double[] Y = new double[dataset.length];
//        for (int i = 0; i < dataset.length; i++) {
//            System.arraycopy(dataset[i], 0, X[i], 0, dataset[i].length - 1);
//            Y[i] = dataset[i][dataset[i].length - 1];
//        }
//
//        int numSamples = X.length;
//        int numFeatures = X[0].length;
//
//        if (numSamples >= minSamplesSplit && currDepth <= maxDepth) {
//            Map<String, Object> bestSplit = getBestSplit(dataset, numSamples, numFeatures);
//
//            if ((double) bestSplit.get("info_gain") > 0) {
//                Node leftSubtree = buildTree((double[][]) bestSplit.get("dataset_left"), currDepth + 1);
//                Node rightSubtree = buildTree((double[][]) bestSplit.get("dataset_right"), currDepth + 1);
//
//                return new Node(
//                        (int) bestSplit.get("feature_index"),
//                        (double) bestSplit.get("threshold"),
//                        leftSubtree,
//                        rightSubtree,
//                        (double) bestSplit.get("info_gain")
//                );
//            }
//        }
//
//        double leafValue = calculateLeafValue(Y);
//        return new Node(leafValue);
//    }

//    public Map<String, Object> getBestSplit(double[][] dataset, int numSamples, int numFeatures) {
//        Map<String, Object> bestSplit = new HashMap<>();
//        double maxInfoGain = Double.NEGATIVE_INFINITY;
//
//        for (int featureIndex = 0; featureIndex < numFeatures; featureIndex++) {
//            double[] featureValues = new double[numSamples];
//            for (int i = 0; i < numSamples; i++) {
//                featureValues[i] = dataset[i][featureIndex];
//            }
//
//            double[] possibleThresholds = Arrays.stream(featureValues).distinct().toArray();
//
//            for (double threshold : possibleThresholds) {
//                double[][] splitResult = split(dataset, featureIndex, threshold);
//                double[][] datasetLeft = splitResult[0];
//                double[][] datasetRight = splitResult[1];
//
//                if (datasetLeft.length > 0 && datasetRight.length > 0) {
//                    double[] y = Arrays.copyOfRange(dataset[0], dataset[0].length - 1, numSamples);
//                    double[] leftY = Arrays.copyOfRange(datasetLeft[0], datasetLeft[0].length - 1, datasetLeft.length);
//                    double[] rightY = Arrays.copyOfRange(datasetRight[0], datasetRight[0].length - 1, datasetRight.length);
//
//                    double currInfoGain = informationGain(y, leftY, rightY, "gini");
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
//            }
//        }
//
//        return bestSplit;
//    }

//    public double[][] split(double[][] dataset, int featureIndex, double threshold) {
//        List<double[]> datasetLeftList = new ArrayList<>();
//        List<double[]> datasetRightList = new ArrayList<>();
//
//        for (double[] row : dataset) {
//            if (row[featureIndex] <= threshold) {
//                datasetLeftList.add(row);
//            } else {
//                datasetRightList.add(row);
//            }
//        }
//
//        double[][] datasetLeft = new double[datasetLeftList.size()][];
//        double[][] datasetRight = new double[datasetRightList.size()][];
//
//        return new double[][]{
//                datasetLeftList.toArray(datasetLeft),
//                datasetRightList.toArray(datasetRight)
//        };
//    }

    float Entropy(float[] labels) {
        return 0;
    }

    //calculate information gain for a child
    public double informationGain(double[] children , double[] parent) {
        double [] weight = new double[children.length];
        for (int i = 0; i < children.length; i++) {
            weight[i] = calculateChildWeight(children , parent);
        }
        double gain;
        double SumEntropies = 0;
        for (int i = 0; i < children.length; i++) {
            SumEntropies += weight[i] * entropy(children);
        }
        gain = entropy(parent) - SumEntropies;
        return gain;
    }

    public double calculateChildWeight(double [] childNode , double [] parentNode){
        return (double) childNode.length / parentNode.length;
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

//    public double calculateLeafValue(double[] Y) {
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
//        return leafValue;
//    }

}
