package edu.upenn.cis350.safetypenn;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
 
public class EditSettingsActivity extends Activity {
	
	Button btnReturnToMainScreen;
	
	EditText inputPassword;
	Button updatePasswordButton;
	
	EditText inputEmergencyContact;
	Button updateEmergencyContactButton;
	
	Button selectImageButton;
    ImageView image;
    Button updateImageButton;
    
    String userEmail;
    UserFunctions userFunction;
    String filePath = null;
    // JSON Response node names
    private final String KEY_SUCCESS = "success";
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editsettings);
        
        btnReturnToMainScreen = (Button) findViewById(R.id.returnToMainScreenButton);
        
        inputPassword = (EditText) findViewById(R.id.editPassword);
        updatePasswordButton = (Button) findViewById(R.id.updatePasswordButton);
        
        inputEmergencyContact = (EditText) findViewById(R.id.emergencycontact);
        updateEmergencyContactButton = (Button) findViewById(R.id.updateEmergencyContactButton);
        
        selectImageButton = (Button) findViewById(R.id.selectImageButton);
        image = (ImageView) findViewById(R.id.uploadImage);
        updateImageButton = (Button) findViewById(R.id.updateImageButton);
        
		//Get user's email
		DatabaseOperations db = new DatabaseOperations(getApplicationContext());
		HashMap<String, String> userDetails = db.getUserDetails();
		userEmail = userDetails.get("email");
		userFunction = new UserFunctions();
        
        btnReturnToMainScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				finish();
			}
		});
        
        updatePasswordButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				String password = inputPassword.getText().toString();
				if(password.trim().length() ==0) {
					/*
					Please provide valid password message
					*/
				}
				else {
		            System.out.println("User clicked change password");
		            JSONObject json = userFunction.updatePassword(userEmail, password);
		    		try {
		                //Timer information successfully sent to server 
		    			if (json.getString(KEY_SUCCESS) != null) {
	                        String res = json.getString(KEY_SUCCESS); 
	                        if(Integer.parseInt(res) == 1){
							/*
							Success message
							*/
		                	
	                        }
	                        else{
							/*
							Error message
							*/
	                        }
		    			}
		    		}
		    		catch(Exception e) {
		    			e.printStackTrace();
		    		}
				}
			}
		});
        
        updateEmergencyContactButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				String number = inputEmergencyContact.getText().toString();
				if(number.trim().length() ==0) {
					/*
					Please provide valid password message
					*/
				}
				else {
		            System.out.println("User clicked change emergency contact");
		            JSONObject json = userFunction.updateEmergencyContact(userEmail, number);
		    		try {
		                //Timer information successfully sent to server 
		    			if (json.getString(KEY_SUCCESS) != null) {
	                        String res = json.getString(KEY_SUCCESS); 
	                        if(Integer.parseInt(res) == 1){
							/*
							Success message
							*/
		                	
	                        }
	                        else{
							/*
							Error message
							*/
	                        }
		    			}
		    		}
		    		catch(Exception e) {
		    			e.printStackTrace();
		    		}
				}
			}
		});
        
        updateImageButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				if(filePath == null) {
					/*
					Image path error
					*/
				}
				else {
		            System.out.println("User clicked change picture");
		            JSONObject json = userFunction.updatePicture(userEmail, filePath);
		    		try {
		                //Timer information successfully sent to server 
		    			if (json.getString(KEY_SUCCESS) != null) {
	                        String res = json.getString(KEY_SUCCESS); 
	                        if(Integer.parseInt(res) == 1){
							/*
							Success message
							*/
		                	
	                        }
	                        else{
							/*
							Error message
							*/
	                        }
		    			}
		    		}
		    		catch(Exception e) {
		    			e.printStackTrace();
		    		}
				}
			}
		});
        
        selectImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
             selectImageFromGallery();

            }
           });
    }
    
    public void selectImageFromGallery() {
      	 Intent intent = new Intent();
      	 intent.setType("image/*");
      	 intent.setAction(Intent.ACTION_GET_CONTENT);
      	 startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);

     if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
      Uri selectedImage = data.getData();
      filePath = getPath(selectedImage);
      image.setImageURI(selectedImage);
      selectImageButton.setText("Change Profile Photo");
     }
     
    }
    
    public String getPath(Uri uri) {
  	  Cursor cursor = null;
  	  try { 
  	       String[] proj = { MediaStore.Images.Media.DATA };
  	        cursor = getApplicationContext().getContentResolver().query(uri,  proj, null, null, null);
  	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
  	        cursor.moveToFirst();
  	        String path = cursor.getString(column_index);

  	        return path;
  	    } finally {
  	        if (cursor != null) {
  	            cursor.close();
  	        }
  	    }
  }
}
