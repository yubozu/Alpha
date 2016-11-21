package cn.ac.ict.alpha.presenters;

import android.content.Context;
import android.content.SharedPreferences;

import cn.ac.ict.alpha.Utils.FileUtils;
import cn.ac.ict.alpha.activities.MainActivity;

/**
 * Author: yangxiaodong
 * Date: 11/16/16
 */

public class MainPresenter {
    public static final String TAG = "MainPresenter";

    public MainActivity mMainView;

    public MainPresenter(MainActivity mainView) {
        mMainView = mainView;
    }

    public void startExam() {
        SharedPreferences sharedPreferences = mMainView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Integer age = sharedPreferences.getInt("age", 0);
        if (age > 0) {
            prepareEva();
        }
        mMainView.onStartExam(age == 0);
    }

    private void prepareEva() {
        SharedPreferences sharedPreferences = mMainView.getSharedPreferences("Alpha", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("startTime",System.currentTimeMillis());
        editor.putString("memoryScore","-1");
        editor.putString("standScore","-1");
        editor.putString("faceScore","-1");
        editor.putString("soundScore","-1");
        editor.putString("tappingScore","-1");
        editor.putString("strideScore","-1");
        editor.putBoolean("takingMed",false);
        editor.putInt("lastTaken",0);
        editor.apply();
        FileUtils.initFileDir(mMainView);
    }

    public void logout() {
        SharedPreferences.Editor editor = mMainView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE).edit();
        editor.remove("password");
        editor.remove("user_id");
        editor.remove("age");
        editor.remove("gender");
        editor.apply();
    }
}
