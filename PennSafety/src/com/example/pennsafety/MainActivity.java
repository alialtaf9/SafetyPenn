package com.example.pennsafety;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements LocationListener {	
	private GoogleMap googleMap;
	private LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

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
