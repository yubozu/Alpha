package cn.ac.ict.alpha.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.presenters.MainPresenter;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";
    @BindView(R.id.spinner_gender)
    MaterialSpinner spinnerGender;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.bt_start_evaluate)
    Button btStartEvaluate;
    @BindView(R.id.bt_logout)
    Button btLogout;
    @BindView(R.id.bt_info)
    Button btInfo;

    private MainPresenter mMainPresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();
        mMainPresenter = new MainPresenter(this);
        HashMap<String, Object> userInfo = mMainPresenter.getUserInfo();
        spinnerGender.setItems(getResources().getStringArray(R.array.gender));
        initInfo(userInfo);
    }

    private void initInfo(HashMap<String, Object> userInfo) {
        if (userInfo == null || userInfo.size() <= 0) {
            setInfoEditable(true);
            return;
        }
        setInfoEditable(false);
        spinnerGender.setSelectedIndex((int) userInfo.get("gender"));
        tvBirthday.setText((String)userInfo.get("birthday"));
    }

    private void setInfoEditable(boolean flag) {
        spinnerGender.setClickable(flag);
        tvBirthday.setClickable(flag);
        if (flag) {
            btInfo.setText(getString(R.string.save_info));
            spinnerGender.setBackgroundColor(Color.WHITE);
            tvBirthday.setBackgroundColor(Color.WHITE);
            btInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress("saving");
                    int gender = spinnerGender.getSelectedIndex();
                    String birthday = tvBirthday.getText().toString();
                    if(spinnerGender.getSelectedIndex()<=0)
                    {
                        toast(getString(R.string.must_input_gender));
                        return;
                    }
                    if(tvBirthday.getText().toString().isEmpty())
                    {
                        toast(getString(R.string.must_input_birthday));
                        return;
                    }
                    mMainPresenter.saveInfo(gender,birthday);
                }
            });
        } else {
            spinnerGender.setBackgroundColor(Color.GRAY);
            tvBirthday.setBackgroundColor(Color.GRAY);
            btInfo.setText(getString(R.string.change_info));
            btInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setInfoEditable(true);
                }
            });
        }
    }
    public void onSaveOK()
    {
        hideProgress();
        toast("save");
        setInfoEditable(false);
    }
    @OnClick({R.id.tv_birthday, R.id.bt_start_evaluate, R.id.bt_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_birthday:
                pickDate();
                break;
            case R.id.bt_start_evaluate:
                // TODO: 开始评估
                if(spinnerGender.isClickable())
                {
                    toast(getString(R.string.must_save_info));
                    return;
                }
                startActivity(MedicineActivity.class);
                break;
            case R.id.bt_logout:
                // TODO: 用户登出
                break;
        }
    }
    private void pickDate()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final String originalDate = tvBirthday.getText().toString();
        View v = View.inflate(MainActivity.this,R.layout.layout_datepicker_dialog,null);
        DatePicker picker = (DatePicker) v.findViewById(R.id.datepicker);
        picker.setDate(2015, 7);
        picker.setMode(DPMode.SINGLE);
        picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                tvBirthday.setText(date);
            }
        });
        builder.setPositiveButton(getString(R.string.confirm),null);
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvBirthday.setText(originalDate);
            }
        });

        builder.setView(v);
        builder.show();
    }

//    private void login() {
//        hideKeyBoard();
//        String userName = mUserName.getText().toString().trim();
//        String password = mPassword.getText().toString().trim();
//        mLoginPresenter.login(userName, password);
//    }

//    public void onUserNameError() {
//        mUserName.setError(getString(R.string.user_name_error));
//    }
//
//    public void onPasswordError() {
//        mPassword.setError(getString(R.string.user_password_error));
//    }
//
//    public void onStartLogin() {
//        showProgress(getString(R.string.logining));
//    }
//
//    public void onLoginSuccess() {
//        hideProgress();
//        toast(getString(R.string.login_success));
////        跳转到程序主界面
////        startActivity(MainActivity.class);
//    }
//
//    public void onLoginFailed() {
//        hideProgress();
//        toast(getString(R.string.login_failed));
//    }
//
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
