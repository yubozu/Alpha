package cn.ac.ict.alpha.presenters;

import java.util.ArrayList;
import java.util.HashMap;

import cn.ac.ict.alpha.activities.ResultActivity;

/**
 * Author: yangxiaodong
 * Date: 11/16/16
 */

public class ResultPresenter {
    public static final String TAG = "ResultPresenter";

    public ResultActivity mResultView;

    public ResultPresenter(ResultActivity resultActivity) {
        mResultView = resultActivity;
    }

    public void loadTestData() {
        //TODO: Load test data from shared preference
        ArrayList<HashMap> list = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        map.put("test_name","认知游戏");
        map.put("start_time","2016-11-16 14:50");
        map.put("end_time","2016-11-16 15:50");
        map.put("score","10");
        list.add(map);
        HashMap<String,String> map1 = new HashMap<>();
        map1.put("test_name","行走平衡");
        map1.put("start_time","2016-11-16 14:50");
        map1.put("end_time","2016-11-16 15:50");
        map1.put("score","10");
        list.add(map1);
        list.add(map1);list.add(map1);list.add(map1);
        mResultView.onTestDataLoaded(list);
    }

}
