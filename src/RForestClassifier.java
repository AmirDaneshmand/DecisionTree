import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RForestClassifier {

    private List<DecisionTreeClassifier> decisionTrees;
    private int numberOfEstimators;

    // Constructor
    public RForestClassifier(double[][] dataset, int[] labels, int numberOfEstimators, int maxDepth) {
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
    public double predict(double[] sample) {
        int[] votes = new int[2]; // Assuming binary classification (0 or 1)

        // Collect votes from each decision tree
        for (DecisionTreeClassifier treeClassifier : decisionTrees) {
            double prediction = treeClassifier.makePrediction(new double[][]{sample}, treeClassifier.getRoot(), new ArrayList<>(), 0);
            votes[(int) prediction]++;
        }

        // Return the label with the majority of votes
        return votes[1] > votes[0] ? 1.0 : 0.0;
    }

    // Make predictions for multiple samples
    public double[] makePredictions(double[][] samples) {
        double[] predictions = new double[samples.length];

        // Make predictions for each sample
        for (int i = 0; i < samples.length; i++) {
            predictions[i] = predict(samples[i]);
        }

        return predictions;
    }

    // Calculate the accuracy of the model
    public double accuracy(double[] trueLabels, double[] predictedLabels) {
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
