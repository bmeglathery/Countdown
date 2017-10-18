package com.bmeglathery.countdown;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.GregorianCalendar;

/**
 * Provides event handler for buttons in the user interface, which
 * creates an intent with extras from date and time pickers. The
 * intent is used in launching a new activity to display a countdown.
 *
 * Added feature: If the user selects Halloween, Easter, or Christmas
 * as their selected date, a holiday appropriate background is shown.
 * If the user selects a date in November, a Fall-themed background is
 * shown. Lastly, if the user includes the word "birthday" in their timer
 * name, a birthday background is shown!
 *
 * @author Brandon Meglathery
 */
public class MainActivity extends AppCompatActivity {

    public static final String DEBUG = "Debug";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String YEAR = "year";
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    public static final String RESUMED = "Resumed";
    public static final String DEFAULT_TIMER_NAME = "Countdown";
    public static final String BACKGROUND = "Background";
    public static final String EVENT_TIME = "EventTime";
    public static final String TIMER_NAME = "TimerName";

    //For receiving info from CountdownActivity
    private static final int TIMER_1 = 1;
    private static final int TIMER_2 = 2;
    private static final int TIMER_3 = 3;

    private static int timerRequest = 0;

    private TimerState timer1;
    private TimerState timer2;
    private TimerState timer3;

    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);

        // Get saved timer information - if none available, default values are provided.
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);

        timer1 = new TimerState(
                preferences.getString("TIMER_NAME1", DEFAULT_TIMER_NAME),
                preferences.getLong("EVENT_TARGET1", 0),
                preferences.getString("BACKGROUND1", ""));
        timer2 = new TimerState(
                preferences.getString("TIMER_NAME2", DEFAULT_TIMER_NAME),
                preferences.getLong("EVENT_TARGET2", 0),
                preferences.getString("BACKGROUND2", ""));
        timer3 = new TimerState(
                preferences.getString("TIMER_NAME3", DEFAULT_TIMER_NAME),
                preferences.getLong("EVENT_TARGET3", 0),
                preferences.getString("BACKGROUND3", ""));

        rb1.setText(timer1.getTimerName());
        rb2.setText(timer2.getTimerName());
        rb3.setText(timer3.getTimerName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Creates an explicit intent with extras encoding the event
     * date and the time specified by the user.
     */
    public void startCountDown(View view){
        DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        TimePicker tp = (TimePicker) findViewById(R.id.timePicker);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioButtons);

        Intent intent = new Intent(this, CountdownActivity.class);
        intent.putExtra(MONTH, dp.getMonth());
        intent.putExtra(DAY, dp.getDayOfMonth());
        intent.putExtra(YEAR, dp.getYear());
        intent.putExtra(HOUR, tp.getHour());
        intent.putExtra(MINUTE, tp.getMinute());
        intent.putExtra(RESUMED, false);

        int timer_request = 0;
        int selectedTimer = rg.getCheckedRadioButtonId();

        String timerName = "";

        if(selectedTimer == R.id.rb1) {
            timer_request = TIMER_1;
            timerName = timer1.getTimerName();
        }else if (selectedTimer == R.id.rb2) {
            timer_request = TIMER_2;
            timerName = timer2.getTimerName();
        }else if (selectedTimer == R.id.rb3) {
            timer_request = TIMER_3;
            timerName = timer3.getTimerName();
        }else
            Log.d(DEBUG, "selected radio button did not match radioButton id");

        intent.putExtra(TIMER_NAME, timerName);

        startActivityForResult(intent, timer_request);
    }

    /**
     * In contrast to <code>startCountDown</code>, this method
     * retrieves the information associated with the selected
     * <code>RadioButton</code> and passes it to the
     * <code>CountdownActivity</code> along with an extra to
     * indicate that this countdown is being resumed.
     */
    public void resumeCountDown(View view){
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioButtons);
        TimerState timer = new TimerState();
        int selectedTimerID = rg.getCheckedRadioButtonId();

        if(selectedTimerID == R.id.rb1) {
            timer = timer1;
            timerRequest = TIMER_1;
        } else if (selectedTimerID == R.id.rb2) {
            timer = timer2;
            timerRequest = TIMER_2;
        } else if (selectedTimerID == R.id.rb3) {
            timer = timer3;
            timerRequest = TIMER_3;
        } else
            Log.d(DEBUG, "An error occurred in resumeCountDown");

        long event = timer.getEventTarget();
        GregorianCalendar now = new GregorianCalendar();
        long currentSeconds = now.getTimeInMillis() / 1000;

        if(event == 0)
            Toast.makeText(this, "You cannot resume a timer which has not been started!", Toast.LENGTH_LONG).show();
        else if(event / 1000 - currentSeconds < 0)
            Toast.makeText(this, "Your countdown ended while you were away...", Toast.LENGTH_LONG).show();
        else {
            String background = timer.getHolidayBackground();
            String timerName = timer.getTimerName();

            Intent intent = new Intent(this, CountdownActivity.class);
            intent.putExtra(BACKGROUND, background);
            intent.putExtra(EVENT_TIME, event);
            intent.putExtra(TIMER_NAME, timerName);
            intent.putExtra(RESUMED, true);

            startActivityForResult(intent, timerRequest);
        }
    }

    /**
     * https://developer.android.com/reference/android/app/Activity.html#onActivityResult(int, int, android.content.Intent)
     *
     * The data passed back from the finishing Activity returns the milliseconds
     * representing the Date/Time selected by the user. If the selected date is
     * an easter-egg eligible holiday, the holiday in question is stored. This
     * information is kept within instances of the TimerState class.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == TIMER_1 && resultCode == RESULT_OK){
            timer1.setEventTarget(data.getLongExtra(EVENT_TIME, 0));
            timer1.setHolidayBackground(data.getStringExtra(BACKGROUND));
            timer1.setTimerName(data.getStringExtra(TIMER_NAME));
            if(!timer1.getTimerName().equals(""))
                rb1.setText(timer1.getTimerName());

        } else if(requestCode == TIMER_2 && resultCode == RESULT_OK){
            timer2.setEventTarget(data.getLongExtra(EVENT_TIME, 0));
            timer2.setHolidayBackground(data.getStringExtra(BACKGROUND));
            timer2.setTimerName(data.getStringExtra(TIMER_NAME));
            if(!timer2.getTimerName().equals(""))
                rb2.setText(timer2.getTimerName());

        } else if(requestCode == TIMER_3 && resultCode == RESULT_OK){
            timer3.setEventTarget(data.getLongExtra(EVENT_TIME, 0));
            timer3.setHolidayBackground(data.getStringExtra(BACKGROUND));
            timer3.setTimerName(data.getStringExtra(TIMER_NAME));
            if(!timer3.getTimerName().equals(""))
                rb3.setText(timer3.getTimerName());

        } else {
            Toast.makeText(this, "Please select a future point in time.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("TIMER_NAME1", timer1.getTimerName());
        editor.putLong("EVENT_TARGET1", timer1.getEventTarget());
        editor.putString("BACKGROUND1", timer1.getHolidayBackground());
        editor.putString("TIMER_NAME2", timer2.getTimerName());
        editor.putLong("EVENT_TARGET2", timer2.getEventTarget());
        editor.putString("BACKGROUND2", timer2.getHolidayBackground());
        editor.putString("TIMER_NAME3", timer3.getTimerName());
        editor.putLong("EVENT_TARGET3", timer3.getEventTarget());
        editor.putString("BACKGROUND3", timer3.getHolidayBackground());

        editor.apply();
    }

}
