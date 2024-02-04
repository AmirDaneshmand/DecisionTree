import java.util.*;

public class RForestClassifier {

    private List<DecisionTreeClassifier> decisionTrees;
    private int numberOfEstimators;

    // Constructor
    public RForestClassifier(double[][] dataset, int[] labels, int numberOfEstimators) {
        this.numberOfEstimators = numberOfEstimators;
        decisionTrees = new ArrayList<>();

        // Build multiple decision trees
        for (int i = 0; i < numberOfEstimators; i++) {
            // Create a random subset of the data for each tree
            double[][] randomSubset = getRandomSubset(dataset, labels);
            Tree tree = new Tree(0);
            DecisionTreeClassifier treeClassifier = new DecisionTreeClassifier(tree, randomSubset, labels);
            Node root = treeClassifier.buildTree(randomSubset, 0, new ArrayList<>());
            tree.setRoot(root);

            decisionTrees.add(treeClassifier);
        }
    }

    // Get a random subset of the data for each decision tree
    private double[][] getRandomSubset(double[][] dataset, int[] labels) {
        Random random = new Random();
        int subsetSize = dataset.length;
        double[][] randomSubset = new double[subsetSize][dataset[0].length];

        for (int i = 0; i < subsetSize; i++) {
            int randomIndex = random.nextInt(dataset.length);
            randomSubset[i] = Arrays.copyOf(dataset[randomIndex], dataset[randomIndex].length);
        }

        return randomSubset;
    }

    // Make predictions for a single sample
    public double predict(double[] dataset , ArrayList<Integer> featureIndexArr , int index) {
        // Assuming multiple classification
        int[] votes = new int[9];
        int maxOccurrences = 0;
        if (dataset != null) {
        // Collect votes from each decision tree
        for (DecisionTreeClassifier treeClassifier : decisionTrees) {
            double prediction = treeClassifier.makePrediction(new double[][]{dataset}, treeClassifier.getRoot(), featureIndexArr, index);
            votes[(int) prediction]++;
        }
        // Return the label with the majority of votes
            for (int vote : votes) {
                if (vote > maxOccurrences)
                    maxOccurrences = vote;
            }
        }
        return maxOccurrences;
    }

    // Make predictions for multiple datasets
    public double[] makePredictions(double[][] dataset , ArrayList<Integer> featureIndexArr) {
        double[] predictions = new double[dataset.length];

        // Make predictions for each sample
        for (int i = 0; i < dataset.length; i++) {
            predictions[i] = predict(dataset[i] , featureIndexArr , i);
        }

        return predictions;
    }

    // Calculate the accuracy of the model
    public double accuracy(int[] trueLabels, double[] predictedLabels) {
        int correctPredictions = 0;

        // Count the number of correct predictions
        for (int i = 0; i < trueLabels.length; i++) {
            if (trueLabels[i] == predictedLabels[i]) {
                correctPredictions++;
            }
        }

        // Calculate and return the accuracy
        return (double) correctPredictions / trueLabels.length;
    }

    // Getters
    public int getNumberOfEstimators() {
        return numberOfEstimators;
    }

    public List<DecisionTreeClassifier> getDecisionTrees() {
        return decisionTrees;
    }

    public static void main(String[] args) {
        // You can use this section for testing the RForestClassifier
        // Example usage:
        // RForestClassifier rfClassifier = new RForestClassifier(data, labels, 5, 3);
        // double[] predictions = rfClassifier.makePredictions(testData);
        // double accuracy = rfClassifier.accuracy(trueLabels, predictions);
        // System.out.println("Accuracy: " + accuracy);
    }
}
