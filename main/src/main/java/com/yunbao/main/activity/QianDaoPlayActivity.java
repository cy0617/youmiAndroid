package com.yunbao.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yunbao.common.http.HttpCallback;
import com.yunbao.main.R;
import com.yunbao.main.http.MainHttpUtil;

import cn.jzvd.JzvdStd;

public class QianDaoPlayActivity extends AppCompatActivity {

    private JzvdStd  jcVideoPlayerStandard;
    private String thumb;
    private String min;
    private Integer integer;
    private TextView mTextField;
    private Intent intent;
    private boolean isPlayResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qian_dao_play);
        init();
    }

    private void init() {
        mTextField = findViewById(R.id.tv);
        intent = getIntent();
        min = intent.getStringExtra("min");
        thumb = intent.getStringExtra("thumb");
        integer = Integer.valueOf(min);
        jcVideoPlayerStandard = (JzvdStd) findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(thumb, "");
        jcVideoPlayerStandard.startButton.performClick();
        jcVideoPlayerStandard.startVideo();
        new CountDownTimer(integer * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTextField.setText("倒计时还有: " + millisUntilFinished / 1000 + "秒完成签到");
            }

            public void onFinish() {
                MainHttpUtil.getQianDao(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, String[] info) {
                        Bundle bundle = new Bundle();
                        bundle.putString("code",String.valueOf(code));
                        bundle.putString("msg",msg);
                        intent.putExtras(bundle);
                        setResult(100,intent);
                        finish();
                    }
                });
            }
        }.start();
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (isPlayResume) {
            jcVideoPlayerStandard.goOnPlayOnResume();

            isPlayResume = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        jcVideoPlayerStandard.goOnPlayOnPause();

        isPlayResume = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (jcVideoPlayerStandard != null) {
//            jcVideoPlayerStandard.release();
        }
        jcVideoPlayerStandard.removeAllViews();
    }

}
