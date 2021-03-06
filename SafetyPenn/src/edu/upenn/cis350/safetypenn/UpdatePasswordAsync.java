package edu.upenn.cis350.safetypenn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class UpdatePasswordAsync extends NetworkAsync{
	private String password;

	public UpdatePasswordAsync(String email, String password) {
		super(email);
		this.password = password;
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting background thread");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
    	System.out.println("Logging in");
        params.add(new BasicNameValuePair("tag", NetworkAsync.updatePasswordTag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
	}

}
