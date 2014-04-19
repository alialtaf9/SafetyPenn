package edu.upenn.cis350.safetypenn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class LoginAsync extends NetworkAsync {
	private String password;
    private static String URL = "http://54.85.175.45/RequestHandler.php";

	//Login
	public LoginAsync(String email, String password) {
		super(email);
		this.password = password;
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting background thread");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
    	System.out.println("Logging in");
        params.add(new BasicNameValuePair("tag", loginTag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
	}
	
    protected void onPostExecute(JSONObject json) {
    	        
    }

}
