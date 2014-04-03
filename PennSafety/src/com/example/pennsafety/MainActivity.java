package com.example.pennsafety;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {	
	private GoogleMap googleMap;

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
				setUpMap();
			}
		}
	}
	
	private void setUpMap() {
		// Enable MyLocation Layer
		googleMap.setMyLocationEnabled(true);
		
		// Get LocationManager
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		// Create Criteria object to retrieve provider and find best provider
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		
		// Get current location
		Location myLocation = locationManager.getLastKnownLocation(provider);
		
		// Set map type to normal
		//googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
		
		// Get latitude and longitutde of location and create a LatLng
		double latitude = myLocation.getLatitude();
		double longitude = myLocation.getLongitude();
		
		/*
		double latitude = 39.9539;
		double longitude = 75.1930;*/
		
		LatLng latLng = new LatLng(latitude, longitude);
		
		// Show current location in map, zoom appropriately, and place marker
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
		googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("EDIT THIS WITH ADDRESS"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
