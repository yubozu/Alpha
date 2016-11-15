package cn.ac.ict.alpha.presenters;

import cn.ac.ict.alpha.Utils.StringUtils;
import cn.ac.ict.alpha.activities.RegisterActivity;

/**
 * Author: saukymo
 * Date: 11/15/16
 */

public class RegisterPresenter {
    public static final String TAG = "RegisterPresenter";

    private RegisterActivity mRegisterView;

    public RegisterPresenter(RegisterActivity registerView) {
        mRegisterView = registerView;
    }

    public void register(String phoneNumber, String password, String passwordConfirm) {
        if (!StringUtils.checkPhoneNumber(phoneNumber)) {
            mRegisterView.onPhoneNumberError();
            return;
        }

        if (!StringUtils.checkPassword(password)) {
            mRegisterView.onPasswordError();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            mRegisterView.onPasswordConfirmError();
            return;
        }

        // 检查电话号码是否已经注册过
//        mRegisterView.onPhoneNumberExister();

        mRegisterView.onStartRegister();
        startRegister(phoneNumber, password);
    }

    private void startRegister(String phoneNumber, String password) {
//        调用model的注册模块新增用户
        mRegisterView.onRegisterSuccess();
    }
}
