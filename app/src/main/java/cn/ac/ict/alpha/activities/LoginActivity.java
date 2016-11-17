package cn.ac.ict.alpha.activities;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.presenters.LoginPresenter;

public class LoginActivity extends BaseActivity {

    public static final String TAG = "LoginActivity";
    @BindView(R.id.phone_number)
    EditText mPhoneNumber;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.new_user)
    TextView mNewUser;

    private LoginPresenter mLoginPresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        super.init();
        mLoginPresenter = new LoginPresenter(this);
    }

    @OnClick({R.id.login, R.id.new_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                startLogin();
                break;
            case R.id.new_user:
//                跳转到注册界面
                startActivity(RegisterActivity.class);
                break;
        }
    }

    private void startLogin() {
        login();
    }

    private void login() {
        hideKeyBoard();
        String phoneNumber = mPhoneNumber.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        mLoginPresenter.login(phoneNumber, password);
    }

    public void onPhoneNumberError() {
        mPhoneNumber.setError(getString(R.string.phone_number_error));
    }

    public void onPasswordError() {
        mPassword.setError(getString(R.string.user_password_error));
    }

    public void onStartLogin() {
        showProgress(getString(R.string.logining));
    }

    public void onLoginSuccess() {
        hideProgress();
        toast(getString(R.string.login_success));
//        跳转到程序主界面
//        startActivity(MainActivity.class);
    }

    public void onLoginFailed() {
        hideProgress();
        toast(getString(R.string.login_failed));
    }

}
