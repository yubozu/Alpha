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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bcgdv.asia.lib.ticktock.TickTockView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.FileUtils;
import cn.ac.ict.alpha.Utils.FloatVector;
import cn.ac.ict.alpha.activities.ResultActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class StrideWalkingActivity extends Activity {
    private TickTockView ttv;
    Vibrator vibrator;
    MediaPlayer mp,mpGuide;
    long[] pattern = {100, 400};
    SensorManager sm;
    AccEventListener accEventListener;
    GyroEventListener gyroEventListener;
    ArrayList<FloatVector> accVectors;
    ArrayList<FloatVector> gyroVectors;
    boolean start = false;
    Button goBtn = null;
    TextView tv = null;
    SweetAlertDialog sweetAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stride_walking);
        init();
    }

    private void init() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        tv = (TextView)findViewById(R.id.tv_stride_walking);
        accVectors = new ArrayList<>();
        gyroVectors = new ArrayList<>();
        mpGuide = MediaPlayer.create(getApplicationContext(), R.raw.stride_walking_guide);
        mpGuide.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mpGuide = null;
            }
        });
        mp = MediaPlayer.create(getApplicationContext(), R.raw.countdown);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                vibrator.vibrate(pattern, -1);
                initSensors();
                initTickTockView();
                start=true;
                tv.setText(getString(R.string.stride_testing));
                goBtn.setVisibility(View.VISIBLE);
                goBtn.setText(getString(R.string.stride_walking_carryon));
            }
        });
        ttv = (TickTockView) findViewById(R.id.ttv_stride_walk);
        goBtn = (Button)findViewById(R.id.stride_walking_btn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!start)
                {
                    if(mpGuide!=null)
                    {
                        mpGuide.stop();
                        mpGuide.release();
                        mpGuide=null;

                    }

                    mp.start();
                    tv.setText(getString(R.string.stride_prepare));
                    goBtn.setVisibility(View.INVISIBLE);

                }
                else
                {
                    ttv.stop();
                    vibrator.vibrate(pattern, -1);
                    stopSensors();
                    executeFinish();
                }
            }
        });

        mpGuide.start();
    }

    private void initTickTockView() {

        Calendar start = Calendar.getInstance();
        start.add(Calendar.SECOND, -1);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.SECOND, 60);
        ttv.setOnTickListener(new TickTockView.OnTickListener() {

            @Override
            public String getText(long timeRemainingInMillis) {
                if (timeRemainingInMillis <= 0) {
                    vibrator.vibrate(pattern, -1);
                    stopSensors();
                    executeFinish();
                }
                return String.valueOf(timeRemainingInMillis / 1000 + 1);
            }
        });
        ttv.start(start, end);
    }

    private void initSensors() {
        accEventListener = new AccEventListener();
        gyroEventListener = new GyroEventListener();
        sm = (SensorManager) StrideWalkingActivity.this.getSystemService(Context.SENSOR_SERVICE);
        Sensor acc =  sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(acc!=null) {
            sm.registerListener(accEventListener,
                    acc,
                    SensorManager.SENSOR_DELAY_GAME);
        }
        Sensor gyro =  sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(gyro!=null) {
            sm.registerListener(gyroEventListener,
                    gyro,
                    SensorManager.SENSOR_DELAY_GAME);
        }

    }

    private void stopSensors() {
        start = false;
        if(sm!=null) {
            sm.unregisterListener(accEventListener);
            sm.unregisterListener(gyroEventListener);
        }
        if(ttv!=null) {
            ttv.stop();
        }

    }

    private void executeFinish() {
        saveToStorage();
        //TODO:
        sweetAlertDialog = new SweetAlertDialog(StrideWalkingActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("测试完成！")
                .setContentText("点击完成测试")
                .setConfirmText("确定")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(StrideWalkingActivity.this, ResultActivity.class));
                        SharedPreferences.Editor editor = getSharedPreferences("Alpha", MODE_PRIVATE).edit();
                        editor.putLong("endTime", System.currentTimeMillis());
                        editor.apply();
                        finish();
                    }
                });
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

    }

    class AccEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (start) {
                FloatVector vector = new FloatVector(event.values[0], event.values[1], event.values[2]);
                accVectors.add(vector);
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
                FloatVector gyroVector = new FloatVector(event.values[0], event.values[1], event.values[2]);
                gyroVectors.add(gyroVector);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    @Override
    protected void onPause() {
        if(mpGuide!=null)
        {
            mpGuide.stop();
            mpGuide.release();
            mpGuide=null;

        }

        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        stopSensors();
        finish();

        super.onPause();
    }


    public void saveToStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);

        String filePath = FileUtils.getFilePath(this, "stride");
        // Example: How to write data to file.
        File file = new File(filePath);
        try {
            FileWriter fileWrite = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);
            bufferedWriter.write("Stride Walking \n");
            bufferedWriter.write("ACC \n");
            for (FloatVector acc : accVectors) {
                bufferedWriter.write(acc.timeStamp + ", " + acc.x + ", " + acc.y + ", " + acc.z + "\n");
                Log.d("GoActivity", String.valueOf(acc.timeStamp));
            }
            bufferedWriter.write("GYRO \n");
            for (FloatVector gyro : gyroVectors) {
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
        editor.putString("strideScore",String.format("%1.1f",getScore(accVectors)));
        editor.apply();
    }

    private float getScore(ArrayList<FloatVector> acc) {
        //TODO:
        int test = Pedometer.getStrideNumbers(Pedometer.genKalman(Pedometer.genOneDimension(acc)));
        return (float)test;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(StrideWalkingActivity.this,StrideMainActivity.class));
        finish();
    }
}
