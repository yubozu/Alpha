package cn.ac.ict.alpha.activities.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    SoundMainActivity mActivity;
    SweetAlertDialog sweetAlertDialog;
    MediaPlayer mp;

    public MainFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sound_main, container, false);
        ButterKnife.bind(this, view);
        mActivity = (SoundMainActivity) getActivity();
        mp = MediaPlayer.create(mActivity.getApplicationContext(), R.raw.sound);
        mp.start();
        return view;
    }
    @OnClick({R.id.tv_prev,R.id.bt_start,R.id.bt_skip})
    public void click(View view) {
        if(mp!=null)
        {
            mp.stop();
            mp.release();
            mp=null;

        }
        switch(view.getId())
        {
            case R.id.tv_prev:
                mActivity.prev();
                break;
            case R.id.bt_start:
                mActivity.isTesting = true;
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Alpha", Context.MODE_PRIVATE);
                double score = Double.parseDouble(sharedPreferences.getString("soundScore", "-1"));
                if (score >= 0) {
                    sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定吗？")
                            .setContentText("本次评估已经进行此测试，是否重新测试？")
                            .setConfirmText("是的，重新测试")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    mActivity.prepareRecorder();
                                    getFragmentManager().beginTransaction().replace(R.id.content, new TestingFragment()).commit();
                                }
                            })
                            .setCancelText("不，进行下一项")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    mActivity.next();
                                }
                            });
                    sweetAlertDialog.show();
                } else {
                    mActivity.prepareRecorder();
                    getFragmentManager().beginTransaction().replace(R.id.content, new TestingFragment()).commit();
                }

                break;
            case R.id.bt_skip:
                sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(getString(R.string.confirm_skip))
                        .setContentText(getString(R.string.skip_dialog_content))
                        .setConfirmText(getString(R.string.skip_negative))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelText(getString(R.string.skip_positive))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mActivity.next();
                            }
                        });
                sweetAlertDialog.show();
                break;
        }
    }

    @Override
    public void onPause() {
        if(sweetAlertDialog!=null&&sweetAlertDialog.isShowing())
        {
            sweetAlertDialog.dismiss();
        }
        if(mp!=null)
        {
            mp.stop();
            mp.release();
            mp=null;

        }
        super.onPause();
    }



}
