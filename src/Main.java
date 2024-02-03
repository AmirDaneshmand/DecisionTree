import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Load dataset
        String[] colNames = {"HighBP", "HighChol", "CholCheck", "Smoker", "Stroke", "HeartDiseaseorAttack",
                "PhysActivity", "Fruits", "Veggies", "HvyAlcoholConsump", "AnyHealthcare",
                "NoDocbcCost", "GenHlth", "DiffWalk", "Sex", "Education", "Income"};

        double[][] data = readCSV("Data/feature_train.csv", colNames);
        double[][] label = readCSV("Data/label_train.csv", colNames);
        double[][] data_test = readCSV("Data/feature_test.csv", colNames);
        double[][] label_test = readCSV("Data/label_test.csv", colNames);

        int[] label_temp = new int[label.length];
        int[] label_test_temp = new int[label_test.length];
//        System.out.println("label.length = " + label.length);
        for (int i = 0; i < label.length; i++) {
            label_temp[i] = (int) label[i][0];
        }
//        System.out.println("label_test.length = " + label_test.length);
        for (int i = 0; i < label_test.length; i++) {
            label_test_temp[i] = (int) label_test[i][0];
        }

        // Run ID3 Algorithm
        Tree tree = new Tree(0);
        // Small temporary test on dataset
        double[][] temp = new double[50][18];
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 17; j++) {
                temp[i][j] = data[i][j];
//                    System.out.print(temp[i][j] + " ");
            }
//                System.out.println();
        }
//        System.out.println("Dataset test = ");
        double[][] temp_test = new double[50][18];
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 17; j++) {
                temp_test[i][j] = data_test[i][j];
//                System.out.print(temp_test[i][j] + " ");
            }
//            System.out.println();
        }
        ArrayList<Integer> featureArr = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            featureArr.add(i, i);
        }
        for (int i = 0; i < temp.length; i++) {
            temp[i][temp[0].length - 1] = label_temp[i];
        }
        // Train the Model
        DecisionTreeClassifier Dtree = new DecisionTreeClassifier(tree, temp, label_temp);
        Node root = Dtree.buildTree(temp, 0, featureArr);
//        System.out.println("root.getFeatureIndex() : " + root.getChildrenNodes());
//        System.out.println("root on Main : " + root.getFeatureIndex());
        System.out.println("Decision tree Built Successfully  * o *");

        // Test the Model
        double[] predictedLabels = new double[temp_test.length];
        System.out.print("predicted Labels : ");
        for (int i = 0; i < temp_test.length; i++) {
            predictedLabels[i] = Dtree.makePrediction(temp_test, root, featureArr , i);
        }
        for (int i = 0; i < predictedLabels.length; i++) {
            System.out.print(predictedLabels[i] + " ");
        }
        int[] label_test_temporary = new int[predictedLabels.length];
        for (int i = 0; i < predictedLabels.length; i++) {
            label_test_temporary[i] = label_test_temp[i];
            System.out.print(label_test_temporary[i] + " ");
        }
        // Assuming accuracy_score
        double accuracy = Dtree.accuracyScore(label_test_temporary, predictedLabels);

        if (accuracy < 0.75) {
            System.out.println();
            System.out.println("Your Fucking accuracy is = " + (accuracy * 100) + " % ");
            System.out.println("""
                    Holy\s
                    \t Bloody\s
                    \t\t Fucking\s
                    \t\t\t Damn\s
                    \t\t\t\t shit\s
                    \t\t\t\t\t body !\s
                     This shit have less than 75% accuracy !!!!!\s""");
        } else {
            System.out.println(" ***  Congratulations *o*  ***");
            double mydouble = accuracy * 100.0;
            System.out.printf("Your Algorithm has %.2f ", mydouble);
            System.out.println(" % " + " accuracy !!! \n");
        }


//		double[][] X = new double[data.length][data[0].length - 1];
//		double[] Y = new double[data.length];
//		for (int i = 0; i < data.length; i++) {
//			System.arraycopy(data[i], 0, X[i], 0, data[i].length - 1);
//			Y[i] = data[i][data[i].length - 1];
//		}
//
//		int randomState = 41;
//		double[][] X_train, X_test;
//		double[] Y_train, Y_test;
//
//		// Assuming train_test_split is similar to Python's sklearn.model_selection.train_test_split
//		trainTestSplit(X, Y, 0.2, randomState, X_train, X_test, Y_train, Y_test);
//
//		// Fit the model
//		DecisionTreeClassifier classifier = new DecisionTreeClassifier(3, 3);
//		classifier.fit(X_train, Y_train);
//		classifier.printTree();
//
//		double[] Y_pred = classifier.predict(X_test);
//		System.out.println(Arrays.toString(Y_pred));

        // Example usage with a list of children Nodes
//        Node child1 = new Node(new double[]{1.0, 1.0, 0.0 , 2.0 , 3.0 , 3.0});
//        Node child2 = new Node(new double[]{1.0, 1.0, 1.0 , 5.0});
//        Node Parent = new Node(new double[]{0} , 0 , new double[]{1.0, 0.0, 1.0, 2.0, 3.0});
//        Parent.addChild(child1);
//        Parent.addChild(child2);
//		Tree Test_tree = new Tree(Parent);
//        Test_tree.informationGain(Parent);
//        System.out.println("information gain of " + Arrays.toString(Parent.getValue()) + " Node is : " + Parent.getInfoGain());
//        Test_tree.informationGain(child1);
//        System.out.println("information gain of " + Arrays.toString(child1.getValue()) + " Node is : " + child1.getInfoGain());
    }

    //reads csv file line by line and passes an array of its data's
    public static double[][] readCSV(String filePath, String[] colNames) {
        List<double[]> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double[] row = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    row[i] = Double.parseDouble(values[i]);
                }
                dataList.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] data = new double[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }

        return data;
    }
}
