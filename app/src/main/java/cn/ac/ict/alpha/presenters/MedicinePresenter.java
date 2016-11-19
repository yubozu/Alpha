package cn.ac.ict.alpha.presenters;

import android.content.Context;
import android.content.SharedPreferences;

import cn.ac.ict.alpha.activities.MedicineActivity;

/**
 * Author: yangxiaodong
 * Date: 11/16/16
 */

public class MedicinePresenter {
    public static final String TAG = "MedicinePresenter";

    public MedicineActivity mMedicineView;

    public MedicinePresenter(MedicineActivity medicineView) {
        mMedicineView = medicineView;
    }


    public void saveMedInfo(boolean checked, int selectedIndex) {
        //TODO: save current medicine info and current time into SharePreference
        SharedPreferences sharedPreferences = mMedicineView.getSharedPreferences("Alpha", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("takingMed",checked);
        editor.putInt("lastTaken",selectedIndex);
        editor.apply();
        mMedicineView.onPrepared();
    }
}
