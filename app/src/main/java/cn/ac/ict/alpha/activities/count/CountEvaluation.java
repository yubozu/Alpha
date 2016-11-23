package cn.ac.ict.alpha.activities.count;

import android.content.Context;

import java.util.ArrayList;

import cn.ac.ict.alpha.R;

public class CountEvaluation {
    public static double CountEvaluation(boolean isRight, ArrayList<String> result) {
        double score = 0;
        if(isRight)
        {
            score = (5.0-(result.size()-1))/5.0;
        }
        return score;
    }
    public static String rank(Context context, boolean isRight, ArrayList<String> result)
    {
        double score =  CountEvaluation(isRight, result);
        String[] rank_str = context.getResources().getStringArray(R.array.rank);
        if (score>=0.8)
        {
            return rank_str[0];
        }
        if(score>=0.6)
        {
            return rank_str[1];
        }
        if(score>=0.2)
        {
            return rank_str[2];
        }else{
            return rank_str[3];
        }
    }
//    static public String evaluation(History history,Context context){
//
////        Do something here.
//
//        boolean isRight = false;
//        boolean isSound = false;
//        String rightAnswer = "";
//        String linet = "";
//        ArrayList<String> tryInput = new ArrayList<String>();
//
//        try{
//            FileReader reader = new FileReader(history.filePath);
//            BufferedReader br = new BufferedReader(reader);
//            String line = br.readLine();
//            linet = line;
//            String[] strings = line.trim().split(";");
//            rightAnswer = strings[0].trim();
//            isRight = strings[1].trim().equals("0")?false:true;
//            isSound = strings[2].trim().equals("0")?false:true;
//            for(int i=3;i<strings.length;i++){
//                tryInput.add(strings[i]);
//            }
//
//        }catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        String result = "";
//        result += isSound?context.getString(R.string.count_test_version_sound):context.getString(R.string.count_test_version_picture);
//        result += isRight?context.getString(R.string.countt_input_rightanswer):context.getString(R.string.countt_input_wronganswer);
//        result += context.getString(R.string.countt_right_answer)+rightAnswer+"\n";
//        result += context.getString(R.string.countt_try_input);
//        for(String s:tryInput){
//            result += s+"\n";
//        }
////        return rightAnswer+":"+isRight+":"+linet;
//        return result;
//
//    }
}
