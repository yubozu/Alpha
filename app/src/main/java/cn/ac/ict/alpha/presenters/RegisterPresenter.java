package cn.ac.ict.alpha.presenters;

import android.util.Log;

import cn.ac.ict.alpha.Entities.AuthEntity;
import cn.ac.ict.alpha.Entities.UserInfoEntity;
import cn.ac.ict.alpha.Utils.StringUtils;
import cn.ac.ict.alpha.activities.RegisterActivity;
import cn.ac.ict.alpha.models.ApiClient;
import rx.Subscriber;

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

        mRegisterView.onStartRegister();
        startRegister(new UserInfoEntity(phoneNumber, password));
    }

    private void startRegister(UserInfoEntity userInfo) {
        Subscriber<AuthEntity> subscriber = new Subscriber<AuthEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.toString());
                mRegisterView.onRegisterFailed();
            }

            @Override
            public void onNext(AuthEntity authEntity) {
                String status = authEntity.getStatus();
                if (status.equals("OK")) {
                    mRegisterView.onRegisterSuccess();
                } else {
                    mRegisterView.toast(authEntity.getError());
                    mRegisterView.onRegisterFailed();
                }
            }
        };

        ApiClient.getInstance().register(subscriber, userInfo);

    }

}
