package cn.ac.ict.alpha.activities;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.presenters.MedicinePresenter;

public class MedicineActivity extends BaseActivity {

    public static final String TAG = "MedicineActivity";
    @BindView(R.id.tg_medicine)
    ToggleButton tgMedicine;
    @BindView(R.id.spinner_medicine_time)
    MaterialSpinner spinnerMedTime;
    @BindView(R.id.cb_tip)
    CheckBox cbTip;
    @BindView(R.id.bt_med_start)
    Button btMedStart;
    @BindView(R.id.ll_medicine_time)
    LinearLayout llMedicineTime;

    private MedicinePresenter mMedicinePresenter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_medicine;
    }

    @Override
    protected void init() {
        super.init();
        mMedicinePresenter = new MedicinePresenter(this);
        spinnerMedTime.setItems(getResources().getStringArray(R.array.time));
        spinnerMedTime.setSelectedIndex(0);
    }

    @OnClick(R.id.bt_med_start)
    public void onClick(View view) {
        if(tgMedicine.isChecked()&&spinnerMedTime.getSelectedIndex()<=0)
        {
            toast(getString(R.string.must_input_time));
            return;
        }
        if(!cbTip.isChecked())
        {
            toast(getString(R.string.must_read_tip));
            return;
        }
        showProgress("正在准备第一项测试");
        mMedicinePresenter.saveMedInfo(tgMedicine.isChecked(),spinnerMedTime.getSelectedIndex());
    }
    public void onPrepared()
    {
        hideProgress();
        toast("Prepared");
        //TODO: start the first test
//        startActivity(MedicineActivity.this,);
    }
    @OnCheckedChanged(R.id.tg_medicine)
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int vis = b?View.VISIBLE:View.INVISIBLE;
        llMedicineTime.setVisibility(vis);
    }
}
