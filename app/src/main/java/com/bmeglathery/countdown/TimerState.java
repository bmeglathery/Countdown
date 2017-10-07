package com.bmeglathery.countdown;

/**
 * Helper class designed to store and retrieve pertinent information
 * related to the user's countdown timers.
 */

public class TimerState {

    private String timerName = "Countdown";
    private long milliseconds_Remaining;

    public TimerState(){
        milliseconds_Remaining = 0;
    }

    public TimerState(long ms){
        milliseconds_Remaining = ms;
    }

    private void setMillis(long ms){
        milliseconds_Remaining = ms;
    }

    public long getMillis(){
        return milliseconds_Remaining;
    }

    private void setTimerName(String name){
        timerName = name;
    }

    public String getTimerName(){
        return timerName;
    }

}
