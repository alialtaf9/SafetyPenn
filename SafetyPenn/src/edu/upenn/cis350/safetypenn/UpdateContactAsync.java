package edu.upenn.cis350.safetypenn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class UpdateContactAsync extends NetworkAsync {
	private String number;
	
	public UpdateContactAsync(String email, String number) {
		super(email);
		this.number = number;
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting update contact background thread");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
    	System.out.println("Logging in");
        params.add(new BasicNameValuePair("tag", NetworkAsync.updateEmergencyContactTag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("number", number));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
	}

}
