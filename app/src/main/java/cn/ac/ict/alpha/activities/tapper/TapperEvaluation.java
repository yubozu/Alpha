package cn.ac.ict.alpha.activities.tapper;

import java.util.ArrayList;

/**
 * Author: saukymo
 * Date: 9/28/16
 */

public class TapperEvaluation {
    public static float evaluation(ArrayList<Boolean> indicatior, ArrayList<Long> timestamps)
    {
        // TODO:
        return 0.0f;
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
    public static double eva_harmonic(ArrayList<Boolean> indicatior, ArrayList<Long> timestamps)
    {
        return 0.0d;
    }

}
