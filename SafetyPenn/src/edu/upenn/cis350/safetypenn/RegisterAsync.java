package edu.upenn.cis350.safetypenn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class RegisterAsync extends NetworkAsync {
	private String name;
	private String password;
	
	//Register
	public RegisterAsync(String name, String email, String password) {
		super(email);
		this.password = password;
		this.name = name;
	}
	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting background thread");
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
		System.out.println("Registering");
	    params.add(new BasicNameValuePair("tag", registerTag));
	    params.add(new BasicNameValuePair("name", name));
	    params.add(new BasicNameValuePair("email", email));
	    params.add(new BasicNameValuePair("password", password));
	    JSONObject json = jsonParser.getJSONFromUrl(URL, params);
	    return json;
	}

}
