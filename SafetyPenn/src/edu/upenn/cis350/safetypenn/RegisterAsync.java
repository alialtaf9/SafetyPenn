package edu.upenn.cis350.safetypenn;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class RegisterAsync extends NetworkAsync {
	private String name;
	private String password;
	private Bitmap bitmap;
	private String filePath;
	private String phoneNumber;
	private String emergencyContact;
	private String height;
	private String weight;
	private String gender;
	private String hair_color;
	private String eye_color;
	
	//Register
	public RegisterAsync(String name, String email, String password, Bitmap bitmap, String filePath, String phoneNumber, String emergencyContact, String height, String weight, String gender, String hair_color, String eye_color) {
		super(email);
		this.password = password;
		this.name = name;
		this.bitmap = bitmap;
		this.filePath = filePath;
		this.phoneNumber = phoneNumber;
		this.emergencyContact = emergencyContact;
		this.height = height;
		this.weight = weight;
		this.gender = gender;
		this.hair_color = hair_color;
		this.eye_color = eye_color;
	}
	@Override
	protected JSONObject doInBackground(String... args) {
		System.out.println("Starting background thread");
		JSONObject json = jsonParser.getJSONMultiPart(URL, registerTag, bitmap, name, email, password, filePath, phoneNumber, emergencyContact, height, weight, gender,hair_color,eye_color);
/*	    List<NameValuePair> params = new ArrayList<NameValuePair>();
		System.out.println("Registering");
	    params.add(new BasicNameValuePair("tag", registerTag));
	    params.add(new BasicNameValuePair("name", name));
	    params.add(new BasicNameValuePair("email", email));
	    params.add(new BasicNameValuePair("password", password)); 
	    JSONObject json = jsonParser.getJSONFromUrl(URL, params); */
	    return json;
	}

}
