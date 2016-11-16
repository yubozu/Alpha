package cn.ac.ict.alpha.activities;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.presenters.ResultPresenter;

public class ResultActivity extends BaseActivity {

    public static final String TAG = "ResultActivity";
    @BindView(R.id.lv_test_results)
    ListView lvTestResults;
    @BindView(R.id.bt_evaluate_self)
    Button btEvaSelf;
    @BindView(R.id.tv_guide_info)
    TextView tvGuide;
    private ResultPresenter mResultPresenter;

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
    public void onTestDataLoaded(ArrayList  list)
    {
        String[] keys={"test_name","start_time","end_time","score"};
        int[] tvs = {R.id.tv_test_name,R.id.tv_start_time,R.id.tv_end_time,R.id.tv_score};
        SimpleAdapter adapter = new SimpleAdapter(ResultActivity.this,list,R.layout.item_test_result,keys,tvs);
        lvTestResults.setAdapter(adapter);
        tvGuide.setText(String.format(getString(R.string.guide_info),list.size(),50));

    }
    @OnClick(R.id.bt_evaluate_self)
    public void onClick(View view) {

    }


}
