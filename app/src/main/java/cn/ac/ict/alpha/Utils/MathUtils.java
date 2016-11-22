package cn.ac.ict.alpha.Utils;

import java.util.ArrayList;

/**
 * Created by yxd on 21/11/2016.
 */

public class MathUtils {
    public static float[] mean(ArrayList<FloatVector> data) {
        float[][] xyz = getXYZlist(data);
        float[] means = new float[3];
        float x_sum = 0f, y_sum = 0f, z_sum = 0f;
        for (int i = 0; i < data.size(); i++) {
            x_sum += xyz[0][i];
            y_sum += xyz[1][i];
            z_sum += xyz[2][i];
        }
        means[0] = x_sum/data.size();
        means[1] = y_sum/data.size();
        means[2] = z_sum/data.size();
        return means;
    }
    public static double mean_long(ArrayList<Long> data) {

        long sum = 0;
        for (int i = 0; i < data.size(); i++) {
           sum+=data.get(i);
        }
        return (double)sum/(double)data.size();
    }
    public static double std_long(ArrayList<Long> data)
    {
        double mean  = mean_long(data);
        double delta = 0d;
        for(int i=0;i<data.size();i++)
        {
            delta+=Math.abs(data.get(i)-mean);
        }
        return Math.sqrt(delta);
    }
    public static float[] std(ArrayList<FloatVector> data)
    {
        float[][] xyz = getXYZlist(data);
        float[] means = mean(data);
        float[] stds = new float[3];
        float[] delta = new float[3];
        for(int i=0;i<data.size();i++)
        {
            delta[0] += Math.abs(xyz[0][i]-means[0]);
            delta[1] += Math.abs(xyz[1][i]-means[1]);
            delta[2] += Math.abs(xyz[2][i]-means[2]);
        }
        stds[0] = (float)Math.sqrt(delta[0]/data.size());
        stds[1] = (float)Math.sqrt(delta[1]/data.size());
        stds[2] = (float)Math.sqrt(delta[2]/data.size());
        return stds;
    }

    public static float[][] getXYZlist(ArrayList<FloatVector> data) {
        float[][] result = new float[3][data.size()];
        float[] xs = new float[data.size()];
        float[] ys = new float[data.size()];
        float[] zs = new float[data.size()];
        for (int i = 0; i < data.size(); i++) {
            xs[i] = data.get(i).x;
            ys[i] = data.get(i).y;
            zs[i] = data.get(i).z;
        }
        result[0] = xs;
        result[1] = ys;
        result[2] = zs;
        return result;
    }
    public static float[] max(float[] data)
    {
        float[] result = new float[2];
        result[0] = 0;
        result[1] = data[0];
        for(int i=1;i<data.length;i++)
        {
            if(data[i]>data[(int)result[0]])
            {
                result[0] = i;
                result[1] = data[i];
            }
        }
        return result;
    }
    public static float[] min(float[] data)
    {
        float[] result = new float[2];
        result[0] = 0;
        result[1] = data[0];
        for(int i=1;i<data.length;i++)
        {
            if(data[i]<data[(int)result[0]])
            {
                result[0] = i;
                result[1] = data[i];
            }
        }
        return result;
    }
}
