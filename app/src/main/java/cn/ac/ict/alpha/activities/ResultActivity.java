package cn.ac.ict.alpha.activities;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.ResultAdapter;
import cn.ac.ict.alpha.presenters.ResultPresenter;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ResultActivity extends BaseActivity {

    public static final String TAG = "ResultActivity";
    @BindView(R.id.lv_test_results)
    ListView lvTestResults;
    @BindView(R.id.bt_evaluate_self)
    Button btEvaSelf;
    @BindView(R.id.tv_guide_info)
    TextView tvGuide;
    private ResultPresenter mResultPresenter;
    SweetAlertDialog sweetAlertDialog = null;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    protected void init() {
        super.init();
        mResultPresenter = new ResultPresenter(this);
        //TODO: get data form m ResultPresentor
        mResultPresenter.loadTestData();
    }
    public void onTestDataLoaded(ArrayList  list,String startTime,String endTime)
    {
        ResultAdapter adapter = new ResultAdapter(ResultActivity.this,list);
        lvTestResults.setAdapter(adapter);
        tvGuide.setText(String.format(getString(R.string.guide_info),startTime,endTime,list.size()));
    }
    @OnClick(R.id.bt_evaluate_self)
    public void onClick(View view) {
        // TODO: 18/11/2016
        startActivity(MainActivity.class);
    }

    @Override
    public void onBackPressed() {
        sweetAlertDialog = new SweetAlertDialog(ResultActivity.this,SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("退出")
                .setContentText("确定退出评估吗？")
                .setConfirmText("是的")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
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

    @Override
    protected void onPause() {
        if(sweetAlertDialog!=null&&sweetAlertDialog.isShowing())
        {
            sweetAlertDialog.dismiss();
        }
        super.onPause();

    }
}
