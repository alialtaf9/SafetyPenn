package edu.upenn.cis350.safetypenn;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer extends CountDownTimer{
	private TextView display;
	private DashboardActivity activity;
	
	public Timer(long startTime, long interval, TextView display, DashboardActivity activity) {
		super(startTime, interval);
		this.display = display;
		this.activity = activity;
		
	}

	@Override
	public void onFinish() {
		//callback
		activity.handleTimerCritical();	
	}

	@Override
	public void onTick(long millisUntilFinished) {
		int seconds = (int)millisUntilFinished / 1000;
		int minutes = seconds / 60;
		seconds = seconds % 60;
		String middle = ":";
		if(seconds < 10) {
			middle = ":0";
		}
		display.setText(minutes + middle + seconds);
		
	}
	
}
