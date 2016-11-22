package cn.ac.ict.alpha.activities;

import android.os.Handler;

import cn.ac.ict.alpha.R;

public class WelcomeActivity extends BaseActivity{
    private final static String TAG = "welcome";

    @Override
    protected void init() {
        super.init();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(PermissionActivity.class);
            }
        },2500);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onBackPressed() {

    }
}
