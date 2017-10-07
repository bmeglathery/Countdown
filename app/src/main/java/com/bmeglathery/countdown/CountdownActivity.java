package com.bmeglathery.countdown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.GregorianCalendar;

/**
 * Displays a countdown to a future event. The starting time
 * is initialized using intent extras.
 */

public class CountdownActivity extends AppCompatActivity {

    private int daysRemaining;
    private int hrsRemaining;
    private int minsRemaining;
    private int secsRemaining;

    private static final DecimalFormat f = new DecimalFormat("00");

    public static final String RESULT_MESSAGE = "resultMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        Bundle extras = getIntent().getExtras();
        int year = extras.getInt(MainActivity.YEAR);
        int month = extras.getInt(MainActivity.MONTH);
        int days = extras.getInt(MainActivity.DAY);
        int hours = extras.getInt(MainActivity.HOUR);
        int mins = extras.getInt(MainActivity.MINUTE);

        // Calculate time in seconds from now until event...
        GregorianCalendar later = new GregorianCalendar(year, month, days, hours, mins);
        GregorianCalendar now = new GregorianCalendar();

        long eventTime = later.getTimeInMillis();
        long nowTime = now.getTimeInMillis();
        long diffSecs = (long) ((eventTime - nowTime) / 1000);

        // Calculate time remaining until event
        final int SECS_PER_DAY = 86400;
        daysRemaining = (int) diffSecs / SECS_PER_DAY;
        long leftOver = diffSecs - daysRemaining * SECS_PER_DAY;

        hrsRemaining = (int) leftOver / 3600;
        leftOver -= hrsRemaining * 3600;

        minsRemaining = (int) leftOver / 60;
        leftOver -= minsRemaining * 60;

        secsRemaining = (int) leftOver;

        // get references to TextViews and initialize
        final TextView daysView = (TextView) findViewById(R.id.daysRemainingView);
        final TextView hrsView = (TextView) findViewById(R.id.hrsRemainingView);
        final TextView minsView = (TextView) findViewById(R.id.minsRemainingView);
        final TextView secsView = (TextView) findViewById(R.id.secsRemainingView);


        class MyTimer extends CountDownTimer{

            public MyTimer() {
                // Constructor:
                // Number of milliseconds begin counting down from,
                // Every 1000 ms, onTick event occurs
                super((secsRemaining + (60 * minsRemaining)
                        + (3600 * hrsRemaining) + (SECS_PER_DAY * daysRemaining)) * 1000, 1000);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                secsRemaining--;
                if(secsRemaining < 0){
                    secsRemaining = 59;
                    minsRemaining--;
                    if(minsRemaining < 0){
                        minsRemaining = 59;
                        hrsRemaining--;
                        if(hrsRemaining < 0){
                            hrsRemaining = 23;
                            daysRemaining--;
                        }
                    }
                }
                daysView.setText(f.format(daysRemaining));
                hrsView.setText(f.format(hrsRemaining));
                minsView.setText(f.format(minsRemaining));
                secsView.setText(f.format(secsRemaining));
            }

            @Override
            public void onFinish() {
                finish();
            }
        }

        MyTimer timer = new MyTimer();
        timer.start();

        // Code-based approach to return to parent activity via
        // up button.
        ActionBar aBar = getSupportActionBar();
        aBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Our CountdownActivity has no button, but if it did then we could
     * handle a click here and return an intent to the parent activity.
     */
    public void buttonClickHandler(View view){
        Intent i = getIntent();
        GregorianCalendar time_remaining_gc =
                new GregorianCalendar(daysRemaining/365, daysRemaining, hrsRemaining, minsRemaining, secsRemaining);
        long millis_remaining = time_remaining_gc.getTimeInMillis();
        i.putExtra(RESULT_MESSAGE, daysRemaining + " days remaining.");
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
