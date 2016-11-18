package cn.ac.ict.alpha.activities.sound;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ac.ict.alpha.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestingFragment extends Fragment {

    @BindView(R.id.btn_start_recorder)
    Button mBtnStartRecorder;
    @BindView(R.id.tv_quston)
    TextView mQueston;
    private String[] questons;
    int currentQueston = 0;
    @BindView(R.id.iv_sound_mic)
    ImageView iv_mic;
    private SoundMainActivity mActivity;
    private AnimationDrawable recordingTransition;

    public TestingFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sound_testing, container, false);
        ButterKnife.bind(this, view);
        mActivity = (SoundMainActivity) getActivity();
        iv_mic.setBackgroundResource(R.drawable.mic_animation);
        recordingTransition = (AnimationDrawable)iv_mic.getBackground();
        recordingTransition.start();
        mActivity.prepareRecorder();
        questons = getResources().getStringArray(R.array.SoundQueston);
        mQueston.setText(questons[0]);
        return view;
    }

    @OnClick(R.id.btn_start_recorder)
    public void click() {
        ++ currentQueston;
        if (currentQueston == questons.length - 1) {
            mBtnStartRecorder.setText(mActivity.getString(R.string.sound_complete));
        }
        if (currentQueston < questons.length) {
            mQueston.setText(questons[currentQueston]);
        } else {
            mActivity.finishTesting();
        }
    }
}
