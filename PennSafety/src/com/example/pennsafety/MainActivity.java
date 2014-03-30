package com.example.pennsafety;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener {

	// Global constants
	/*
	 * Define a request code to send to Google Play services
	 * This code is returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private LocationManager locationManager;
	private LocationClient locationClient;
	private Location currentLocation;
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// set up map
		map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		setUpMap();
/*
		// set up location client
		locationClient = new LocationClient(this, this, this);

		// get current location
		currentLocation = locationClient.getLastLocation();
		onStart(); */
	}
/*
	// called when activity becomes visible
	protected void onStart() {
		super.onStart();
		// connect client
		locationClient.connect();
	}

	// called when activity disappears
	protected void onStop() {
		locationClient.disconnect();
		super.onStop();
	}
*/
	protected void setUpMap() {
		// set map type to normal view mode
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		/*
        map.setMyLocationEnabled(true);

        // get handler to system-level location services
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // create a Criteria object to retrieve best provider
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        // obtain current location
        Location currentLocation = locationManager.getLastKnownLocation(provider);
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        // update map screen to current location
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraUpdateFactory.zoomTo(20);*/
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	 /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

    }
    
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}


	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;
		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}
		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}
		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	

	/*
	 * Handle results returned to the FragmentActivity
	 * by Google Play services
	 */
/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST :
			
		 // If the result code is Activity.RESULT_OK, try to connect again
		  *
			switch (resultCode) {
			case Activity.RESULT_OK :
				

				//try the request again

				break;
			}
		}
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode =
				GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates",
					"Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error code
			int errorCode = ConnectionResult.getErrorCode();
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					errorCode,
					this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment =
						new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getSupportFragmentManager(),
						"Location Updates");
			}
		}
	}
*/
}
