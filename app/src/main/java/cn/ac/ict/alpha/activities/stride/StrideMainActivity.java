package cn.ac.ict.alpha.activities.stride;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.View;

import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.activities.BaseActivity;
import cn.ac.ict.alpha.activities.MainActivity;
import cn.ac.ict.alpha.activities.ResultActivity;
import cn.ac.ict.alpha.activities.tapper.TapperMainActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class StrideMainActivity extends BaseActivity {
    MediaPlayer mp;
    SweetAlertDialog sweetAlertDialog = null;
    @Override
    public int getLayoutRes() {
        return R.layout.activity_stride_main;
    }

    @Override
    protected void init() {
        super.init();
        startAudioWidget();
    }

    private void startAudioWidget() {
        mp = MediaPlayer.create(StrideMainActivity.this, R.raw.stride);
        mp.start();
    }

    @OnClick({R.id.tv_prev,R.id.bt_skip,R.id.bt_start})
    public void onClick(View view)
    {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        switch (view.getId())
        {
            case R.id.tv_prev:
                startActivity(TapperMainActivity.class);
                break;
            case R.id.bt_start:
                final SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);
                double score = Double.parseDouble(sharedPreferences.getString("strideScore", "-1"));
                if (score >= 0) {
                    sweetAlertDialog = new SweetAlertDialog(StrideMainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定吗？")
                            .setContentText("本次评估已经进行此测试，是否重新测试？")
                            .setConfirmText("是的，重新测试")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    startActivity(StrideWalkingActivity.class);
                                }
                            })
                            .setCancelText("完成评估")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    //TODO: complete the eva
                                    startActivity(new Intent(StrideMainActivity.this, ResultActivity.class));
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putLong("endTime",System.currentTimeMillis());
                                    editor.apply();
                                    finish();
                                }
                            });
                    sweetAlertDialog.show();
                } else {
                    startActivity(StrideWalkingActivity.class);
                }
                break;
            case R.id.bt_skip:

                sweetAlertDialog = new SweetAlertDialog(StrideMainActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(getString(R.string.confirm_skip))
                        .setContentText(getString(R.string.skip_dialog_content))
                        .setConfirmText(getString(R.string.skip_negative))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText("跳过，完成评估")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);
                                startActivity(new Intent(StrideMainActivity.this, ResultActivity.class));
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putLong("endTime",System.currentTimeMillis());
                                editor.apply();
                                finish();
                            }
                        });
                sweetAlertDialog.show();
                sweetAlertDialog.setCancelable(false);
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
        if(sweetAlertDialog!=null&&sweetAlertDialog.isShowing())
        {
            sweetAlertDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        sweetAlertDialog = new SweetAlertDialog(StrideMainActivity.this, SweetAlertDialog.WARNING_TYPE)
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
