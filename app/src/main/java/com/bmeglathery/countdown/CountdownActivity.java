package com.bmeglathery.countdown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.GregorianCalendar;

import static com.bmeglathery.countdown.MainActivity.BACKGROUND;
import static com.bmeglathery.countdown.MainActivity.DEBUG;
import static com.bmeglathery.countdown.MainActivity.EVENT_TIME;
import static com.bmeglathery.countdown.MainActivity.TIMER_NAME;

/**
 * Displays a countdown to a future event. The starting time
 * is initialized using intent extras.
 */
public class CountdownActivity extends AppCompatActivity {

    private int daysRemaining;
    private int hrsRemaining;
    private int minsRemaining;
    private int secsRemaining;
    private long eventTime;
    private long leftOver;
    private String background;

    private static final DecimalFormat f = new DecimalFormat("00");

    public static final String RESULT_MESSAGE = "resultMessage";
    public static final int SECS_PER_DAY = 86400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        Bundle extras = getIntent().getExtras();

        int year = extras.getInt(MainActivity.YEAR);
        int month = extras.getInt(MainActivity.MONTH);
        int days = extras.getInt(MainActivity.DAY);
        int hours = extras.getInt(MainActivity.HOUR);
        int minutes = extras.getInt(MainActivity.MINUTE);

        boolean isResumed = extras.getBoolean("Resumed");

        long resumedEventTime = extras.getLong("EventTime");
        long diffSecs;

        String timerName = extras.getString("TimerName");
        GregorianCalendar now = new GregorianCalendar();

        background = extras.getString("Background");
        if (background == null)
            background = "";

        RelativeLayout cl = (RelativeLayout) findViewById(R.id.surroundingLayout);
        EditText editor = (EditText) findViewById(R.id.timerEdit);
        editor.setText(timerName);

        /**
         * Android DatePicker indexes months starting at 0,
         * so December is 11, October is 9, and April is 3
         */
        if (month == 11 && days == 25 || background.equalsIgnoreCase("Christmas")) {
            cl.setBackgroundResource(R.drawable.christmas_bg);
            background = "Christmas";
        } else if (month == 9 && days == 31 || background.equalsIgnoreCase("Halloween")) {
            cl.setBackgroundResource(R.drawable.halloween_bg);
            background = "Halloween";
        } else if (month == 10 || background.equalsIgnoreCase("Fall")) {
            cl.setBackgroundResource(R.drawable.fall_bg);
            background = "Fall";
        }
        /**
         * Easter Sunday for the year 2018 is on April 1st.
         * If modifying this code for long-term use, calculations
         * to determine which day Easter Sunday falls on can be found
         * online.
         */
        else if (month == 3 && days == 1 || background.equalsIgnoreCase("Easter")) {
            cl.setBackgroundResource(R.drawable.easter_bg);
            background = "Easter";
        }
        else if (timerName.matches(".*[bB]irthday.*")) {
            cl.setBackgroundResource(R.drawable.birthday);
            background = "Birthday";
        } else {
            //It's not one of the cool holidays, so the user sees a default background.
        }

        // Check if the timer is being resumed - if so, pass value stored earlier,
        // otherwise build a <code>GregorianCalender</code> with the passed values
        if(!isResumed) {
            // Calculate time in seconds from now until event...
            GregorianCalendar later = new GregorianCalendar(year, month, days, hours, minutes);
            eventTime = later.getTimeInMillis();
        } else {
            eventTime = resumedEventTime;
        }

        long nowTime = now.getTimeInMillis();
        diffSecs = (eventTime - nowTime) / 1000;

        // Calculate time remaining until event
        daysRemaining = (int) diffSecs / SECS_PER_DAY;
        leftOver = diffSecs - daysRemaining * SECS_PER_DAY;

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

            //The long representation of the number of milliseconds is sometimes negative,
            //causing issues within 25 day periods of time. I cannot figure out a solution to this
            //problem...
            public MyTimer() {
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
    }

    /**
     * 'Save and Return' button safely returns the user to the <code>MainActivity</code>,
     * and stores the user's selected in the <code>TimerState</code> associated
     * with the <code>RadioButton</code> selected.
     */
    public void buttonClickHandler(View view){
        Intent i = getIntent();
        EditText text = (EditText) findViewById(R.id.timerEdit);
        String timerName = text.getText().toString();

        i.putExtra(EVENT_TIME, eventTime);
        i.putExtra(BACKGROUND, background);
        i.putExtra(TIMER_NAME, timerName);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
