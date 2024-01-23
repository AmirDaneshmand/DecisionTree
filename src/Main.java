public class Main {

    public static void main(String[] args) {
//		// Load your dataset
//		String[] colNames = {"HighBP", "HighChol", "CholCheck", "Smoker", "Stroke", "HeartDiseaseorAttack",
//				"PhysActivity", "Fruits", "Veggies", "HvyAlcoholConsump", "AnyHealthcare",
//				"NoDocbcCost", "GenHlth", "DiffWalk", "Sex", "Education", "Income"};
//
//		double[][] data = readCSV("../Data/feature_train.csv", colNames);
//		double[][] label = readCSV("../Data/label_train.csv" , colNames);
//
//		// Run ID3
//		DecisionTreeClassifier Dtree = new DecisionTreeClassifier(3, 3);
//		Tree tree = new Tree();
//		Node root = Dtree.buildTree(data, 0);
//		System.out.println("Generated decision tree:");
//
//		// Train-Test split
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
//		// Test the model
//		double[] Y_pred = classifier.predict(X_test);
//		System.out.println(Arrays.toString(Y_pred));
//
//		// Assuming accuracy_score is similar to Python's sklearn.metrics.accuracy_score
//		double accuracy = accuracyScore(Y_test, Y_pred);
//		System.out.println("Accuracy: " + accuracy);

        //first test entropy and information Gain
        Tree tree = new Tree();
        double[] SampleParentArray = new double[4];
        SampleParentArray[0] = 1;
        SampleParentArray[1] = 1;
        SampleParentArray[2] = 2;
        SampleParentArray[3] = 3;

        double[] SampleChildArray = new double[5];
        SampleChildArray[0] = 1;
        SampleChildArray[1] = 1;
        SampleChildArray[2] = 1;
        SampleChildArray[3] = 2;
        SampleChildArray[4] = 3;

        double[] Sample2ChildArray = new double[5];
        Sample2ChildArray[0] = 1;
        Sample2ChildArray[1] = 2;
        Sample2ChildArray[2] = 2;
        Sample2ChildArray[3] = 3;
        Sample2ChildArray[4] = 3;

        System.out.println("information gain = " + tree.informationGain(SampleChildArray, SampleParentArray));
        System.out.println("information gain = " + tree.informationGain(Sample2ChildArray, SampleParentArray));
//	}


//	public static double[][] readCSV(String filePath, String[] colNames) {
//		List<double[]> dataList = new ArrayList<>();
//
//		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//			String line;
//			br.readLine(); // skip header
//			while ((line = br.readLine()) != null) {
//				String[] values = line.split(",");
//				double[] row = new double[values.length];
//				for (int i = 0; i < values.length; i++) {
//					row[i] = Double.parseDouble(values[i]);
//				}
//				dataList.add(row);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		double[][] data = new double[dataList.size()][];
//		for (int i = 0; i < dataList.size(); i++) {
//			data[i] = dataList.get(i);
//		}
//
//		return data;
//	}

//	public static void trainTestSplit(
//			double[][] X, double[] Y, double testSize, int randomState,
//			double[][] X_train, double[][] X_test, double[] Y_train, double[] Y_test
//	) {
//		// Implement train-test split logic similar to Python's sklearn.model_selection.train_test_split
//		// You can use a random seed for reproducibility (similar to random_state in Python)
//		// The implementation depends on the logic you want to follow for the split.
//	}

//	public static double accuracyScore(double[] Y_true, double[] Y_pred) {
//		// Implement accuracy score calculation similar to Python's sklearn.metrics.accuracy_score
//		// You need to compare Y_true and Y_pred and calculate the accuracy.
//		return 0.0; // Placeholder, replace with actual logic
//	}
//}

//public class Main
//{

        /**
         * Main jump
         *
         * @param args
         */
//	public static void main(String[] args)
//	{
//		System.out.println("begin");
//
//		// conditionas are possible values for attributes
//		Condition gr_20 = new Condition(">20");
//		Condition gr_50 = new Condition(">50");
//		Condition gr_80 = new Condition(">80");
//		Condition yes = new Condition("yes");
//		Condition no = new Condition("no");
//		Condition scattered = new Condition("scattered");
//		Condition overcast = new Condition("overcast");
//
//		// these attributes are overall, all possible values
//		Attribute[] real_attributes = new Attribute[3];
//		real_attributes[0] = new Attribute("%", gr_20, gr_50, gr_80);
//		real_attributes[1] = new Attribute("rained", yes, no);
//		real_attributes[2] = new Attribute("cloudy", scattered, no, overcast);
//
//		// these attributes will be linked to examples
//		Attribute[] example_attributes = new Attribute[6];
//		example_attributes[0] = new Attribute("%", gr_20, gr_50, gr_80);
//		example_attributes[1] = new Attribute("%", gr_20, gr_50, gr_80);
//		example_attributes[2] = new Attribute("%", gr_20, gr_50, gr_80);
//		example_attributes[3] = new Attribute("rained", yes, no);
//		example_attributes[4] = new Attribute("cloudy", scattered, no, overcast);
//		example_attributes[5] = new Attribute("rain_today", yes, no);
//
//		// examples contain attributes and a value
//		// here we will build a decision tree based on 'weather'
//		Example[] examples = new Example[10];
//		// these are in the order of the example attributes above
//		examples[0] = new Example(example_attributes, gr_20, gr_50, no, no, scattered, yes);
//		examples[1] = new Example(example_attributes, gr_20, no, no, no, no, no);
//		examples[2] = new Example(example_attributes, no, no, no, yes, no, no);
//		examples[3] = new Example(example_attributes, gr_20, gr_50, gr_80, no, overcast, yes);
//		examples[4] = new Example(example_attributes, gr_20, gr_50, gr_80, yes, no, yes);
//		examples[5] = new Example(example_attributes, gr_20, gr_50, gr_80, yes, overcast, yes);
//		examples[6] = new Example(example_attributes, gr_20, no, no, yes, no, no);
//		examples[7] = new Example(example_attributes, no, no, no, no, no, no);
//		examples[8] = new Example(example_attributes, gr_20, no, no, no, overcast, yes);
//		examples[9] = new Example(example_attributes, gr_20, no, no, no, scattered, yes);
//
//		Attribute desired_attribute = example_attributes[example_attributes.length - 1]; // desired attribute is the last
//
//		Node<?> tree = trainDecisionTree(examples, real_attributes, yes, desired_attribute);

//		System.out.println(Dtree.toString());
//	}

        /**
         * Main learn loop that builds a decision tree
         *
         * @param examples list of input samples built from attributes and conditions
         * @param attributes list of all attributes to iterate over
         * @param default_label list of default condition for desired attribute to apply
         * @param desired_attribute the attribute to test for
         * @return
         */
//	public static Node<?> trainDecisionTree(Example[] examples, Attribute[] attributes, Condition default_label, Attribute desired_attribute)
//	{
//		// first base case
//		if (examples.length == 0)
//		{
//			System.out.println("leaf 1");
//			return new Node<Condition>(default_label, "first leaf ");
//		}
//
//		// second base case
//		ArrayList<Example> example_copy = new ArrayList<Example>();
//		for(Example e: examples)
//			example_copy.add(e);
//		Collections.sort(example_copy, new ExampleComparator());
//		if(example_copy.get(0).get_label().equals(//
//				example_copy.get(example_copy.size() - 1).get_label()))
//		{
//			System.out.println("leaf 2");
//			return new Node<Condition>(examples[0].get_label(), "second leaf ");
//		}
//
//		// third base case
//		if (attributes.length == 0)
//		{
//			Condition mode = Mode(examples);
//			System.out.println("leaf 3");
//			return new Node<Condition>(mode, "third leaf ");
//		}
//
//		// recurse
//		Attribute best = ChooseBestAttribute(examples, attributes, desired_attribute);
//		System.out.println("Finding a best: " + best.name);
//		Node<Attribute> tree = new Node<Attribute>(best, "middle node ");
//		Condition label = Mode(examples);
//
//		for (Condition c : best.possible_conditions)
//		{
//			Example[] example_i = best.satisfied(examples, c);
//			Node<?> sub_tree = trainDecisionTree(example_i, removeBest(attributes, best), label, desired_attribute);
//			sub_tree.identifier += c.toString();
//
//			tree.children.add(sub_tree);
//		}
//
//		return tree;
//	}

        /**
         * Takes in an array of attributes and removes one.
         *
         * @param attributes array of attributes
         * @param best attribute to be removed
         * @return an array of attributes
         */
//	public static Attribute[] removeBest(Attribute[] attributes, Attribute best)
//	{
//		ArrayList<Attribute> modified_attributes = new ArrayList<Attribute>();
//
//		for (Attribute a : attributes)
//		{
//			if (!a.equals(best))
//				modified_attributes.add(a);
//		}
//
//		return modified_attributes.toArray(new Attribute[0]);
//	}

        /**
         * Calculates the mathematical mode condition (based on desired attribute, aka, last attribute in example).
         *
         * @param examples
         * @return condition that appears most often
         */
//	public static Condition Mode(Example[] examples)
//	{
//		Condition max_condition = null;
//		int max_count = 0;
//
//		// not the most efficient
//		for (Example e : examples)
//		{
//			int local_count = 0;
//			for (Example inner_e : examples)
//			{
//				if (inner_e.get_label().equals(e.get_label()))
//					local_count++;
//			}
//
//			if (local_count > max_count)
//			{
//				max_count = local_count;
//				max_condition = e.get_label();
//			}
//		}
//
//		System.out.println("Mode value: " + max_condition);
//
//		return max_condition;
//	}

        /**
         * This will calculate the Gain and Remainder of attributes and picks the best to recurse over
         *
         * @param examples
         * @param attributes
         * @param desired_attribute
         * @return
         */
//	public static Attribute ChooseBestAttribute(Example[] examples, Attribute[] attributes, Attribute desired_attribute)
//	{
//
//		Attribute best = null;
//		double smallest_double = Double.MAX_VALUE;
//
//		for (Attribute a : attributes)
//		{
//			double remain = Remain(examples, a, desired_attribute);
//			if (best == null || remain < smallest_double)
//			{
//				smallest_double = remain;
//				best = a;
//			}
//		}
//
//		return best;
//	}

        /**
         * Computes the Remain,
         * sum(p/t * I(pi/ti, ni/ti));
         *
         * @param examples
         * @param attribute
         * @param desired_attribute
         * @return a double value for the remain
         */
//	public static double Remain(Example[] examples, Attribute attribute, Attribute desired_attribute)
//	{
//		System.out.println("\nrunning a rem for: " + attribute.name);
//
//		double total_examples = examples.length;
//
//		double total = 0;
//
//		// figure out each attribute
//		for (Condition major_condition : attribute.possible_conditions)
//		{
//			System.out.println("possible condition: " + major_condition.name);
//
//			Example[] sub_examples = attribute.satisfied(examples, major_condition);
//			Double total_sub_examples = (double) sub_examples.length;
//			double precident = total_sub_examples / total_examples;
//
//			System.out.println("Number satisfied: " + total_sub_examples);
//
//			// figure out the igain
//			ArrayList<Double> sub_example_count = new ArrayList<Double>();
//			for (Condition c : desired_attribute.possible_conditions)
//			{
//				Example[] examples_c = desired_attribute.satisfied(sub_examples, c);
//				System.out.println("number of passing sub examples: " + examples_c.length);
//				sub_example_count.add(examples_c.length / total_sub_examples);
//			}
//			double i_gain = IGain(sub_example_count.toArray(new Double[0]));
//			System.out.println("iGain: " + i_gain);
//
//			double total_local_value = precident * i_gain;
//
//			total += total_local_value;
//		}
//
//		System.out.println("got a result of: " + total);
//
//		return total;
//	}


        /**
         * computers the I value of Remain
         * sum(-p(vi)logbase2ofp(vi))
         *
         * @param ds which comes from remain
         * @return double value of I
         */
//	public static double IGain(Double... ds)
//	{
//		double final_value = 0;
//		for (double d : ds)
//		{
//			if (d != 0.0)
//				final_value += -d * Math.log(d) / Math.log(2.0);
//		}
//
//		if (Double.isNaN(final_value))
//			final_value = 0;
//
//		return final_value;
//	}

//}

//class ExampleComparator implements Comparator<Example>
//{
//	@Override
//	public int compare(Example o1, Example o2)
//	{
//		return (o1.get_label()).compareTo(o2.get_label());
//	}
//}

//class Example
//{
//	public Attribute[] my_attributes;
//	public Condition[] my_conditions;
//
//	public Condition get_label()
//	{
//		return my_conditions[my_conditions.length - 1];
//	}
//
//	public Example(Attribute[] const_attributes, Condition... conditions)
//	{
//		my_attributes = const_attributes;
//		my_conditions = conditions;
//	}
//
//	public boolean check_condition(Attribute attribute, Condition c)
//	{
//		for (int i = 0; i < my_attributes.length; i++)
//		{
//			if (my_attributes[i].equals(attribute))
//				if (my_conditions[i].equals(c))
//				{
//					return true;
//				}
//		}
//
//		return false;
//	}
//}

//class Attribute
//{
//	@Override
//	public boolean equals(Object obj)
//	{
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Attribute other = (Attribute) obj;
//		if (name == null)
//		{
//			if (other.name != null)
//				return false;
//		}
//		else if (!name.equals(other.name))
//			return false;
//		return true;
//	}
//
//	public String name;
//
//	public Condition[] possible_conditions;
//
//	public Attribute(String name, Condition... possible_conditions)
//	{
//		this.name = name;
//		this.possible_conditions = possible_conditions;
//	}
//
//	public Example[] satisfied(Example[] examples, Condition c)
//	{
//		ArrayList<Example> satisfied_examples = new ArrayList<Example>();
//
//		for (Example e : examples)
//		{
//			if (e.check_condition(this, c))
//				satisfied_examples.add(e);
//		}
//
//		return satisfied_examples.toArray(new Example[0]);
//	}
//
//	@Override
//	public String toString()
//	{
//		return name;
//	}
//}

//class Condition
//{
//	public String name;
//
//	@Override
//	public int hashCode()
//	{
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		return result;
//	}
//
//	public int compareTo(Condition get_label)
//	{
//		return (get_label.name.compareTo(name));
//	}
//
//	@Override
//	public boolean equals(Object obj)
//	{
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Condition other = (Condition) obj;
//		if (name == null)
//		{
//			if (other.name != null)
//				return false;
//		}
//		else if (!name.equals(other.name))
//			return false;
//		return true;
//	}
//
//	public Condition(String name)
//	{
//		this.name = name;
//	}
//
//	@Override
//	public String toString()
//	{
//		return name;
//	}
//}

//class Node<T>
//{
//	public String identifier;
//
//	public T data;
//	public Node<?> parent = null;
//	public List<Node<?>> children;
//
//	public Node(T data, String ident)
//	{
//		this.identifier = ident;
//		this.data = data;
//		children = new ArrayList<Node<?>>();
//	}
//
//	public Node(T data)
//	{
//		this(data, "unset");
//	}
//
//	public String toString(String tabs)
//	{
//		String childs = "";
//		for (Node<?> n : children)
//			childs += n.toString(tabs + "-");
//
//		if (childs.equals(""))
//			childs = "no children.";
//
//		return "\n" + tabs + "Node: " + identifier + " value: " + data.toString() + " children: " + childs;
//	}
//
//	@Override
//	public String toString()
//	{
//		return toString("");
//	}
//
//	public T getValue() {
//		return data;
//	}
    }
}
