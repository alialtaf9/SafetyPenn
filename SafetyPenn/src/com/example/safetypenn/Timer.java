package com.example.safetypenn;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer extends CountDownTimer{
	private TextView display;

	public Timer(long startTime, long interval, TextView display) {
		super(startTime, interval);
		this.display = display;
		
	}

	@Override
	public void onFinish() {
		display.setText("Done");
		
	}

	@Override
	public void onTick(long millisUntilFinished) {
		display.setText("" + millisUntilFinished / 1000);
		
	}
	
}
