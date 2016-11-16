package cn.ac.ict.alpha.presenters;

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
        mMedicineView.onPrepared();
    }
}
