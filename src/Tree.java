public class Tree {
    int depth;

    public int getDepth() {
        return depth;
    }

    public void createTree(float[][] data, float[] labels){}

    public float Entropy(float[] labels) {
        float value = 0;
        for (float label : labels) {
            value = (float) (-label * Math.log(label) / Math.log(2.0));
        }
        return value;
    }

    public float iGain(float pEntropy, float[] weight, float[] entropies) {


        float sum =0;
        for (int i = 0;i<weight.length;i++){
            sum += weight[i]*entropies[i];
        }

        return pEntropy-sum;
    }
}
