package kobaj;

public class DTreeClassifier {

    float [][]data;
    float []labels;
    public DTreeClassifier(float [] [] data , float [] labels)
    {

    }
    float [] predictAll(float[][] , int depth);
    float accuracy(int [] labels , int [] labels_predicted);
}
