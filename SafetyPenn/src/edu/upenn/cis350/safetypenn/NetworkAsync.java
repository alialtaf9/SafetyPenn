package edu.upenn.cis350.safetypenn;

import org.json.JSONObject;

import android.os.AsyncTask;

public abstract class NetworkAsync extends AsyncTask<String, Void, JSONObject> {
	protected JSONParser jsonParser;
	protected static String URL = "http://54.85.24.39/RequestHandler.php";
	protected String email;
	protected static String loginTag = "login";
	protected static String registerTag = "register";
	protected static String timerTurnedOffTag = "timerTurnedOff";
	protected static String timerSetTag = "timerSet";
	protected static String timerCriticalTag = "timerCritical";
	protected static String escortTag = "escort";
	protected static String updatePicTag = "updatePic";
	protected static String updatePasswordTag = "updatePassword";
	protected static String updateEmergencyContactTag = "updateEmergencyContact";
	
	public NetworkAsync(String email)  {
		this.email = email;
		jsonParser = new JSONParser();
	}
	
	abstract protected JSONObject doInBackground(String... args);

}
