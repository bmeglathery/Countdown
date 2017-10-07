package com.bmeglathery.countdown;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        View viewToLoad = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        this.setContentView(viewToLoad);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

    /**
     * Illustrating IMPLICIT intent
     */
    public void websiteClickHandler(View view){
        // Use URI class in Android.net, not Java API
        Uri webpage = Uri.parse("http://www.kotaku.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        startActivity(intent);
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

    /*
    public void resetPickers(View view) {
        Toast.makeText(this, "Resetting Pickers...", Toast.LENGTH_SHORT).show();
        DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
        TimePicker tp = (TimePicker) findViewById(R.id.timePicker);
        Calendar now = new GregorianCalendar();

        int y = now.get(Calendar.YEAR);
        int m = now.get(Calendar.MONTH);
        int d = now.get(Calendar.DAY_OF_MONTH);
        dp.updateDate(y, m, d);

        // To save my life, I cannot figure out why this does not work. HELP ME!
        int hr = now.get(Calendar.HOUR);
        Log.d("RESET_PICKERS", "hr: " + hr);
        int min = now.get(Calendar.MINUTE);
        Log.d("RESET_PICKERS", "min: " + min);

        tp.setHour(hr);
        tp.setMinute(min);
    }
    */
}
