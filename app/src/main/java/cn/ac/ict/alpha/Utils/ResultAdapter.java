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
    double[] rank_count={0.8,0.4,0.2};
    double[] rank_stand={0.8,1,1.2};
    double[] rank_stride={90,70,50};
    double[] rank_tapping = {80,60,40};
    public ResultAdapter(Context context, List<BaseResult> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }
    private String getRank(double[] rank_step,double score,boolean acsend)
    {
        String[] rank = context.getResources().getStringArray(R.array.rank);
        if(acsend) {
            for (int i = 0; i < rank_step.length; i++) {
                if (score >= rank_step[i]) {
                    return rank[i];
                }
            }
            return rank[3];
        }else
        {
            for (int i = 0; i < rank_step.length; i++) {
                if (score <= rank_step[i]) {
                    return rank[i];
                }
            }
            return rank[3];
        }
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
        double score = Double.parseDouble(list.get(i).getScore());
        switch (list.get(i).getType())
        {
            case 0:
                img.setImageResource(R.drawable.ic_memory);
                tv_name.setText("认知记忆游戏");
                tv_score.setText(getRank(rank_count,score,true));
                break;
            case 1:
                img.setImageResource(R.drawable.ic_stand);
                tv_name.setText("站立平衡");
                tv_score.setText(getRank(rank_stand,score,false));
                break;
            case 2:
                img.setImageResource(R.drawable.ic_face);
                tv_name.setText("面部表情");
                tv_score.setText(context.getString(R.string.recorded));
                break;
            case 3:
                img.setImageResource(R.drawable.ic_sound);
                tv_name.setText("语音测试");
                tv_score.setText(context.getString(R.string.recorded));
                break;
            case 4:
                img.setImageResource(R.drawable.ic_tapping);
                tv_name.setText("手指点击灵活");
                tv_score.setText(getRank(rank_tapping,score,true));
                break;
            case 5:
                img.setImageResource(R.drawable.ic_walk);
                tv_name.setText("步态平衡");
                tv_score.setText(getRank(rank_stride,score,true));
                break;
        }
        return view;
    }
}
