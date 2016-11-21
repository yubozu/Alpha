package cn.ac.ict.alpha.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.ac.ict.alpha.Entities.ExamEntity;
import cn.ac.ict.alpha.Entities.UploadResponseEntity;
import cn.ac.ict.alpha.Utils.BaseResult;
import cn.ac.ict.alpha.activities.ResultActivity;
import cn.ac.ict.alpha.models.ApiClient;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Author: yangxiaodong
 * Date: 11/16/16
 */

public class ResultPresenter {
    public static final String TAG = "ResultPresenter";

    public ResultActivity mResultView;
    private Integer taskCount = 6;
    private Integer taskSuccess;
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
        long start = sharedPreferences.getLong("startTime", 0l);
        long end = sharedPreferences.getLong("endTime", 0l);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        mResultView.onTestDataLoaded(results,sdf.format(new Date(start)),sdf.format(new Date(end)));
    }

    public void upload() {
        Subscriber<UploadResponseEntity> subscriber = new Subscriber<UploadResponseEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
                mResultView.onUploadFailed();
            }

            @Override
            public void onNext(UploadResponseEntity uploadResponseEntity) {
                String status = uploadResponseEntity.getStatus();
                if (status.equals("OK")) {
                    onResponse(true);
                } else {
                    mResultView.toast(uploadResponseEntity.getError());
                    mResultView.onUploadFailed();
                }
            }
        };

        taskSuccess = 0;
        mResultView.onStartUpload();
        ApiClient.getInstance().uploadExamFiles(subscriber, loadExamEntity("memory"));
        ApiClient.getInstance().uploadExamFiles(subscriber, loadExamEntity("tapping"));
        ApiClient.getInstance().uploadExamFiles(subscriber, loadExamEntity("stride"));
        ApiClient.getInstance().uploadExamFiles(subscriber, loadExamEntity("stand"));
        ApiClient.getInstance().uploadExamFiles(subscriber, loadExamEntity("face"));
        ApiClient.getInstance().uploadExamFiles(subscriber, loadExamEntity("sound"));
    }

    private ExamEntity loadExamEntity(String examType) {
        Integer userId = getUserId();
        // TODO: 如果跳过测试，文件名为空。
        File examFile = new File(getFilePath(examType));
        Log.d(TAG, "loadExamEntity: "+getFilePath(examType));
        RequestBody file = RequestBody.create(MediaType.parse(getMediaType(examType)), examFile);
        String score = getScore(examType);
        Integer medicine = getMedicine();
        return new ExamEntity(score, examType, file, userId, medicine);
    }

    private Integer getMedicine() {
        SharedPreferences sharedPreferences = mResultView.getSharedPreferences("Alpha", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("lastTaken", 0);
    }

    private String getScore(String examType) {
        SharedPreferences sharedPreferences = mResultView.getSharedPreferences("Alpha", Context.MODE_PRIVATE);
        return sharedPreferences.getString(examType + "Score", "");
    }

    private String getMediaType(String examType) {
        switch (examType) {
            case "face":
                return "video/mp4";
            case "sound":
                return "audio/3gp";
        }
        return "text/plain";
    }

    private String getFilePath(String examType) {
        SharedPreferences sharedPreferences = mResultView.getSharedPreferences("Alpha", Context.MODE_PRIVATE);
        return sharedPreferences.getString(examType + "FilePath", "");
    }

    private void onResponse(Boolean result) {
        if (result) {
            taskSuccess += 1;
        }
        if (taskSuccess.equals(taskCount)) {
                mResultView.onUploadSuccess();
        }
    }

    private Integer getUserId(){
        SharedPreferences sharedPreferences = mResultView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}
