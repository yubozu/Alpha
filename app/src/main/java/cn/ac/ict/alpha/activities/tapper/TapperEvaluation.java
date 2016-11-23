package cn.ac.ict.alpha.activities.tapper;

import android.content.Context;

import java.util.ArrayList;

import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.MathUtils;

/**
 * Author: saukymo
 * Date: 9/28/16
 */

public class TapperEvaluation {
    public static double evaluation(ArrayList<Boolean> indicatior, ArrayList<Long> timestamps)
    {
        // TODO:
        return indicatior.size();
    }
    public static String rank(Context context, ArrayList<Boolean> indicatior)
    {
        int count = indicatior.size();
        String[] rank = context.getResources().getStringArray(R.array.rank);
        if(count>=80)
        {
            return rank[0];
        }
        if(count>=60)
        {
            return rank[1];
        }
        if(count>=40)
        {
            return rank[2];
        }
        else{
            return rank[3];
        }
    }
    public static double eva_order(ArrayList<Boolean> indicatior)
    {
        int same = 0;
        int oppo = 0;
        boolean prevPress = indicatior.get(0);
        for(int i = 1;i<indicatior.size();i++)
        {
            boolean currentPress = indicatior.get(i);
            if(currentPress==prevPress)
            {
                same++;
            }else{
                oppo++;
            }
        }
        return (double)oppo/(double)(same+oppo);
    }
    public static double a_harmonic(ArrayList<Long> timestamps)
    {
        ArrayList<Long> times = new ArrayList<>();
        for(int i=1;i<times.size();i++)
        {
            times.add(timestamps.get(i)-times.get(i-1));
        }
        double mean  = MathUtils.mean_long(times);
        double std = MathUtils.std_long(times);
        return 0.0d;
    }

}
