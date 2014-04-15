package edu.upenn.cis350.safetypenn;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
 
public class DashboardActivity extends FragmentActivity implements LocationListener{
	private GoogleMap googleMap;
	private LocationManager locationManager;
	UserFunctions userFunctions;
    Button btnLogout;
    Button btnTimer;
    Button btnTimerStart;
    TextView timerDisplay;
    private static String KEY_SUCCESS = "success";
    boolean timerStart = false;
    private final long interval = 1 * 1000;
    private final long startTime = 10 * 1000;
    private final DashboardActivity currentActivity = this;
    private PopupWindow popup;
    private String  userEmail;
    private String userId;
    private double latitude;
    private double longitude;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        /**
         * Dashboard Screen for the application
         * */       
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
       // user already logged in show databoard
            setContentView(R.layout.dashboard);
            setUpMapIfNeeded();
        	userFunctions = new UserFunctions();
    		DatabaseOperations db = new DatabaseOperations(getApplicationContext());
    		HashMap<String, String> userDetails = db.getUserDetails();
    		userEmail = userDetails.get("email");
    		userId = userDetails.get("uid");
    		/****
    		 * Using Dummy variables right now.
    		 * timerEnded needs to be passed longitude, latitude, and length of timer
    		 * 
    		 * 
    		 */
    		latitude = 0;
    		longitude = 0;
    		
            btnLogout = (Button) findViewById(R.id.btnLogout);
            btnTimer = (Button) findViewById(R.id.btnTimer);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                 
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    userFunctions.logoutUser(getApplicationContext());
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    // Closing dashboard screen
                    finish();
                }
            });
            
            btnTimer.setOnClickListener(new View.OnClickListener() {
                
                public void onClick(View arg0) {
                	showTimerPopup(DashboardActivity.this);
                }
            });
             
        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dashboard screen
            finish();
        }        
    }
    
    private void showTimerPopup(final Activity context) {
    	int popupWidth = 450;
    	int popupHeight = 500;
    	LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.timer_popup);
    	LayoutInflater layoutInflater = (LayoutInflater) context
    	     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View layout = layoutInflater.inflate(R.layout.timer, viewGroup);
    	
	   popup = new PopupWindow(context);
	   popup.setContentView(layout);
	   popup.setWidth(popupWidth);
	   popup.setHeight(popupHeight);
	   popup.setFocusable(true);
	   popup.setBackgroundDrawable(new BitmapDrawable());
	   popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
       //Show timer
       //To-do: Access database to get user preferences
       timerDisplay = (TextView) layout.findViewById(R.id.timer_display);
       timerDisplay.setText(String.valueOf(startTime / 1000));
       btnTimer = (Button) findViewById(R.id.btnTimer);
	   btnTimerStart = (Button) layout.findViewById(R.id.timer_button);
       btnTimerStart.setOnClickListener(new View.OnClickListener() {
           
           public void onClick(View arg0) {
        	   if(timerStart == false) {
        		   //User just started timer
        		   timerStart = true;
        		   Timer timer = new Timer(startTime, interval, timerDisplay, currentActivity);
        		   timer.start();
        		   btnTimerStart.setBackgroundColor(getResources().getColor(R.color.end_red));
        		   btnTimerStart.setText("Stop");
        	   }
        	   //User stopped timer, close popup
        	   else {
        		   timerStart = false;
        		   popup.dismiss();
        	   }
           		
           }
       });
    }
    //After timer goes off, this method is called
    public void handleTimerCritical() {
    	System.out.println("Handle timer critical");

		JSONObject json = userFunctions.timerEnded(userEmail, userId, latitude, longitude);
	
		try {
            //Timer information successfully sent to server 
			if (json.getString(KEY_SUCCESS) != null) {
				Toast.makeText(this, "Campus police are being notified.", Toast.LENGTH_SHORT).show();
				timerDisplay.setText("!!!");
				timerStart = true;
				btnTimerStart.setText("Close");
            	
            }
			else{
                // Error 
                timerDisplay.setText("Error occured.");
            }
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
    	
    }
    
 // Set up map when needed
 	private void setUpMapIfNeeded() {
 		// Check if Google map is initiated
 		if (googleMap == null) {
 			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
 			// Make another check for map initiation
 			if (googleMap != null) {
 				// initialize map at penn
 				double pennLat = 39.9539;
 				double pennLong = -75.1930;
 				LatLng pennLatLng = new LatLng(pennLat, pennLong);

 				// Show current location in map, zoom appropriately, and place marker
 				googleMap.moveCamera(CameraUpdateFactory.newLatLng(pennLatLng));
 				googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
 				
 				
 				setUpMap();
 			}
 		}
 	}

 	private void setUpMap() {
 		System.out.println("Test to see how soon this happens");
 		
 		
 		// Enable MyLocation Layer
 		googleMap.setMyLocationEnabled(true);

 		// Get LocationManager
 		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 		
 	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1000, this);     

 	    // see if map has gps enabled
 	    if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null) {
 			Toast.makeText(this, "Not Connected to Current Location", Toast.LENGTH_SHORT).show();

 	    }
 	    
 		/*
 		// Create Criteria object to retrieve provider and find best provider
 		Criteria criteria = new Criteria();
 		String provider = locationManager.getBestProvider(criteria, true);
 /*
 		// Get current location
 		Location myLocation = locationManager.getLastKnownLocation(provider);

 		// Error handling for if app cannot find current location
 		if (myLocation == null) {
 			Toast.makeText(this, "Not Connected to Current Location", Toast.LENGTH_SHORT).show();
 		}*/
 		// find current location if myLocation is not null
 		//else {
 		/*
 			// Get latitude and longitutde of location and create a LatLng
 			double latitude = myLocation.getLatitude();
 			double longitude = myLocation.getLongitude();
 			LatLng latLng = new LatLng(latitude, longitude);

 			// Show current location in map, zoom appropriately, and place marker
 			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 			googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
 			googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Current Location"));
 		 */
 	}

 	//}

 	@Override
 	public void onLocationChanged(Location myLocation) {
 		System.out.println("Location changed happened");

 		if (myLocation == null) {
 			Toast.makeText(this, "Not Connected to Current Location", Toast.LENGTH_SHORT).show();
 		} else {

 			// Get latitude and longitutde of location and create a LatLng
 			double latitude = myLocation.getLatitude();
 			double longitude = myLocation.getLongitude();
 			LatLng latLng = new LatLng(latitude, longitude);

 			// Show current location in map, zoom appropriately, and place marker
 			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 			googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
 		//	googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Current Location"));

 		}
 	}

 	@Override
 	public void onProviderDisabled(String arg0) {
 		Toast.makeText(this, "Not Connected to Current Location", Toast.LENGTH_SHORT).show();
 	}

 	@Override
 	public void onProviderEnabled(String arg0) {
 	}

 	@Override
 	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
 		// TODO Auto-generated method stub

 	}
}