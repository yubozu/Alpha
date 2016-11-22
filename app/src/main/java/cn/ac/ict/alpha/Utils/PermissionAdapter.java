package cn.ac.ict.alpha.Utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.ac.ict.alpha.Entities.PermissionEntity;
import cn.ac.ict.alpha.R;

/**
 * Author: saukymo
 * Date: 9/12/16
 * Reference: https://github.com/excilys/androidannotations/wiki/Adapters-and-lists
 */

public class PermissionAdapter extends BaseAdapter {

    ToastManager toastManager;
    Context mContext;
    LayoutInflater layoutInflater;
    private List<PermissionEntity> mPermissionEntitySet;
    public PermissionAdapter(Context context, List<PermissionEntity> mPermissionEntitySet)
    {
        this.mContext = context;
        this.mPermissionEntitySet = mPermissionEntitySet;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null) {
            view = layoutInflater.inflate(R.layout.adapter_permission,null);
        }

        Button btnPermissionName = (Button)view.findViewById(R.id.btn_permission_name);

        PermissionEntity permissionEntity = getItem(position);
        btnPermissionName.setText(permissionEntity.permissionName);
        if (!permissionEntity.permissionStatus) {
            btnPermissionName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CheckedChanged", String.valueOf(mPermissionEntitySet.size()) + "," + String.valueOf(position));
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{mPermissionEntitySet.get(position).permissionName}, position);
                }
            });
        } else {
            btnPermissionName.setEnabled(false);
            btnPermissionName.setBackgroundColor(mContext.getResources().getColor(R.color.freebie_9));
            btnPermissionName.setTextColor(mContext.getResources().getColor(R.color.freebie_6));
        }

        return view;

    }

    public void setList(ArrayList<PermissionEntity> permissionEntitySet) {
        mPermissionEntitySet = permissionEntitySet;
    }
    @Override
    public int getCount() {
        return mPermissionEntitySet.size();
    }

    @Override
    public PermissionEntity getItem(int position) {
        return mPermissionEntitySet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(PermissionEntity permissionEntity){
        mPermissionEntitySet.add(permissionEntity);
    }

    public void deleteItem(int position) {
        mPermissionEntitySet.remove(position);
    }


}
