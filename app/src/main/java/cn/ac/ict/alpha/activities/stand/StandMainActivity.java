package cn.ac.ict.alpha.activities.stand;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ToggleButton;

import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.activities.BaseActivity;
import cn.ac.ict.alpha.activities.MainActivity;
import cn.ac.ict.alpha.activities.count.CountMainActivity;
import cn.ac.ict.alpha.activities.face.FaceMainActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class StandMainActivity extends BaseActivity {
    //TODO： 左右腿轮着来
    private ToggleButton toggleFoot;
    private boolean isRight = true;
    SweetAlertDialog sweetAlertDialog = null;
    MediaPlayer mp;

    @Override
    protected void init() {
        super.init();
        initWidget();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_stand_main;
    }

    private void initWidget() {
        toggleFoot = (ToggleButton) findViewById(R.id.toggle_foot);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.stand);
        mp.start();
    }

    @OnClick({R.id.tv_prev, R.id.bt_start, R.id.bt_skip})
    public void onClick(View v) {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;

        }
        switch (v.getId()) {
            case R.id.tv_prev:
                startActivity(CountMainActivity.class);
                break;
            case R.id.bt_start:
                SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);
                double score = Double.parseDouble(sharedPreferences.getString("standScore", "-1"));
                if (score >= 0) {
                    sweetAlertDialog = new SweetAlertDialog(StandMainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定吗？")
                            .setContentText("本次评估已经进行此测试，是否重新测试？")
                            .setConfirmText("是的，重新测试")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    startActivity(StandTestingActivity.class, false);
                                }
                            })
                            .setCancelText("不，进行下一项")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(FaceMainActivity.class);
                                }
                            });
                    sweetAlertDialog.show();
                } else {
                    isRight = toggleFoot.isChecked();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("standRight", isRight);
                    editor.apply();
                    startActivity(StandTestingActivity.class, false);
                }
                break;
            case R.id.bt_skip:
                sweetAlertDialog = new SweetAlertDialog(StandMainActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(getString(R.string.confirm_skip))
                        .setContentText(getString(R.string.skip_dialog_content))
                        .setConfirmText(getString(R.string.skip_negative))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText(getString(R.string.skip_positive))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                startActivity(FaceMainActivity.class);
                            }
                        });
                sweetAlertDialog.show();
                break;
        }
    }

    @Override
    protected void onPause() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;

        }
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        sweetAlertDialog = new SweetAlertDialog(StandMainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("退出")
                .setContentText("本次评估尚未完成，是否退出？")
                .setConfirmText("退出")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        startActivity(MainActivity.class);
                    }
                })
                .setCancelText("取消")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
        sweetAlertDialog.show();
    }
}
