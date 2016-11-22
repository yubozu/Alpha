package cn.ac.ict.alpha.activities.face;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.View;

import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.activities.BaseActivity;
import cn.ac.ict.alpha.activities.MainActivity;
import cn.ac.ict.alpha.activities.sound.SoundMainActivity;
import cn.ac.ict.alpha.activities.stand.StandMainActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class FaceMainActivity extends BaseActivity {
    private MediaPlayer mp;
    SweetAlertDialog sweetAlertDialog = null;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_face_main;
    }

    @Override
    protected void init() {
        super.init();
        initWidget();
    }

    private void initWidget() {
        mp = MediaPlayer.create(getApplicationContext(), R.raw.face_guide);
        mp.start();

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

    @OnClick({R.id.tv_prev, R.id.bt_start, R.id.bt_skip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_prev:
                startActivity(StandMainActivity.class);
                break;
            case R.id.bt_start:

                SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);
                double score = Double.parseDouble(sharedPreferences.getString("faceScore", "-1"));
                if (score >= 0) {
                    sweetAlertDialog = new SweetAlertDialog(FaceMainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定吗？")
                            .setContentText("本次评估已经进行此测试，是否重新测试？")
                            .setConfirmText("是的，重新测试")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    startActivity(VideoCaptureActivity.class, false);
                                }
                            })
                            .setCancelText("不，进行下一项")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    //TODO next test
                                    startActivity(SoundMainActivity.class);
                                }
                            });
                    sweetAlertDialog.show();
                } else {
                    startActivity(VideoCaptureActivity.class, false);
                }

                break;
            case R.id.bt_skip:
                sweetAlertDialog = new SweetAlertDialog(FaceMainActivity.this, SweetAlertDialog.NORMAL_TYPE)
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
                                //TODO next test
                                startActivity(SoundMainActivity.class);
                            }
                        });
                sweetAlertDialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        sweetAlertDialog = new SweetAlertDialog(FaceMainActivity.this, SweetAlertDialog.WARNING_TYPE)
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
