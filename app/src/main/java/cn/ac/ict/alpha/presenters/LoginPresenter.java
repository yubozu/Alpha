package cn.ac.ict.alpha.presenters;

import android.util.Log;

import cn.ac.ict.alpha.Entities.StatusEntity;
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
                startLogin(phoneNumber, password);
            } else {
                mLoginView.onPasswordError();
            }
        } else {
            mLoginView.onPhoneNumberError();
        }
    }

    private void startLogin(String phoneNumber, String pwd) {
//        调用model的登录模块检查用户名密码是否正确
        Subscriber subscriber = new Subscriber<StatusEntity>() {
            @Override
            public void onCompleted() {
                mLoginView.onLoginSuccess();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
                mLoginView.onLoginFailed();
            }

            @Override
            public void onNext(StatusEntity statusEntity) {
                mLoginView.toast(statusEntity.getStatus());
            }
        };
        ApiClient.getInstance().getStatus(subscriber);
    }

}
