package cn.ac.ict.alpha.presenters;

import cn.ac.ict.alpha.Utils.StringUtils;
import cn.ac.ict.alpha.activities.LoginActivity;

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
        mLoginView.onLoginSuccess();
    }

}
