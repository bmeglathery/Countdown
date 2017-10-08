package com.bmeglathery.countdown;

/**
 * Helper class designed to store and retrieve pertinent information
 * related to the user's countdown timers.
 */

public class TimerState {

    private String timerName;
    private long eventTarget;
    private String holidayBackground;

    public TimerState(){
        timerName = "Countdown";
        eventTarget = 0;
        holidayBackground = "";
    }

    public TimerState(String name, long target, String bg){
        timerName = name;
        eventTarget = target;
        holidayBackground = bg;
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
