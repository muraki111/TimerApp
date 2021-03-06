//参考文献：https://codinginflow.com/tutorials/android/countdowntimer/part-1-countdown-timer

package com.example.TimerApp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.TimedText;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 1;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private Button mButton30;
    private Button mButton1;
    private Button mButton5;
    private ProgressBar progressBar;
    private double percent;
    private double percent2;
    private String timeText;
    private static final String TAG = "MyActivity";
    private SoundPlayer soundPlayer;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long a=0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        percent = 100; //初期状態を100%にします。
        soundPlayer = new SoundPlayer(this);

        progressBar = (ProgressBar)findViewById(R.id.Progress0);
        progressBar.setMax(100);
        progressBar.setProgress((int)percent,true);
        progressBar.setMin(0);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mButton30 = findViewById(R.id.button_30);
        mButton1 = findViewById(R.id.button_1);
        mButton5 = findViewById(R.id.button_5);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                percent = 100;
                a=0;
                progressBar.setProgress((int)percent);
            }
        });
        mButton30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeLeftInMillis += 30000;
                updateCountDownText();
            }
        });
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeLeftInMillis += 60000;
                updateCountDownText();
            }
        });
        mButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeLeftInMillis += 300000;
                updateCountDownText();
            }
        });

        updateCountDownText();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                percent -= 100.0/((a+mTimeLeftInMillis)/1000.0);
                a += 1000;
                int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
                int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "");
                if(1000+mTimeLeftInMillis==a+mTimeLeftInMillis){
                    timeText = minutes+":"+seconds;
                }
                if(minutes == 0 && !(seconds == 0)){
                    soundPlayer.playlittleSound();
                    Drawable customDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.circle_progress_1min, null);
                    progressBar.setProgressDrawable(customDrawable);
                }else {
                    Drawable customDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.circle_progress, null);
                    progressBar.setProgressDrawable(customDrawable);
                }

                Log.d(TAG, String.valueOf("設定時間："+ timeText +" 　現在時間："+minutes+":"+seconds+"　　現在のパーセンテージ："+(percent)+"　　引いたパーセンテージ："+(percent-percent2)));
                percent2 = percent;
                progressBar.setProgress((int)percent);
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
                soundPlayer.playalarmSound();
                Drawable customDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.circle_progress, null);
                progressBar.setProgressDrawable(customDrawable);
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("pause");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }
}