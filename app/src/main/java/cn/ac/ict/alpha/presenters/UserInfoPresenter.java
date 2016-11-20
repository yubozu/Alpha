package cn.ac.ict.alpha.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cn.ac.ict.alpha.Entities.UserInfoEntity;
import cn.ac.ict.alpha.activities.UserInfoActivity;
import cn.ac.ict.alpha.models.ApiClient;
import rx.Subscriber;

/**
 * Author: saukymo
 * Date: 11/19/16
 */

public class UserInfoPresenter {
    public static final String TAG = "UserInfoPresenter";

    private UserInfoActivity mUserInfoView;

    public UserInfoPresenter(UserInfoActivity userInfoView) {
        mUserInfoView = userInfoView;
    }

    public void saveUserInfo(String phoneNumber, Boolean gender, Integer age) {
        mUserInfoView.onStartSave();
        startSave(new UserInfoEntity(phoneNumber, age, gender));
    }

    private void startSave(UserInfoEntity userInfo) {
        Subscriber<UserInfoEntity> subscriber = new Subscriber<UserInfoEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
                mUserInfoView.onSaveFailed();
            }

            @Override
            public void onNext(UserInfoEntity userInfoEntity) {
                String status = userInfoEntity.getStatus();
                if (status.equals("OK")) {
                    saveUserInfo(userInfoEntity);
                    mUserInfoView.onSaveSuccess();
                } else {
                    mUserInfoView.toast(userInfoEntity.getError());
                    mUserInfoView.onSaveFailed();
                }
            }
        };

        ApiClient.getInstance().updateUserInfo(subscriber, getUserId(), userInfo);
    }

    private Integer getUserId(){
        SharedPreferences sharedPreferences = mUserInfoView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    public void loadUserInfo() {
        SharedPreferences sharedPreferences = mUserInfoView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Boolean gender = sharedPreferences.getBoolean("gender", false);
        Integer age = sharedPreferences.getInt("age", 0);
        String phoneNumber = sharedPreferences.getString("phone_number", "");
        mUserInfoView.setUserInfo(phoneNumber, age, gender);
    }

    private void saveUserInfo(UserInfoEntity userInfoEntity) {
        Log.d(TAG, "userId: " + userInfoEntity.getId());
        SharedPreferences.Editor editor = mUserInfoView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE).edit();
        editor.putInt("user_id", userInfoEntity.getId());
        editor.putInt("age", userInfoEntity.getAge());
        editor.putBoolean("gender", userInfoEntity.getGender());
        editor.putString("phone_number", userInfoEntity.getPhone_number());
        editor.apply();
    }
}
