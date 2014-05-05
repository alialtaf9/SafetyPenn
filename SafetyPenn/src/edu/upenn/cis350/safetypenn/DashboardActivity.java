package edu.upenn.cis350.safetypenn;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.view.KeyEvent;

public class DashboardActivity extends FragmentActivity implements LocationListener, OnMarkerDragListener{
	private GoogleMap googleMap;
	private LocationManager locationManager;
	Timer timer;
	UserFunctions userFunctions;
	Button btnLogout;
	Button btnEditSettings;
	Button btnRequestEscort;
	Button btnTimer;
	private static String KEY_SUCCESS = "success";
	boolean timerStart = false;
	private final DashboardActivity currentActivity = this;
	private String  userEmail;
	private double latitude;
	private double longitude;
	private int timerLength;
	static Dialog timerSetDialog;
	private EditText startAddrText;
	private EditText endAddrText;
	private Geocoder gc;
	private final OnClickListener setTimerOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			try {
				estTimerLL.setVisibility(View.GONE);
			} catch (NullPointerException e) {}
			setTimer();
		}    
	};
	private LinearLayout startAddrView;
	private LinearLayout endAddrView;
	private LinearLayout estTimerLL;
	private Marker myMarker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);       
		// Check login status in database
		userFunctions = new UserFunctions();
		timerLength = 1;
		if(userFunctions.isUserLoggedIn(getApplicationContext())){
			// user already logged in show databoard
			setContentView(R.layout.dashboard);
			setUpMapIfNeeded();
			userFunctions = new UserFunctions();
			DatabaseOperations db = new DatabaseOperations(getApplicationContext());
			HashMap<String, String> userDetails = db.getUserDetails();
			userEmail = userDetails.get("email");

			// Initialize global lat/long
			latitude = 39.9539;
			longitude = -75.1930;

			timerSetDialog = new Dialog(DashboardActivity.this);
			timerSetDialog.setContentView(R.layout.timersetdialog);
			Button setTimerButton = (Button) timerSetDialog.findViewById(R.id.setTimerButton);
			Button cancelButton = (Button) timerSetDialog.findViewById(R.id.cancelButton);
			final NumberPicker np = (NumberPicker) timerSetDialog.findViewById(R.id.numberPicker);
			np.setMaxValue(120);
			np.setMinValue(1);
			np.setWrapSelectorWheel(false);
			np.setOnValueChangedListener(new OnValueChangeListener()
			{
				@Override
				public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

					timerLength = newVal * 60;

				}
			});
			setTimerButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					btnTimer.setText("Start");
					btnTimer.setOnClickListener(setTimerOnClickListener);
					timerSetDialog.dismiss();
				}    
			});
			cancelButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {					
					timerSetDialog.dismiss();
				}    
			});

			btnEditSettings = (Button) findViewById(R.id.btnEditSettings);
			btnLogout = (Button) findViewById(R.id.btnLogout);
			btnTimer = (Button) findViewById(R.id.btnTimer);
			btnRequestEscort = (Button) findViewById(R.id.btnRequestEscort);
			startAddrView = (LinearLayout) findViewById(R.id.startAddress_field);
			endAddrView = (LinearLayout) findViewById(R.id.endAddress_field);

			btnEditSettings.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					Intent editSettings = new Intent(getApplicationContext(), EditSettingsActivity.class);
					editSettings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(editSettings);
				}
			});

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
					// hide address fields
					startAddrView.setVisibility(View.GONE);
					endAddrView.setVisibility(View.GONE);
					
					// hide pin
					if (myMarker != null) {
						myMarker.remove();
					}
					
					registerForContextMenu(btnTimer);
					openContextMenu(btnTimer);
				}
			});

			btnRequestEscort.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					handleEscortRequest();
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

	public void handleEscortRequest() {
		System.out.println("Handle escort request.");
		JSONObject json = userFunctions.escortRequest(userEmail, latitude, longitude);

		try {
			//Timer information successfully sent to server 
			if (json.getString(KEY_SUCCESS) != null) {
				Toast.makeText(this, "An escort has been requested.", Toast.LENGTH_SHORT).show();
			}
			else{
				// Error 
				//Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
				Toast.makeText(this, "An escort has been requested.", Toast.LENGTH_SHORT).show();
			}
		}
		catch(Exception e) {
			Toast.makeText(this, "An escort has been requested.", Toast.LENGTH_SHORT).show();
			//Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
		}

	}

	/******************************************
	 * 			TIMER HANDLING
	 ******************************************/

	public void setTimer() {
		if(timerStart == false) {
			//User just started timer
			timerStart = true;
			timer = new Timer(timerLength * 1000, 1000, btnTimer, currentActivity);
			timer.start();
			userFunctions.timerEnded(userEmail, latitude, longitude);
			btnTimer.setOnClickListener(setTimerOnClickListener);
		}
		//User stopped timer, close popup
		else {
			timerStart = false;
			timer.cancel();
			btnTimer.setText("Timer");
			btnTimer.setOnClickListener(new View.OnClickListener() {

				public void onClick(View arg0) {
					//showTimerPopup(DashboardActivity.this);
					registerForContextMenu(btnTimer);
					openContextMenu(btnTimer);
				}
			});
		}

	}

	//After timer goes off, this method is called
	public void handleTimerCritical() {
		System.out.println("Handle timer critical");

		btnTimer.setText("Timer");
		timerStart = false;

		btnTimer.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				//showTimerPopup(DashboardActivity.this);
				registerForContextMenu(btnTimer);
				openContextMenu(btnTimer);
			}
		});

		JSONObject json = userFunctions.timerEnded(userEmail, latitude, longitude);

		try {
			//Timer information successfully sent to server 
			if (json.getString(KEY_SUCCESS) != null) {
				Toast.makeText(this, "Campus police are being notified.", Toast.LENGTH_SHORT).show();
			}
			else{
				// Error 
				//Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
				Toast.makeText(this, "Campus police are being notified.", Toast.LENGTH_SHORT).show();
			}
		}
		catch(Exception e) {
			Toast.makeText(this, "Campus police are being notified.", Toast.LENGTH_SHORT).show();
			//Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
		}

	}

	// Set timer based on calculated time to destination
	private void showDestinationPicker(final Activity context) {	
		// show address fields
		startAddrView.setVisibility(View.VISIBLE);
		endAddrView.setVisibility(View.VISIBLE);

		if (locationManager == null) {
			Toast.makeText(this, "Could not find current location! Please enter the address of your location.", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Move pin to set start location, or input address manually.", Toast.LENGTH_LONG).show();

			double pinLatitude = Double.MIN_VALUE;
			double pinLongitude = Double.MIN_VALUE;

			try {
				// drop draggable pin on user's current location
				pinLatitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
				pinLongitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
			} catch (NullPointerException e) {
				Toast.makeText(this, "Could not find current location! Please turn on Location Services to use this app.", Toast.LENGTH_LONG).show();
			}

			LatLng pinLatLng = new LatLng(pinLatitude, pinLongitude);
			MarkerOptions marker = new MarkerOptions()
			.position(pinLatLng)
			.title("Current Location")
			.draggable(true);
			myMarker = googleMap.addMarker(marker);

			// zoom camera to pin location
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(pinLatLng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

			// create access for address fields
			startAddrText = (EditText) findViewById(R.id.startAddress_text);
			endAddrText = (EditText) findViewById(R.id.endAddress_text);


			// create Geocoder to find address info of current location
			gc = new Geocoder(context, Locale.getDefault());
			List<Address> addresses = null;
			try {
				addresses = gc.getFromLocation(pinLatitude, pinLongitude, 1);
			} catch (IOException e) {
				Toast.makeText(this, "Could not find current location! Please enter the address of your location.", Toast.LENGTH_LONG).show();
			}
			if (addresses != null && !addresses.isEmpty()) {
				// Find address info and set the start address text view
				String address = addresses.get(0).getAddressLine(0);
				String city = addresses.get(0).getAddressLine(1);
				startAddrText.setText(address + ", " + city);
			}

			// calculate time needed to get to destination
			endAddrText.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						setupEstimateTimer(context);

					}	
					return false;
				}
			});
		}

	}

	private void setupEstimateTimer(final Activity context) {
		// create Address classes from user input
		List<Address> startAddr = null;
		List<Address> endAddr = null;
		try {
			// add "19104" to address info
			String start = startAddrText.getText().toString() + ", 19104";
			String destination = endAddrText.getText().toString() + ", 19104";

			startAddr = gc.getFromLocationName(start, 1);
			endAddr = gc.getFromLocationName(destination, 1);
		} catch (IOException e) {
			Toast.makeText(this, "Could not geocode your set location. Please manually input the addresses.", Toast.LENGTH_SHORT).show();
		}

		// convert addresses into lat/long
		boolean startAddrValid = true;
		boolean endAddrValid = true;
		double startLat = 0;
		double startLong = 0;
		double endLat = 0;
		double endLong = 0;
		// find coordinates for start address
		try {
			if (startAddr != null && !startAddr.isEmpty()) {					
				startLat = startAddr.get(0).getLatitude();
				startLong = startAddr.get(0).getLongitude();
			} else {
				Toast.makeText(context, "Start address not valid! Please re-enter and make sure you're connected to Location Services.", Toast.LENGTH_LONG).show();			
				return;
			}
		} catch (IllegalStateException e) {
			startAddrValid = false;
			Toast.makeText(context, "Start address not valid! Please re-enter and make sure you're connected to Location Services.", Toast.LENGTH_LONG).show();
		}

		// find coordinates for end address
		try {
			if (endAddr != null && !endAddr.isEmpty()) {
				System.out.println("END ADDR ZIP: " + endAddr.get(0).getPostalCode());

				endLat = endAddr.get(0).getLatitude();
				endLong = endAddr.get(0).getLongitude();
			} else {
				Toast.makeText(context, "End address not valid! Please re-enter and make sure you're connected to Location Services.", Toast.LENGTH_LONG).show();				
				return;
			}
		} catch (IllegalStateException e) {
			endAddrValid = false;
			Toast.makeText(context, "End address not valid! Please re-enter and make sure you're connected to Location Services.", Toast.LENGTH_LONG).show();
		}

		// set up timer with estimated time
		if (startAddrValid && endAddrValid) {
			// calculates distance in meters between two locations
			float[] results = new float[10];
			Location.distanceBetween(startLat, startLong, endLat, endLong, results);
			int timeEstimate = ((int) (results[0]/1.39)) + 1;

			// don't do anything if location is outside penn's campus
			if (timeEstimate > 45 * 60) {
				Toast.makeText(context, "Location is outside Penn's campus! Please re-enter addresses.", Toast.LENGTH_LONG).show();
			} else {
				// remove marker when ready
				myMarker.remove();
				
				btnTimer.setText("Start");
				btnTimer.setOnClickListener(setTimerOnClickListener);
				timerLength = timeEstimate;

				// hide address fields
				startAddrView.setVisibility(View.GONE);
				endAddrView.setVisibility(View.GONE);

				// create string to display in popup
				StringBuilder popupMessage = new StringBuilder("Estimated time: ");
				if (timeEstimate > 60) {
					int minutes = timeEstimate / 60;
					int seconds = timeEstimate % 60;
					if (seconds == 0) {
						popupMessage.append(minutes + " min");
					} else {
						popupMessage.append(minutes + " minutes, " + seconds + " seconds");
					}
				} else {
					popupMessage.append(timeEstimate + " seconds");
				}

				// add timer estimate to textview and make it visible
				estTimerLL = (LinearLayout) findViewById(R.id.timerEst_popup);
				TextView popupTV = (TextView) findViewById(R.id.timerEst_text);
				popupTV.setText(popupMessage);
				estTimerLL.setVisibility(View.VISIBLE);
			}
		}
	}

	/******************************************
	 * 			MAP HANDLING
	 ******************************************/

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

				// Set marker-drag listener
				googleMap.setOnMarkerDragListener(this);

				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// Enable MyLocation Layer
		googleMap.setMyLocationEnabled(true);

		// Get LocationManager
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1000, this);     

		// checking for enabled gps
		if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null) {
			Toast.makeText(this, "Not Connected to Current Location", Toast.LENGTH_SHORT).show();
		} 
	}

	//}

	@Override
	public void onLocationChanged(Location myLocation) {
		if (myLocation == null) {
			Toast.makeText(this, "Not Connected to Current Location", Toast.LENGTH_SHORT).show();
		} else {	

			// Get latitude and longitutde of location and create a LatLng
			latitude = myLocation.getLatitude();
			longitude = myLocation.getLongitude();
			LatLng latLng = new LatLng(latitude, longitude);

			googleMap.setMyLocationEnabled(true);

			// Show current location in map, zoom appropriately, and place marker
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
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


	/******************************************
	 * 			SELECTING WHICH TIMER
	 * 					TO USE
	 ******************************************/	

	// Floating menu for selecting timer
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.timer_select, menu);
		menu.setHeaderTitle("Select Timer");
	}

	// Delegate handling of each selection of menu
	@Override
	public boolean onContextItemSelected(MenuItem item) {		
		switch (item.getItemId()) {

		// Option 1: let Google Maps estimate time
		case R.id.estimate_timer:
			showDestinationPicker(DashboardActivity.this);
			return true;

			// Option 2: user sets custom timer
		case R.id.user_set_timer:
			// Display timer pop-up
			timerSetDialog.show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}


	/*****************************************************
	 * 			MAP MARKER DRAGLISTENER
	 ****************************************************/

	@Override
	public void onMarkerDrag(Marker arg0) {	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		double pinLatitude = arg0.getPosition().latitude;
		double pinLongitude = arg0.getPosition().longitude;

		List<Address> addresses = null;
		try {
			addresses = gc.getFromLocation(pinLatitude, pinLongitude, 1);
		} catch (IOException e) {
			Toast.makeText(this, "Could not find current location! Please enter the address of your location.", Toast.LENGTH_LONG).show();
		}
		if (addresses != null) {
			// Find address info and set the start address text view
			String address = addresses.get(0).getAddressLine(0);
			String city = addresses.get(0).getAddressLine(1);
			startAddrText.setText(address + ", " + city);
		}
	}

	@Override
	public void onMarkerDragStart(Marker arg0) { }
}


