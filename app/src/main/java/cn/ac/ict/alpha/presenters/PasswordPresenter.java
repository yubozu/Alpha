package cn.ac.ict.alpha.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import cn.ac.ict.alpha.Entities.UserInfoEntity;
import cn.ac.ict.alpha.Utils.StringUtils;
import cn.ac.ict.alpha.activities.PasswordActivity;
import cn.ac.ict.alpha.models.ApiClient;
import rx.Subscriber;

/**
 * Author: saukymo
 * Date: 11/20/16
 */

public class PasswordPresenter {
    public static final String TAG = "PasswordPresenter";

    private PasswordActivity mPasswordView;

    public PasswordPresenter(PasswordActivity passwordView) {
        mPasswordView = passwordView;
    }

    public void editPassword(String password, String passwordConfirm) {
        if (!StringUtils.checkPassword(password)) {
            mPasswordView.onPasswordError();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            mPasswordView.onPasswordConfirmError();
            return;
        }

        mPasswordView.onStartSave();
        startEditPassword(new UserInfoEntity(password));
    }

    private void startEditPassword(UserInfoEntity userInfoEntity) {
        Subscriber<UserInfoEntity> subscriber = new Subscriber<UserInfoEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
                mPasswordView.onSaveFailed();
            }

            @Override
            public void onNext(UserInfoEntity userInfoEntity) {
                String status = userInfoEntity.getStatus();
                if (status.equals("OK")) {
//                    saveUserInfo(userInfoEntity);
                    mPasswordView.onSaveSuccess();
                } else {
                    mPasswordView.toast(userInfoEntity.getError());
                    mPasswordView.onSaveFailed();
                }
            }
        };

        ApiClient.getInstance().updateUserInfo(subscriber, getUserId(), userInfoEntity);
    }

    private Integer getUserId(){
        SharedPreferences sharedPreferences = mPasswordView.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}
