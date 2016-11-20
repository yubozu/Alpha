package cn.ac.ict.alpha.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cn.ac.ict.alpha.Entities.UserInfoEntity;
import cn.ac.ict.alpha.Utils.StringUtils;
import cn.ac.ict.alpha.activities.LoginActivity;
import cn.ac.ict.alpha.models.ApiClient;
import rx.Subscriber;

/**
 * Author: saukymo
 * Date: 11/14/16
 */

public class LoginPresenter {
    public static final String TAG = "LoginPresenter";

    private LoginActivity mLoginView;

    public LoginPresenter(LoginActivity loginView) {
        mLoginView = loginView;
    }

    public void autoLogin() {
        SharedPreferences sharedPreferences = mLoginView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phone_number", "");
        String password = sharedPreferences.getString("password", "");
        mLoginView.setAuthInfo(phoneNumber, password);
        if (! phoneNumber.equals("") && !password.equals("")) {
            login(phoneNumber, password);
        }
    }

    public void login(String phoneNumber, String password) {
        if (StringUtils.checkPhoneNumber(phoneNumber)) {
            if (StringUtils.checkPassword(password)) {
                mLoginView.onStartLogin();
                savePassWord(password);
                startLogin(new UserInfoEntity(phoneNumber, password));
            } else {
                mLoginView.onPasswordError();
            }
        } else {
            mLoginView.onPhoneNumberError();
        }
    }

    private void startLogin(UserInfoEntity userInfo) {
        Subscriber<UserInfoEntity> subscriber = new Subscriber<UserInfoEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
                mLoginView.onLoginFailed();
            }

            @Override
            public void onNext(UserInfoEntity userInfoEntity) {
                String status = userInfoEntity.getStatus();
                if (status.equals("OK")) {
                    saveUserInfo(userInfoEntity);
                    mLoginView.onLoginSuccess();
                } else {
                    mLoginView.toast(userInfoEntity.getError());
                    mLoginView.onLoginFailed();
                }
            }
        };

        ApiClient.getInstance().login(subscriber, userInfo);
    }

    private void saveUserInfo(UserInfoEntity userInfoEntity) {
        Log.d(TAG, "userId: " + userInfoEntity.getId());
        SharedPreferences.Editor editor = mLoginView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE).edit();
        editor.putInt("user_id", userInfoEntity.getId());
        editor.putInt("age", userInfoEntity.getAge());
        editor.putBoolean("gender", userInfoEntity.getGender());
        editor.putString("phone_number", userInfoEntity.getPhone_number());
        editor.apply();
    }

    private void savePassWord(String password) {
        SharedPreferences.Editor editor = mLoginView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE).edit();
        editor.putString("password", password);
        editor.apply();
    }

}
