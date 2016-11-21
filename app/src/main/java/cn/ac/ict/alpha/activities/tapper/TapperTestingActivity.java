package cn.ac.ict.alpha.activities.tapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bcgdv.asia.lib.ticktock.TickTockView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.FileUtils;
import cn.ac.ict.alpha.activities.face.FaceMainActivity;
import cn.ac.ict.alpha.activities.stride.StrideMainActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class TapperTestingActivity extends Activity {

    @BindView(R.id.tv_left_count)
    TextView tvLeft;
    @BindView(R.id.tv_right_count)
    TextView tvRight;
    @BindView(R.id.btn_left)
    ImageButton btnLeft;
    @BindView(R.id.btn_right)
    ImageButton btnRight;
    @BindView(R.id.ttv_tapper)
    TickTockView ttv;

    private int leftCount, rightCount;
    private ArrayList<Long> content;
    private ArrayList<Boolean> indicator;
    SweetAlertDialog sweetAlertDialog = null;
    private boolean isRight = false;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapper_testing);
        ButterKnife.bind(this);
        content = new ArrayList<Long>();
        indicator = new ArrayList<Boolean>();
        isRight = getIntent().getBooleanExtra("isRight",true);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.countdown);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                initTickTockView();
                setEnable(true);
            }
        });
        mp.start();
        setEnable(false);
    }

    private void initTickTockView()
    {
        Calendar start = Calendar.getInstance();
        start.add(Calendar.SECOND,-1);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.SECOND,10);
        ttv.setOnTickListener(new TickTockView.OnTickListener() {
            @Override
            public String getText(long timeRemainingInMillis) {
                if(timeRemainingInMillis<=0)
                {
//                    mp.release();
//                    mp = null;
                    saveToStorage();
                    sweetAlertDialog = new SweetAlertDialog(TapperTestingActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText("测试完成！")
                            .setContentText("点击确定进行下一项测试")
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(TapperTestingActivity.this, FaceMainActivity.class));
                                    finish();
                                }
                            });
                    sweetAlertDialog.show();
                    //TODO: next test
                    startActivity(new Intent(TapperTestingActivity.this, StrideMainActivity.class));
                    finish();
                  //  startActivity(new Intent(TapperTestingActivity.this, ModuleHelper.getActivityAfterExam()));
                }
                return String.valueOf(timeRemainingInMillis/1000+1);
            }
        });
        ttv.start(start,end);
    }
    @OnClick({R.id.btn_left, R.id.btn_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                tvLeft.setText(String.valueOf(++leftCount));
                indicator.add(false);
                content.add(System.currentTimeMillis());
                break;
            case R.id.btn_right:
                tvRight.setText(String.valueOf(++rightCount));
                indicator.add(true);
                content.add(System.currentTimeMillis());
                break;
        }
    }

    private void setEnable(boolean enabled2) {
        btnLeft.setEnabled(enabled2);
        btnRight.setEnabled(enabled2);
    }

    public void saveToStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);

        String filePath = FileUtils.getFilePath(this, "tapping");
        // Example: How to write data to file.
        File file = new File(filePath);
        try {
            FileWriter fileWrite = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);
            bufferedWriter.write(isRight + "\n");
            for(int i= 0;i<content.size();i++)
            {
                String line = "";
                if(indicator.get(i))
                {
                    line +="right:";
                }else{
                    line +="left:";
                }
                line+=content.get(i)+"\n";
                bufferedWriter.write(line);
            }
            //Important! Have a new line in the end of txt file.
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWrite.close();
        } catch (IOException e) {
            Log.e("ExamAdapter", e.toString());
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tappingFilePath", filePath);
        editor.putString("tappingScore", String.format("%1.1f",TapperEvaluation.evaluation(indicator,content)));
        editor.apply();
    }


    @Override
    protected void onPause() {

        if(mp!=null)
        {
            mp.stop();
            mp.release();
            mp = null;
        }
        if(ttv!=null)
        {
            ttv.stop();
            ttv = null;
        }
        if(sweetAlertDialog!=null&&sweetAlertDialog.isShowing())
        {
            sweetAlertDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(ttv!=null)
        {
            ttv.stop();
            ttv = null;
        }
        startActivity(new Intent(TapperTestingActivity.this,TapperMainActivity.class));
        finish();
    }
}
