package cn.ac.ict.alpha.activities.stride;

import android.content.Context;

import java.util.ArrayList;

import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.FloatVector;

/**
 * Author: saukymo
 * Date: 9/28/16
 */

public class StrideEvaluation {
public static String rank(Context context, ArrayList<FloatVector> accVector)
{
    int step = Pedometer.getStrideNumbers(Pedometer.genKalman(Pedometer.genOneDimension(accVector)));
    String[] rank = context.getResources().getStringArray(R.array.rank);
    if(step>=90)
    {
        return rank[0];
    }
    if(step>=70){
        return rank[1];
    }
    if(step>=50)
    {
        return rank[2];
    }
    else
    {
        return rank[3];
    }
}

//    static public String evaluation(History history,Context context){
//        boolean isAcc = FALSE;
//        ArrayList<FloatVector> accFloatVectors = null;
//        ArrayList<FloatVector> gyroFloatVectors = null;
//        int type = 0;
//        try {
//            FileReader reader = new FileReader(history.filePath);
//            BufferedReader br = new BufferedReader(reader);
//            String line = br.readLine();
//            if(line.trim().equals("Stride Walking"))
//            {
//                type = 1;
//            }
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
//        return String.valueOf(type);
////        return context.getString(R.string.default_feedback);
//    }

}
