package cn.ac.ict.alpha.presenters;

import cn.ac.ict.alpha.Utils.StringUtils;
import cn.ac.ict.alpha.activities.LoginActivity;

/**
 * Author: saukymo
 * Date: 11/14/16
 */

public class LoginPresenter {
    public static final String TAG = "LoginPresenterImpl";

    public LoginActivity mLoginView;

    public LoginPresenter(LoginActivity loginView) {
        mLoginView = loginView;
    }

    public void login(String userName, String pwd) {
        if (StringUtils.checkUserName(userName)) {
            if (StringUtils.checkPassword(pwd)) {
                mLoginView.onStartLogin();
                startLogin(userName, pwd);
            } else {
                mLoginView.onPasswordError();
            }
        } else {
            mLoginView.onUserNameError();
        }
    }

    private void startLogin(String userName, String pwd) {
//        调用model的登录模块检查用户名密码是否正确
        mLoginView.onLoginSuccess();
    }

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
