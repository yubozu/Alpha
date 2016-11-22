package cn.ac.ict.alpha.activities.sound;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.FileUtils;
import cn.ac.ict.alpha.activities.MainActivity;
import cn.ac.ict.alpha.activities.face.FaceMainActivity;
import cn.ac.ict.alpha.activities.tapper.TapperMainActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class SoundMainActivity extends FragmentActivity {

    private MediaRecorder recorder;
    private String path;
    SweetAlertDialog sweetAlertDialog = null;
    public boolean isTesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_main);

        init();
    }

    private void init() {
        isTesting = false;
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new MainFragment()).commit();
        path = getFilesDir().getAbsolutePath() + "/" + "temp.3gp";
    }

    public void prepareRecorder() {

        if (recorder == null) {
            try {
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setOutputFile(path);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.prepare();
                recorder.start();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "错误", Toast.LENGTH_SHORT).show();
                recorder = null;
                init();
            }
        }
    }

    public void finishTesting() {
        releaseRecorder();
        SaveToStorage();
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("测试完成！")
                .setContentText("点击确定进行下一项测试")
                .setConfirmText("确定")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        next();
                    }
                });
        sweetAlertDialog.show();
    }

    public void releaseRecorder() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    @Override
    protected void onPause() {
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }
        releaseRecorder();
        super.onPause();
    }

    private void SaveToStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);

        String filePath = FileUtils.getFilePath(this, "sound");

        File file = new File(path);
        Boolean result = file.renameTo(new File(filePath));
        Log.d("SaveToStorage", "save file result: " + result);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("soundFilePath", filePath);
        editor.putString("soundScore", String.format("%1.1f", getScore()));
        editor.apply();
    }

    private float getScore() {
        return 0.0f;
    }

    public void prev() {
        startActivity(new Intent(SoundMainActivity.this, FaceMainActivity.class));
        finish();
    }

    public void next() {
        //TODO
        startActivity(new Intent(SoundMainActivity.this, TapperMainActivity.class));
        finish();
//        Toast.makeText(this, "next", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        releaseRecorder();
        if (path != null) {
            File file = new File(path);
            if (file.exists())
                file.delete();
        }
        if (!isTesting) {
            sweetAlertDialog = new SweetAlertDialog(SoundMainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("退出")
                    .setContentText("本次评估尚未完成，是否退出？")
                    .setConfirmText("退出")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            startActivity(new Intent(SoundMainActivity.this, MainActivity.class));
                            finish();
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
        } else {
            init();
        }
    }
}
