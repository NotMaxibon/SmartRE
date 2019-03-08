package it.smartre.smartrev20;
/* by cjf 1801615 352871242@qq.com*/
/**
 * Face feature (512-dimensional feature value)
 * Similarity takes the Euclidean distance between feature vectors.
 */
public class FaceFeature {
    public static final int DIMS=512;
    private float fea[];
    FaceFeature(){
        fea=new float[DIMS];
    }
    public float[] getFeature(){
        return fea;
    }
    //Compare the similarity between the current feature and another feature
    public double compare(FaceFeature ff){
        double dist=0;
        for (int i=0;i<DIMS;i++)
            dist+=(fea[i]-ff.fea[i])*(fea[i]-ff.fea[i]);
        dist=Math.sqrt(dist);
        return dist;
    }
}
