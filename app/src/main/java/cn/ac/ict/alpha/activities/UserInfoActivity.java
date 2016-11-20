package cn.ac.ict.alpha.activities;

import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.presenters.UserInfoPresenter;

/**
 * Author: saukymo
 * Date: 11/19/16
 */

public class UserInfoActivity extends BaseActivity{
    public static final String TAG = "UserInfoActivity";
    @BindView(R.id.phone_number)
    EditText mPhoneNumber;
    @BindView(R.id.gender)
    ToggleButton mGender;
    @BindView(R.id.age)
    EditText mAge;

    private UserInfoPresenter mUserInfoPresenter;
    @Override
    public int getLayoutRes() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init() {
        super.init();
        mUserInfoPresenter = new UserInfoPresenter(this);
        mUserInfoPresenter.loadUserInfo();
    }

    @OnClick({R.id.save, R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                saveUserInfo();
                break;
            case R.id.cancel:
//                跳转到注册界面
                startActivity(MainActivity.class);
                break;
        }
    }

    public void setUserInfo(String phoneNumber, Integer age, Boolean gender) {
        if (age > 0) {
            mAge.setText(String.valueOf(age));
        }
        mPhoneNumber.setText(phoneNumber);
        mGender.setChecked(gender);
    }

    private void saveUserInfo() {
        String phoneNumber = mPhoneNumber.getText().toString().trim();
        Boolean gender = mGender.isChecked();
        Integer age = Integer.parseInt(mAge.getText().toString().trim());
        mUserInfoPresenter.saveUserInfo(phoneNumber, gender, age);
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
