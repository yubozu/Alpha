package cn.ac.ict.alpha.presenters;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.HashMap;

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

    public HashMap<String,Object> getUserInfo() {
        // TODO: A test version, should get the info from SharePreference
        String gender= "gender";
        String birthday = "birthday";
        HashMap<String,Object> userInfo = new HashMap<>();
        userInfo.put(gender,1);
        userInfo.put(birthday,"1991-02-02");
        return userInfo;
    }

    public void saveInfo(int gender, String birthday) {
        //TODO: Save current info into SharePreference and upload to cloud
        mMainView.onSaveOK();
    }

    public void prepareEva() {
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
        String dir = mMainView.getFilesDir().getAbsolutePath()+"/tempData";
        File fdir = new File(dir);
        if(fdir.exists())
        {
            fdir.delete();
        }
        fdir.mkdirs();
        mMainView.onPrepared();
    }

//    public void login(String userName, String pwd) {
//        if (StringUtils.checkUserName(userName)) {
//            if (StringUtils.checkPassword(pwd)) {
//                mLoginView.onStartLogin();
//                startLogin(userName, pwd);
//            } else {
//                mLoginView.onPasswordError();
//            }
//        } else {
//            mLoginView.onUserNameError();
//        }
//    }
//
//    private void startLogin(String userName, String pwd) {
////        调用model的登录模块检查用户名密码是否正确
//        mLoginView.onLoginSuccess();
//    }

//    private EMCallBackAdapter mEMCallBack = new EMCallBackAdapter() {
//
//        @Override
//        public void onSuccess() {
//            ThreadUtils.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mLoginView.onLoginSuccess();
//                }
//            });
//        }
//
//        @Override
//        public void onError(int i, String s) {
//            ThreadUtils.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mLoginView.onLoginFailed();
//                }
//            });
//        }
//    };
}
