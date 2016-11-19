package cn.ac.ict.alpha.activities;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.presenters.RegisterPresenter;

/**
 * Author: saukymo
 * Date: 11/15/16
 */

public class RegisterActivity extends BaseActivity {
    public static final String TAG = "RegisterPresenter";
    @BindView(R.id.phone_number)
    EditText mPhoneNumber;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.password_confirm)
    EditText mPasswordConfirm;
    @BindView(R.id.register)
    Button mRegister;
    @BindView(R.id.login)
    TextView mLogin;

    private RegisterPresenter mRegisterPresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        super.init();
        mRegisterPresenter = new RegisterPresenter(this);
    }

    @OnClick({R.id.register, R.id.login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startRegister();
                break;
            case R.id.login:
//                跳转到登录界面
                startActivity(LoginActivity.class);
                break;
        }
    }

    private void startRegister() {
        register();
    }

    private void register() {
        hideKeyBoard();
        String phoneNumber = mPhoneNumber.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String passwordConfirm = mPasswordConfirm.getText().toString().trim();
        mRegisterPresenter.register(phoneNumber, password, passwordConfirm);
    }

    public void onPhoneNumberError() {
        mPhoneNumber.setError(getString(R.string.phone_number_error));
    }

    public void onPasswordError() {
        mPassword.setError(getString(R.string.user_password_error));
    }

    public void onPasswordConfirmError() {
        mPasswordConfirm.setError(getString(R.string.user_password_confirm_error));
    }

    public void onStartRegister() {
        showProgress(getString(R.string.registering));
    }

    public void onRegisterSuccess() {
        hideProgress();
        toast(getString(R.string.register_success));
//        跳转到程序主界面
        startActivity(LoginActivity.class);
    }

    public void onRegisterFailed() {
        hideProgress();
        toast(getString(R.string.login_failed));
    }
}
