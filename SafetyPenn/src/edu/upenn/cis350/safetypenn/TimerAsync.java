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
	private String uid;
	//Location coordinates
	private String lat;
	private String lon;

	public TimerAsync(String email, String userId, String timerTag, double lat, double lon) {
		super(email);
		this.timerTag = timerTag;
		this.lat = Double.toString(lat);
		this.lon = Double.toString(lon);
		this.uid = userId;
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting background thread");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
    	System.out.println("Timer");
        params.add(new BasicNameValuePair("tag", timerTag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("lat", lat));
        params.add(new BasicNameValuePair("lon", lon));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
	}

}
