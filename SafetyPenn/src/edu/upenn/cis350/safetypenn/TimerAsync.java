package edu.upenn.cis350.safetypenn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* Takes care of sending timer notification to database */
public class TimerAsync extends NetworkAsync {
    //References the type of timer alert
	private String timerTag;
	//Location coordinates
	private String lat;
	private String lon;
	//User's timer set length
	private String length;

	public TimerAsync(String email, String timerTag, double lat, double lon, int length) {
		super(email);
		this.timerTag = timerTag;
		this.lat = Double.toString(lat);
		this.lon = Double.toString(lon);
		this.length = Integer.toString(length);
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting background thread");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
    	System.out.println("Timer");
        params.add(new BasicNameValuePair("tag", timerTag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("lat", lat));
        params.add(new BasicNameValuePair("lon", lon));
        params.add(new BasicNameValuePair("length", length));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
	}

}
