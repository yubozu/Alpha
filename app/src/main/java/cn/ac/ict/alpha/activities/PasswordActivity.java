package cn.ac.ict.alpha.activities;

import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.presenters.PasswordPresenter;

/**
 * Author: saukymo
 * Date: 11/20/16
 */

public class PasswordActivity extends BaseActivity{
    public static final String TAG = "PasswordActivity";
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.password_confirm)
    EditText mPasswordConfirm;

    private PasswordPresenter mPasswordPresenter;
    @Override
    public int getLayoutRes() {
        return R.layout.activity_password;
    }

    @Override
    protected void init() {
        super.init();
        mPasswordPresenter = new PasswordPresenter(this);
    }

    @OnClick({R.id.bt_confirm, R.id.bt_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                editPassword();
                break;
            case R.id.bt_cancel:
                startActivity(MainActivity.class);
                break;
        }
    }

    private void editPassword() {
        String password = mPassword.getText().toString().trim();
        String passwordConfirm = mPasswordConfirm.getText().toString().trim();
        mPasswordPresenter.editPassword(password, passwordConfirm);
    }

    public void onPasswordError() {
        mPassword.setError(getString(R.string.user_password_error));
    }

    public void onPasswordConfirmError() {
        mPasswordConfirm.setError(getString(R.string.user_password_confirm_error));
    }

    public void onStartSave() {
        showProgress(getString(R.string.saving));
    }

    public void onSaveSuccess() {
        hideProgress();
        toast(getString(R.string.saving_success));
//        跳转到程序主界面
        startActivity(MainActivity.class);
    }

    public void onSaveFailed() {
        hideProgress();
        toast(getString(R.string.saving_failed));
    }
}
