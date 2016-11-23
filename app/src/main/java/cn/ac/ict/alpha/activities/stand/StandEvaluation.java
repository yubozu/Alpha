package cn.ac.ict.alpha.activities.stand;

import android.content.Context;

import java.util.ArrayList;

import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.FloatVector;
import cn.ac.ict.alpha.Utils.MathUtils;

/**
 * Author: saukymo
 * Date: 9/28/16
 */

public class StandEvaluation {
public static String rank(Context context, ArrayList<FloatVector> accVectors)
{
    String[] rank = context.getResources().getStringArray(R.array.rank);
    float accFloat = 0f;
    if(accVectors!=null&&!accVectors.isEmpty())
    {
        float[] stds = MathUtils.std(accVectors);
        accFloat = MathUtils.max(stds)[1];
    }
    if(accFloat<=0.8)
    {
        return rank[0];
    }
    if(accFloat<=1)
    {
        return rank[1];
    }
    if(accFloat<=1.2)
    {
        return rank[2];
    }else
    {
        return rank[3];
    }
}

//    static public String evaluation(History history,Context context){
//        boolean isRight = false;
//        boolean isAcc = FALSE;
//        ArrayList<FloatVector> accFloatVectors = null;
//        ArrayList<FloatVector> gyroFloatVectors = null;
////        Do something here.
//        try {
//            FileReader reader = new FileReader(history.filePath);
//            BufferedReader br = new BufferedReader(reader);
//            String line = br.readLine();
//            isRight = Boolean.parseBoolean(line);
//            line = br.readLine();
//            while(line!=null && !line.isEmpty())
//            {
//                if(line.startsWith("ACC"))
//                {
//                    accFloatVectors = new ArrayList<>();
//                    isAcc = true;
//                }else if (line.startsWith("GYRO"))
//                {
//                    gyroFloatVectors = new ArrayList<>();
//                    isAcc = false;
//                }else
//                {
//                    String[] t = line.split(",");
//                    FloatVector vector = new FloatVector();
//                    vector.timeStamp = Long.parseLong(t[0].trim());
//                    vector.x = Float.parseFloat(t[1].trim());
//                    vector.y = Float.parseFloat(t[2].trim());
//                    vector.z = Float.parseFloat(t[3].trim());
//                    if(isAcc)
//                    {
//                        accFloatVectors.add(vector);
//                    }else
//                    {
//                        gyroFloatVectors.add(vector);
//                    }
//
//                }
//                line = br.readLine();
//
//            }
//
//        }catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        return context.getString(R.string.default_feedback);
//    }

}
