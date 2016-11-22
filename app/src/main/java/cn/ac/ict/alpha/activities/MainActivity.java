package cn.ac.ict.alpha.activities;

import android.view.View;
import android.widget.Button;

import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.presenters.MainPresenter;

public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    @BindView(R.id.bt_start_evaluate)
    Button btStartEvaluate;
    @BindView(R.id.bt_logout)
    Button btLogout;


    private MainPresenter mMainPresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();
        mMainPresenter = new MainPresenter(this);
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, "58345f2cb27b0a0a83000dd8", "airdoc");
        MobclickAgent. startWithConfigure(config);
        MobclickAgent.onProfileSignIn(String.valueOf(mMainPresenter.getUserId()));
    }

    @OnClick({R.id.bt_start_evaluate, R.id.bt_edit_user_info, R.id.bt_edit_user_password, R.id.bt_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start_evaluate:
                mMainPresenter.startExam();
                break;
            case R.id.bt_edit_user_info:
                startActivity(UserInfoActivity.class, true);
                break;
            case R.id.bt_edit_user_password:
                startActivity(PasswordActivity.class, true);
                break;
            case R.id.bt_logout:
                mMainPresenter.logout();
                startActivity(LoginActivity.class, true);
                break;
        }
    }

    public void onStartExam(Boolean isFailed){
        if (isFailed) {
            toast(getString(R.string.must_save_info));
        } else {
            startActivity(MedicineActivity.class, false);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
