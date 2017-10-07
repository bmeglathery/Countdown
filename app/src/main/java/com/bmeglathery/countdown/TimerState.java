package com.bmeglathery.countdown;

/**
 * Helper class designed to store and retrieve pertinent information
 * related to the user's countdown timers.
 */

public class TimerState {

    private String timerName = "Countdown";
    private int secondsRemaining;
    private long eventTarget;
    private String holidayBackground = "";

    public TimerState(){
        secondsRemaining = 0;
    }

    public TimerState(int s){
        secondsRemaining = s;
    }

    public void setSecs(int s){
        secondsRemaining = s;
    }

    public int getSecs(){
        return secondsRemaining;
    }

    public void setEventTarget(long target){
        eventTarget = target;
    }

    public long getEventTarget(){
        return eventTarget;
    }

    public void setTimerName(String name){
        timerName = name;
    }

    public String getTimerName(){
        return timerName;
    }

    public void setHolidayBackground(String bgName){
        holidayBackground = bgName;
    }

    public String getHolidayBackground(){
        return holidayBackground;
    }

}
