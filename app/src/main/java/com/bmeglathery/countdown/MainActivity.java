package com.bmeglathery.countdown;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.Timer;

/**
 * Provides event handler for button in the user interface, which
 * creates an intent with extras from date and time pickers. The
 * intent is used in launching a new activity to display a countdown.
 */
public class MainActivity extends AppCompatActivity {

    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String YEAR = "year";
    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";

    //For receiving info from CountdownActivity
    public static final int TIMER_1 = 1;
    public static final int TIMER_2 = 2;
    public static final int TIMER_3 = 3;

    private TimerState timer1 = new TimerState();
    private TimerState timer2 = new TimerState();
    private TimerState timer3 = new TimerState();

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

        rb1.setText(timer1.getTimerName());
        rb2.setText(timer2.getTimerName());
        rb3.setText(timer3.getTimerName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            timer1.setEventTarget(data.getLongExtra("Event_Target", 0));
            timer1.setHolidayBackground(data.getStringExtra("Background"));
            timer1.setTimerName(data.getStringExtra("TimerName"));
            if(!timer1.getTimerName().equals(""))
                rb1.setText(timer1.getTimerName());

        } else if(requestCode == TIMER_2 && resultCode == RESULT_OK){
            timer2.setEventTarget(data.getLongExtra("Event_Target", 0));
            timer2.setHolidayBackground(data.getStringExtra("Background"));
            timer2.setTimerName(data.getStringExtra("TimerName"));
            if(!timer2.getTimerName().equals(""))
                rb2.setText(timer2.getTimerName());

        } else if(requestCode == TIMER_3 && resultCode == RESULT_OK){
            timer3.setEventTarget(data.getLongExtra("Event_Target", 0));
            timer3.setHolidayBackground(data.getStringExtra("Background"));
            timer3.setTimerName(data.getStringExtra("TimerName"));
            if(!timer3.getTimerName().equals(""))
                rb3.setText(timer3.getTimerName());

        } else {
            Toast.makeText(this, "Please select a future point in time.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates an explicit intent with extras encoding the event
     * date and the time specified by the user.
     *
     * Known bug: Only works for dates/times closer than two and a half
     * months past current day...
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
        intent.putExtra("Resumed", false);

        int timer_request = 0;
        int selectedTimer = rg.getCheckedRadioButtonId();

        if(selectedTimer == R.id.rb1)
            timer_request = TIMER_1;
        else if (selectedTimer == R.id.rb2)
            timer_request = TIMER_2;
        else if (selectedTimer == R.id.rb3)
            timer_request = TIMER_3;
        else
            Log.d("Countdown", "selected radio button did not match radioButton id");

        /*
         * WARNING: This will cause the launched activity to
         * crash unless we have added an activity entry in the
         * manifest for CountdownActivity!
         */
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
        int timerRequest = 0;

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
            Log.d("Countdown", "An error occurred in resumeCountDown");

        long event = timer.getEventTarget();
        GregorianCalendar now = new GregorianCalendar();
        long currentSeconds = now.getTimeInMillis() / 1000;

        if(event / 1000 - currentSeconds < 0)
            Toast.makeText(this, "Your countdown ended while you were away...", Toast.LENGTH_SHORT).show();
        else if(event == 0)
            Toast.makeText(this, "You cannot resume a timer which has not been started!", Toast.LENGTH_SHORT).show();
        else {
            String background = timer.getHolidayBackground();
            String timerName = timer.getTimerName();

            Intent intent = new Intent(this, CountdownActivity.class);
            intent.putExtra("Background", background);
            intent.putExtra("EventTime", event);
            intent.putExtra("TimerName", timerName);
            intent.putExtra("Resumed", true);

            startActivityForResult(intent, timerRequest);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
