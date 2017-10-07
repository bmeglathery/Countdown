package com.bmeglathery.countdown;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

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
    public static final int REQUEST_CODE = 1234;

    private TimerState timer1 = new TimerState();
    private TimerState timer2 = new TimerState();
    private TimerState timer3 = new TimerState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String msg = data.getStringExtra("resultMessage");
            //now do something with the message...
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates an explicit intent with extras encoding the event
     * date and the time specified by the user.
     */
    public void startCountDown(View view){
        DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        TimePicker tp = (TimePicker) findViewById(R.id.timePicker);

        Intent intent = new Intent(this, CountdownActivity.class);
        intent.putExtra(MONTH, dp.getMonth());
        intent.putExtra(DAY, dp.getDayOfMonth());
        intent.putExtra(YEAR, dp.getYear());
        intent.putExtra(HOUR, tp.getHour());
        intent.putExtra(MINUTE, tp.getMinute());


        /*
         * WARNING: This will cause the launched activity to
         * crash unless we have added an activity entry in the
         * manifest for CountdownActivity!
         */
        startActivityForResult(intent, REQUEST_CODE);
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
