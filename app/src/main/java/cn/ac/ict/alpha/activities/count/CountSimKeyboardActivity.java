package cn.ac.ict.alpha.activities.count;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.FileUtils;
import cn.ac.ict.alpha.activities.stand.StandMainActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CountSimKeyboardActivity extends Activity {

    private String randomStr;
    private String version;
    private TextView nextet;
    private Button nextbtn;
    private Intent intent;
    private int times;
    private boolean isRight;
    private ArrayList<String> result;
    private GridLayout gridLayout;
    private String[] chars;
    private boolean isNotFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_simkeyboard);

        init();
    }

    SweetAlertDialog sweetAlertDialog = null;

    public void init() {
        isNotFull = true;
        chars = new String[]{
                "7", "8", "9",
                "6", "5", "4",
                "3", "2", "1",
                "0", getApplicationContext().getString(R.string.count_sim_clear), getApplication().getString(R.string.count_sim_delete)
        };

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int oneQuarterWidth = (int) (screenWidth * 0.30);

        intent = this.getIntent();
        randomStr = intent.getStringExtra("data");
        version = intent.getStringExtra("version");
        randomStr = randomStr.substring(0, 6);
        nextet = (TextView) findViewById(R.id.count_simkeyboard_tv);
        nextbtn = (Button) findViewById(R.id.count_simkeyboard_confirmBtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = nextet.getText().toString().trim();
                if (str.length() < 6) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(CountSimKeyboardActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.input_hint))
                            .setContentText("请输入6个数字")
                            .setConfirmText(getString(R.string.btn_confirm))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            });
                    sweetAlertDialog.show();
                    return;
                }
                result.add(str);
                if (str.equals(randomStr)) {
                    isRight = true;
                    saveAndContinue();
                } else {
                    times++;
                    if (times >= 5) {
                        saveAndContinue();
                        return;
                    }
                    sweetAlertDialog = new SweetAlertDialog(CountSimKeyboardActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("输入错误")
                            .setContentText(String.format(Locale.CHINA, getString(R.string.count_wrong_answer), times))
                            .setConfirmText("重新输入")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            });
                    sweetAlertDialog.show();
                }
            }
        });
        result = new ArrayList<>();
        gridLayout = (GridLayout) findViewById(R.id.coutn_gridlayout_root);
        for (int i = 0; i < chars.length; i++) {
            Button btn = new Button(this);
            btn.setBackgroundResource(R.drawable.count_key_button_style);
            btn.setText(chars[i]);
            if (i > 9) {
                btn.setTextSize(30);
            } else {
                btn.setTextSize(40);
            }
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Button btn = (Button) arg0;

                    String btnText = btn.getText().toString().trim();

                    if (!isNotFull && btnText.length() < 2) {
                        sweetAlertDialog = new SweetAlertDialog(CountSimKeyboardActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.input_hint))
                                .setContentText(getString(R.string.input_full_hint))
                                .setConfirmText(getString(R.string.btn_confirm))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                        sweetAlertDialog.show();
                    }


                    String str = nextet.getText().toString();
                    if (btnText.equals("删除") || btnText.equals("delete")) {
                        deleteText(nextet);
                        isNotFull = true;
                    } else if (btnText.equals("清空") || btnText.equals("clear")) {
                        clearText(nextet);
                        isNotFull = true;
                    } else {
                        btnText = str.concat(btnText);
                        nextet.setText(btnText);
                        if (btnText.length() > 5) {
                            isNotFull = false;
                        } else {
                            isNotFull = true;
                        }
                    }
                }

            });
            GridLayout.Spec rowSpec = GridLayout.spec(i / 3);
            GridLayout.Spec colSpec = GridLayout.spec(i % 3);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
            params.setGravity(Gravity.FILL);
            gridLayout.addView(btn, params);
            params.width = oneQuarterWidth;
        }

    }

    protected void saveAndContinue() {
        String content = randomStr;
        if (isRight) {
            content += ";1";
        } else {
            content += ";0";
        }
        if (version.equals("sound")) {
            content += ";1";
        } else {
            content += ";0";
        }
        for (String x : result) {
            content += ";" + x;
        }
        saveToStorage(content);
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("测试完成！")
                .setContentText("点击确定进行下一项测试")
                .setConfirmText("确定")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(CountSimKeyboardActivity.this, StandMainActivity.class));
                        finish();
                    }
                });
        sweetAlertDialog.show();

    }

    public void saveToStorage(String content) {
        SharedPreferences sharedPreferences = getSharedPreferences("Alpha", Context.MODE_PRIVATE);
        // Example: How to write data to file.
        String filePath = FileUtils.getFilePath(this, "memory");
        File file = new File(filePath);
        try {
            FileWriter fileWrite = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWrite);
            bufferedWriter.write(content);
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWrite.close();
        } catch (IOException e) {
            Log.e("ExamAdapter", e.toString());
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("memoryFilePath", filePath);
        editor.putString("memoryScore", String.valueOf(CountEvaluation.CountEvaluation(isRight, result)));
        editor.apply();
    }

    public void clearText(TextView v) {
        v.setText("");
    }

    public void deleteText(TextView v) {
        String str = v.getText().toString().trim();
        if (str.equals("")) {
            return;
        }
        str = str.substring(0, str.length() - 1);
        v.setText(str);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(CountSimKeyboardActivity.this, CountMainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, CountMainActivity.class));
        finish();
    }
}
