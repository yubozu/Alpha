package cn.ac.ict.alpha.activities.count;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.View;

import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.activities.BaseActivity;
import cn.ac.ict.alpha.activities.MainActivity;
import cn.ac.ict.alpha.activities.stand.StandMainActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CountMainActivity extends BaseActivity {
    //TODO:确定是否要做成多轮的，是否要做成声音版的
    private MediaPlayer mp;
    //    private Intent intent;
    SweetAlertDialog sweetAlertDialog = null;

    @Override
    protected void init() {
        super.init();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.memory);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp = null;
            }
        });
        mp.start();
    }

    @OnClick({R.id.bt_count_start, R.id.bt_count_skip, R.id.tv_back_to_medicine})
    public void onClick(View view) {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;

        }
        switch (view.getId()) {
            case R.id.bt_count_start:

                SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);
                double memoryScore = Double.parseDouble(sharedPreferences.getString("memoryScore", "-1"));
                if (memoryScore >= 0) {
                    sweetAlertDialog = new SweetAlertDialog(CountMainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定吗？")
                            .setContentText("本次评估已经进行此测试，是否重新测试？")
                            .setConfirmText("是的，重新测试")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    startActivity(CountGameActivity.class, false);
                                }
                            })
                            .setCancelText("不，进行下一项")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(StandMainActivity.class);
                                }
                            });
                    sweetAlertDialog.show();
                } else {

                    startActivity(CountGameActivity.class, false);
                }
                break;
            case R.id.bt_count_skip:
                sweetAlertDialog = new SweetAlertDialog(CountMainActivity.this, SweetAlertDialog.NORMAL_TYPE)
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
                                startActivity(StandMainActivity.class);
                            }
                        });
                sweetAlertDialog.show();
                break;
            case R.id.tv_back_to_medicine:
                finish();
                break;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_count_main;
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
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CountMainActivity.this, SweetAlertDialog.WARNING_TYPE)
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

