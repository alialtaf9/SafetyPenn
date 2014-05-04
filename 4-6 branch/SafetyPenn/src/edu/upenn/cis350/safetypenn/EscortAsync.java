package edu.upenn.cis350.safetypenn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class EscortAsync extends NetworkAsync{
	//Location coordinates
	private String lat;
	private String lon;

	public EscortAsync(String email, double lat, double lon)  {
		super(email);
		this.lat = Double.toString(lat);
		this.lon = Double.toString(lon);
	}
	
	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting background thread");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
    	System.out.println("Escort");
        params.add(new BasicNameValuePair("tag", escortTag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("lat", lat));
        params.add(new BasicNameValuePair("lon", lon));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
	}

}
