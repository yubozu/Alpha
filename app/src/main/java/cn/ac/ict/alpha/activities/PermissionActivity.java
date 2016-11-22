package cn.ac.ict.alpha.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cengalabs.flatui.FlatUI;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ac.ict.alpha.Entities.PermissionEntity;
import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.Utils.PermissionAdapter;
import cn.ac.ict.alpha.Utils.ToastManager;


/**
 * Author: saukymo
 * Date: 9/12/16
 */
public class PermissionActivity extends BaseActivity {

    @BindView(R.id.bt_permission_pass)
    Button button;
    @BindView(R.id.tv_permission_info)
    TextView tvPermissionInfo;

    @BindView(R.id.lv_permission_not_granted)
    ListView lvPermission;

    @BindView(R.id.lv_permission_granted)
    ListView lvPermissionGranted;
    @BindView(R.id.layout_permission)
    LinearLayout lyPermission;

    ToastManager toastManager;
    PermissionAdapter mPermissionAdapter;
    PermissionAdapter mPermissionGrantedAdapter;

    private ArrayList<PermissionEntity> mPermissionEntitySet = new ArrayList<>();
    private ArrayList<PermissionEntity> mPermissionEntityGrantedSet = new ArrayList<>();

    @Override
    protected void init() {
        super.init();
        GeneratePermissionSet();
        InitialPermissionCheckList();
        PermissionCheckStatus();
        //如果程序开始运行时,所有的权限都已经开通了的话,直接跳过这个界面;
        if (mPermissionAdapter.getCount() == 0) {
            Log.d("PermissionEntity", "Skip permission");
            startOurApp();
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_permission;
    }


    public void GeneratePermissionSet() {
        ArrayList<PermissionEntity> permissionEntities = new ArrayList<>();
        permissionEntities.add(new PermissionEntity(Manifest.permission.CAMERA));
        permissionEntities.add(new PermissionEntity(Manifest.permission.VIBRATE));
        permissionEntities.add(new PermissionEntity(Manifest.permission.INTERNET));
        permissionEntities.add(new PermissionEntity(Manifest.permission.RECORD_AUDIO));
        permissionEntities.add(new PermissionEntity(Manifest.permission.ACCESS_NETWORK_STATE));
        permissionEntities.add(new PermissionEntity(Manifest.permission.ACCESS_WIFI_STATE));
        permissionEntities.add(new PermissionEntity(Manifest.permission.READ_PHONE_STATE));

        for (PermissionEntity permissionEntity : permissionEntities) {
            permissionEntity.permissionStatus = getPackageManager().checkPermission(permissionEntity.permissionName, getPackageName()) == PackageManager.PERMISSION_GRANTED;
            if (permissionEntity.permissionStatus) {
                mPermissionEntityGrantedSet.add(permissionEntity);
            } else {
                mPermissionEntitySet.add(permissionEntity);
            }
        }
    }

    public void InitialPermissionCheckList() {
        mPermissionAdapter = new PermissionAdapter(PermissionActivity.this, mPermissionEntitySet);
        mPermissionGrantedAdapter = new PermissionAdapter(PermissionActivity.this, mPermissionEntityGrantedSet);

        lvPermission.setAdapter(mPermissionAdapter);
        lvPermissionGranted.setAdapter(mPermissionGrantedAdapter);
    }

    @OnClick(R.id.bt_permission_pass)
    public void startOurApp() {
        Log.d("PermissionActivity", "Start Main activity");
//        toastManager.show("Start main activity.");
        Intent intent = new Intent(PermissionActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//           toastManager.show("PermissionEntity Granted");
            Log.d(TAG, "Granted");
            PermissionEntity permissionEntity = mPermissionAdapter.getItem(requestCode);
            permissionEntity.permissionStatus = true;

            mPermissionAdapter.deleteItem(requestCode);
            mPermissionGrantedAdapter.addItem(permissionEntity);

        } else {
            toastManager.show(getResources().getString(R.string.permission_denied));
        }
        mPermissionAdapter.notifyDataSetChanged();
        mPermissionGrantedAdapter.notifyDataSetChanged();

        PermissionCheckStatus();
    }

    public void PermissionCheckStatus() {
        boolean is_passed = mPermissionAdapter.getCount() == 0;
        if (is_passed) {
//            tvPermissionInfo.setVisibility(View.GONE);
//            lvPermission.setVisibility(View.GONE);
            lyPermission.setVisibility(View.GONE);
            button.setText(R.string.btn_continue);
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.freebie_4));
        } else {
            button.setText(String.format(Locale.CHINA, getResources().getString(R.string.permission_remained), mPermissionAdapter.getCount()));
        }
        button.setEnabled(is_passed);
    }

    @Override
    public void onStart() {
        super.onStart();
        FlatUI.initDefaultValues(this);
    }
}