package edu.upenn.cis350.safetypenn;

 
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
 




import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
 
public class UserFunctions {
     
     
    // constructor
    public UserFunctions(){

    }
    
    /**
     * function Timer Ended- Critical
     * @param email
     * @param lat
     * @param lon
     * */
    
    public JSONObject timerEnded(String email, double lat, double lon) {
    	System.out.println("Creating thread");
    	String timerTag = NetworkAsync.timerCriticalTag;
    	TimerAsync thread = new TimerAsync(email, timerTag, lat, lon);
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
     * function Timer Ended- Critical
     * @param email
     * @param lat
     * @param lon
     * */
    
    public JSONObject escortRequest(String email, double lat, double lon) {
    	System.out.println("Creating thread");
    	NetworkAsync thread = new EscortAsync(email, lat, lon);
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
     * function update picture
     * @param email
     * @param filepath
     * */
    public JSONObject updatePicture(String email, String filePath){
    	System.out.println("Creating thread");
    	UpdatePicAsync thread = new UpdatePicAsync(email, filePath);
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
     * function update password
     * @param email
     * @param password
     * */
    public JSONObject updatePassword(String email, String password){
    	System.out.println("Creating thread");
    	UpdatePasswordAsync thread = new UpdatePasswordAsync(email, password);
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
     * function update emergency contact
     * @param email
     * @param number
     * */
    public JSONObject updateEmergencyContact(String email, String number){
    	System.out.println("Creating thread");
    	UpdateContactAsync thread = new UpdateContactAsync(email, number);
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
     * @param eye_color 
     * @param hair_color 
     * @param gender 
     * @param weight 
     * @param height 
     * @param emergencyContact 
     * @param phoneNumber 
     * */
    public JSONObject registerUser(String name, String email, String password, Bitmap bitmap, String filePath, String phoneNumber, String emergencyContact, String height, String weight, String gender, String hair_color, String eye_color){
    	System.out.println("Beginning user registration");
    	RegisterAsync thread = new RegisterAsync(name, email, password, bitmap, filePath, phoneNumber, emergencyContact, height, weight, gender, hair_color,eye_color);
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
