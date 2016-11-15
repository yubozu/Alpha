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
    @BindView(R.id.user_name)
    EditText mUserName;
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
//                startActivity(RegisterActivity.class);
                break;
        }
    }


    private void login() {
        hideKeyBoard();
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        mLoginPresenter.login(userName, password);
    }

    public void onUserNameError() {
        mUserName.setError(getString(R.string.user_name_error));
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


    private void startLogin() {
//        如果之后需要请求权限，可以仿照下面的代码写在这里。
//        if (hasWriteExternalStoragePermission()) {
//            login();
//        } else {
//            applyPermission();
//        }
        login();
    }

    /**
     * 申请权限回调
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_WRITE_EXTERNAL_STORAGE:
//                if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
//                    login();
//                } else {
//                    toast(getString(R.string.not_get_permission));
//                }
//                break;
//        }
//    }
}
