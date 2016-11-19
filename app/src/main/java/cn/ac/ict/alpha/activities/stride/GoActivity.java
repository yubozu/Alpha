package cn.ac.ict.alpha.activities.stride;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.FileUtils;
import cn.ac.ict.alpha.Utils.FloatVector;
import cn.ac.ict.alpha.activities.ResultActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class GoActivity extends Activity {
    TextView goContent;
    Button btnGo;
    Vibrator vibrator;
    long[] pattern = {100, 400};
    boolean flag = false;
    boolean start = false;
    MediaPlayer mp;
    SensorManager sm;
    AccEventListener accEventListener;
    GyroEventListener gyroEventListener;
    ArrayList<FloatVector> accFloatVectors;
    ArrayList<FloatVector> gyroFloatVectors;

    SweetAlertDialog sweetAlertDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stride_go);
        goContent = (TextView) findViewById(R.id.tv_go);
        btnGo = (Button) findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new onBtnClickListener());
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.countdown);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                flag = true;
                btnGo.setText(getString(R.string.stride_btn_text));
                btnGo.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.freebie_2));
                btnGo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.freebie_1));
                btnGo.setVisibility(View.VISIBLE);
                vibrator.vibrate(pattern, -1);
                goContent.setText(getString(R.string.stride_testing));
                start = true;

            }
        });
        sm = (SensorManager) GoActivity.this.getSystemService(Context.SENSOR_SERVICE);

        accEventListener = new AccEventListener();
        gyroEventListener = new GyroEventListener();
        flag = false;
        prepare();
    }

    private void register() {
        sm.registerListener(accEventListener,
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        sm.registerListener(gyroEventListener,
                sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);
    }

    private void stop() {
        start = false;
        sm.unregisterListener(accEventListener);
        sm.unregisterListener(gyroEventListener);
    }

    class AccEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (start) {
                FloatVector vector = new FloatVector(event.values[0], event.values[1], event.values[2]);
                accFloatVectors.add(vector);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    class GyroEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (start) {
                FloatVector gyroFloatVector = new FloatVector(event.values[0], event.values[1], event.values[2]);
                gyroFloatVectors.add(gyroFloatVector);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    class onBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            stop();
            saveToStorage(accFloatVectors,gyroFloatVectors);
            //TODO: complete the eva
            sweetAlertDialog = new SweetAlertDialog(GoActivity.this,SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitleText("测试完成！")
                    .setContentText("点击确定进行下一项测试")
                    .setConfirmText("确定")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            startActivity(new Intent(GoActivity.this, ResultActivity.class));
                            SharedPreferences.Editor editor = getSharedPreferences("Alpha",MODE_PRIVATE).edit();
                            editor.putLong("endTime",System.currentTimeMillis());
                            editor.apply();
                            finish();
                        }
                    });
            sweetAlertDialog.show();
        }
    }

    private void prepare() {

        mp.start();
        goContent.setText(getString(R.string.stride_next_turn));

        btnGo.setVisibility(View.GONE);

        accFloatVectors = new ArrayList<>();
        gyroFloatVectors = new ArrayList<>();
        register();
    }

    @Override
    protected void onPause() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;

        }
        stop();
        finish();
        super.onPause();
    }

    public void saveToStorage(ArrayList<FloatVector> accList,ArrayList<FloatVector> gyroList) {
        SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);

        String filePath = FileUtils.getFilePath(this, "stride");
        // Example: How to write data to file.
        File file = new File(filePath);
        try {
            FileWriter fileWrite = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);
            bufferedWriter.write("Stride Tug \n");
            bufferedWriter.write("ACC \n");
            for (FloatVector acc : accList) {
                bufferedWriter.write(acc.timeStamp + ", " + acc.x + ", " + acc.y + ", " + acc.z + "\n");
                Log.d("GoActivity", String.valueOf(acc.timeStamp));
            }
            bufferedWriter.write("GYRO \n");
            for (FloatVector gyro : gyroList) {
                bufferedWriter.write(gyro.timeStamp + ", " + gyro.x + ", " + gyro.y + ", " + gyro.z + "\n");
            }
            //Important! Have a new line in the end of txt file.
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWrite.close();
        } catch (IOException e) {
            Log.e("ExamAdapter", e.toString());
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("strideFilePath", filePath);
        editor.putString("strideScore",String.format("%1.1f",getScore()));
        editor.apply();
    }

    private float getScore() {
        //TODO:
        return 0.0f;
    }
}
