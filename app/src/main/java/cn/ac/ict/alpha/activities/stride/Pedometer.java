package cn.ac.ict.alpha.activities.stride;

import java.util.ArrayList;

import cn.ac.ict.alpha.Utils.FloatVector;
import jama.Matrix;
import jkalman.JKalman;

/**
 * Created by zhongxi on 2016/11/22.
 */

public class Pedometer {

    public static ArrayList<Double> genOneDimension(ArrayList<FloatVector> accList){
        ArrayList<Double> result = new ArrayList<>();
        double temp;
        for(int i=0;i<accList.size();i++) {
            temp = Math.sqrt(accList.get(i).x *accList.get(i).x + accList.get(i).y * accList.get(i).y + accList.get(i).z *accList.get(i).z);
            result.add(temp);
        }
        return result;
    }

    public static ArrayList<Double> genKalman(ArrayList<Double> accList){
        ArrayList<Double> result = new ArrayList<>();
        try {
            JKalman kalman = new JKalman(1, 1);
            double[][] A= new double[][]{{1}};
            double[][] H= new double[][]{{1}};
            double[][] Q= new double[][]{{1}};
            double[][] R= new double[][]{{6}};
            kalman.setTransition_matrix(new Matrix(A));
            kalman.setMeasurement_matrix(new Matrix(H));
            kalman.setProcess_noise_cov(new Matrix(Q));
            kalman.setMeasurement_noise_cov(new Matrix(R));
            kalman.setError_cov_post(kalman.getError_cov_post().identity());

            //开始位置
            Matrix statePost = new Matrix(1,1);
            statePost.set(0, 0, accList.get(0));
            kalman.setState_post(statePost);

            Matrix measurementZ = new Matrix(1,1);
            Matrix predictX = null;
            Matrix currectionX = null;
            for(double data : accList){
                measurementZ.set(0, 0,data);
                predictX = kalman.Predict();
                currectionX = kalman.Correct(measurementZ);
                result.add(currectionX.get(0,0));
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static int getStrideNumbers(ArrayList<Double> arrayList){
        int result = 0;
        int lastIndex = 0;
        double max = 0;
        boolean isIncreased = true;
        for(int i=1;i<arrayList.size();i++){
            if(arrayList.get(i)>=arrayList.get(i-1)){
                isIncreased = true;
            }else {
                if((isIncreased)&&(i-lastIndex>=25)&&arrayList.get(i)>10){
                    result++;
                    lastIndex = i;
                }
                isIncreased = false;
            }
            if(arrayList.get(i)>max){
                max = arrayList.get(i);
            }
        }
        return result;
    }

}
