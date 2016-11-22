package cn.ac.ict.alpha.activities.tapper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatToggleButton;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.activities.BaseActivity;
import cn.ac.ict.alpha.activities.MainActivity;
import cn.ac.ict.alpha.activities.sound.SoundMainActivity;
import cn.ac.ict.alpha.activities.stand.StandTestingActivity;
import cn.ac.ict.alpha.activities.stride.StrideMainActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class TapperMainActivity extends BaseActivity {
    private boolean isRight = true;
    @BindView(R.id.toggle_hand)
    FlatToggleButton toggleButton;
    int right = 0;
    int left = 0;
    SweetAlertDialog sweetAlertDialog = null;
    MediaPlayer mp;
    @Override
    protected void init() {
        super.init();
        startAudioGuide();
    }
    private void startAudioGuide() {
        mp = MediaPlayer.create(getApplicationContext(), R.raw.tapping);
        mp.start();
    }
    @Override
    public int getLayoutRes() {
        return R.layout.activity_tapper_main;
    }

    @OnClick({R.id.tv_prev,R.id.bt_skip,R.id.bt_start,R.id.btn_left, R.id.btn_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_prev:
                startActivity(SoundMainActivity.class);
            case R.id.btn_left:
                left++;
                break;
            case R.id.btn_right:
                right++;
                break;
            case R.id.bt_start:
                isRight = toggleButton.isChecked();
                if (mp != null) {
                    mp.stop();
                    mp.release();
                    mp = null;

                }
                SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);
                double score = Double.parseDouble(sharedPreferences.getString("tappingScore", "-1"));
                if (score >= 0) {
                    sweetAlertDialog = new SweetAlertDialog(TapperMainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定吗？")
                            .setContentText("本次评估已经进行此测试，是否重新测试？")
                            .setConfirmText("是的，重新测试")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    startActivity(TapperTestingActivity.class, false);
                                }
                            })
                            .setCancelText("不，进行下一项")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                   startActivity(StrideMainActivity.class);
                                }
                            });
                    sweetAlertDialog.show();
                } else {
                    isRight = toggleButton.isChecked();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("tappingRight", isRight);
                    editor.apply();
                    startActivity(StandTestingActivity.class, false);
                }

                Intent intent = new Intent(TapperMainActivity.this, TapperTestingActivity.class);
                intent.putExtra("isRight", isRight);
                startActivity(intent);
                break;
            case R.id.bt_skip:
                sweetAlertDialog = new SweetAlertDialog(TapperMainActivity.this, SweetAlertDialog.NORMAL_TYPE)
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
                                startActivity(StrideMainActivity.class);
                            }
                        });
                sweetAlertDialog.show();
                break;
        }
        if (left > 3 || right > 3) {
            Toast.makeText(this, getResources().getText(R.string.tapper_main_tip), Toast.LENGTH_SHORT).show();
            left = 0;
            right = 0;
        }

    }



    @Override
    protected void onPause() {
        if(mp!=null)
        {
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
        sweetAlertDialog = new SweetAlertDialog(TapperMainActivity.this, SweetAlertDialog.WARNING_TYPE)
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
