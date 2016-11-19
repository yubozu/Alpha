package cn.ac.ict.alpha.presenters;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.ac.ict.alpha.Utils.BaseResult;
import cn.ac.ict.alpha.activities.ResultActivity;

/**
 * Author: yangxiaodong
 * Date: 11/16/16
 */

public class ResultPresenter {
    public static final String TAG = "ResultPresenter";

    public ResultActivity mResultView;

    public ResultPresenter(ResultActivity resultActivity) {
        mResultView = resultActivity;
    }

    public void loadTestData() {
        //TODO: Load test data from shared preference
        String[] scoreNames={"memoryScore","standScore","faceScore","soundScore","tappingScore","strideScore"};
        SharedPreferences sharedPreferences = mResultView.getSharedPreferences("Alpha", Context.MODE_PRIVATE);
        ArrayList<BaseResult> results = new ArrayList<>();
        for(int i=0;i<scoreNames.length;i++)
        {
            float score = Float.parseFloat(sharedPreferences.getString(scoreNames[i],"-1"));
            if(score==-1)
                continue;
            int type = i;
            String resultPath = sharedPreferences.getString(scoreNames[i],null);
            results.add(new BaseResult(resultPath,score,type));
        }
        long start = sharedPreferences.getLong("startTime",0l);
        long end = sharedPreferences.getLong("endTime",0l);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        mResultView.onTestDataLoaded(results,sdf.format(new Date(start)),sdf.format(new Date(end)));
    }

}
