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

import cn.ac.ict.alpha.R;
import cn.ac.ict.alpha.models.Permission;

/**
 * Author: saukymo
 * Date: 9/12/16
 * Reference: https://github.com/excilys/androidannotations/wiki/Adapters-and-lists
 */

public class PermissionAdapter extends BaseAdapter {

    ToastManager toastManager;
    Context mContext;
    LayoutInflater layoutInflater;
    private List<Permission> mPermissionSet;
    public PermissionAdapter(Context context, List<Permission> mPermissionSet)
    {
        this.mContext = context;
        this.mPermissionSet = mPermissionSet;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null) {
            view = layoutInflater.inflate(R.layout.adapter_permission,null);
        }

        Button btnPermissionName = (Button)view.findViewById(R.id.btn_permission_name);

        Permission permission = getItem(position);
        btnPermissionName.setText(permission.permissionName);
        if (!permission.permissionStatus) {
            btnPermissionName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CheckedChanged", String.valueOf(mPermissionSet.size()) + "," + String.valueOf(position));
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{mPermissionSet.get(position).permissionName}, position);
                }
            });
        } else {
            btnPermissionName.setEnabled(false);
            btnPermissionName.setBackgroundColor(mContext.getResources().getColor(R.color.freebie_9));
            btnPermissionName.setTextColor(mContext.getResources().getColor(R.color.freebie_6));
        }

        return view;

    }

    public void setList(ArrayList<Permission> permissionSet) {
        mPermissionSet = permissionSet;
    }
    @Override
    public int getCount() {
        return mPermissionSet.size();
    }

    @Override
    public Permission getItem(int position) {
        return mPermissionSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Permission permission){
        mPermissionSet.add(permission);
    }

    public void deleteItem(int position) {
        mPermissionSet.remove(position);
    }


}
