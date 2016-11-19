package cn.ac.ict.alpha.presenters;

import android.util.Log;

import cn.ac.ict.alpha.Entities.ResultEntity;
import cn.ac.ict.alpha.Entities.UserInfo;
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

    public void login(String phoneNumber, String password) {
        if (StringUtils.checkPhoneNumber(phoneNumber)) {
            if (StringUtils.checkPassword(password)) {
                mLoginView.onStartLogin();
                startLogin(new UserInfo(phoneNumber, password));
            } else {
                mLoginView.onPasswordError();
            }
        } else {
            mLoginView.onPhoneNumberError();
        }
    }

    private void startLogin(UserInfo userInfo) {
        Subscriber<ResultEntity> subscriber = new Subscriber<ResultEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
                mLoginView.onLoginFailed();
            }

            @Override
            public void onNext(ResultEntity resultEntity) {
                String status = resultEntity.getStatus();
                if (status.equals("OK")) {
                    mLoginView.onLoginSuccess();
                } else {
                    mLoginView.toast(resultEntity.getError());
                    mLoginView.onLoginFailed();
                }
            }
        };

        ApiClient.getInstance().login(subscriber, userInfo);
    }

}
