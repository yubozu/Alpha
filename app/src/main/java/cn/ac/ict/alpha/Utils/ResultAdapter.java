package cn.ac.ict.alpha.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ac.ict.alpha.R;

/**
 * Created by yxd on 18/11/2016.
 */

public class ResultAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<BaseResult> list;
    public ResultAdapter(Context context, List<BaseResult> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public BaseResult getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view ==null)
        {
            view= layoutInflater.inflate(R.layout.item_test_result,null);
        }
        ImageView img = (ImageView)view.findViewById(R.id.img);
        TextView tv_name = (TextView)view.findViewById(R.id.tv_test_name);
        TextView tv_score = (TextView)view.findViewById(R.id.tv_score);
        switch (list.get(i).getType())
        {
            case 0:
                img.setImageResource(R.drawable.ic_memory);
                tv_name.setText("认知记忆游戏");
                break;
            case 1:
                img.setImageResource(R.drawable.ic_stand);
                tv_name.setText("站立平衡");
                break;
            case 2:
                img.setImageResource(R.drawable.ic_face);
                tv_name.setText("面部表情");
                break;
            case 3:
                img.setImageResource(R.drawable.ic_sound);
                tv_name.setText("语音测试");
                break;
            case 4:
                img.setImageResource(R.drawable.ic_tapping);
                tv_name.setText("手指点击灵活");
                break;
            case 5:
                img.setImageResource(R.drawable.ic_walk);
                tv_name.setText("步态平衡");
                break;
        }
        tv_score.setText(String.valueOf(list.get(i).getScore()));
        return view;
    }
}
