package com.example.safetypenn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class TimerAsync extends NetworkAsync {
	//Add location later

	public TimerAsync(String email) {
		super(email);
		
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting background thread");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
    	System.out.println("Timer");
        params.add(new BasicNameValuePair("tag", timerTag));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
	}

}
