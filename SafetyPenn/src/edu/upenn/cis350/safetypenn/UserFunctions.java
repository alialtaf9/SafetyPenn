package edu.upenn.cis350.safetypenn;

 
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
 


import android.content.Context;
import android.content.Intent;
 
public class UserFunctions {
     
     
    // constructor
    public UserFunctions(){

    }
    
    /**
     * function Timer Ended- Critical
     * @param email
     * @param lat
     * @param lon
     * @param length
     * */
    
    public JSONObject timerEnded(String email, double lat, double lon, int length) {
    	System.out.println("Creating thread");
    	String timerTag = NetworkAsync.timerCriticalTag;
    	TimerAsync thread = new TimerAsync(email, timerTag, lat, lon, length);
    	System.out.println("Thread created");
    	JSONObject json = null;
    	try {
			json = thread.execute().get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	return json;    	
    }
    
    
    /**
     * function Timer Started by User
     * @param email
     * @param lat
     * @param lon
     * @param length
     * */
    
    public JSONObject timerSet(String email, double lat, double lon, int length) {
    	System.out.println("Creating thread");
    	String timerTag = NetworkAsync.timerSetTag;
    	TimerAsync thread = new TimerAsync(email, timerTag, lat, lon, length);
    	System.out.println("Thread created");
    	JSONObject json = null;
    	try {
			json = thread.execute().get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	return json;    	
    }
    
    /**
     * function Timer Shut off by User
     * @param email
     * @param lat
     * @param lon
     * @param length
     * */
    
    public JSONObject timerShutOff(String email, double lat, double lon, int length) {
    	System.out.println("Creating thread");
    	String timerTag = NetworkAsync.timerTurnedOffTag;
    	TimerAsync thread = new TimerAsync(email, timerTag, lat, lon, length);
    	System.out.println("Thread created");
    	JSONObject json = null;
    	try {
			json = thread.execute().get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	return json;    	
    }
    
     
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
    	System.out.println("Creating thread");
    	LoginAsync thread = new LoginAsync(email, password);
    	System.out.println("Thread created");
    	JSONObject json = null;
    	try {
			json = thread.execute().get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	return json;
    }
     
    /**
     * function make Register Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
    	System.out.println("Beginning user registration");
    	RegisterAsync thread = new RegisterAsync(name, email, password);
    	System.out.println("Thread created");
    	JSONObject json = null;
    	try {
			json = thread.execute().get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	return json;
    }
     
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseOperations db = new DatabaseOperations(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
     
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseOperations db = new DatabaseOperations(context);
        db.resetTables();
        return true;
    }
    
    
     
}
