package edu.upenn.cis350.safetypenn;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
 
public class RegisterActivity extends Activity {
    Button btnRegister;
    TextView registerLink;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputPassword;
    TextView registerErrorMsg;
    TextView registerSuccessMsg;
    Spinner genderSpinner;
    Spinner hairColorSpinner;
    Spinner eyeColorSpinner;
    EditText inputPhoneNumber;
    EditText inputEmergencyContact;
    EditText inputHeight;
    EditText inputWeight;
    Bitmap bitmap;
    Button selectImageButton;
    ImageView image;
    String imagePath;
     
    // JSON Response node names
    private final String KEY_SUCCESS = "success";
    private final String KEY_ERROR = "error";
    private final String KEY_ERROR_MSG = "error_msg";
    private final String KEY_NAME = "name";
    private final String KEY_EMAIL = "email";
    private final String KEY_CREATED_AT = "created_at";

 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        System.out.println("Created register view");
        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        inputPhoneNumber = (EditText) findViewById(R.id.phonenumber);
        inputEmergencyContact = (EditText) findViewById(R.id.emergencycontact);
        inputHeight = (EditText) findViewById(R.id.height);
        inputWeight = (EditText) findViewById(R.id.weight);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        registerLink = (TextView) findViewById(R.id.login_link);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
        registerSuccessMsg = (TextView) findViewById(R.id.register_success);
        selectImageButton = (Button) findViewById(R.id.selectImageButton);
        image = (ImageView) findViewById(R.id.uploadImage);
        addItemsOnSpinners();
        // Register Image Upload event
        selectImageButton.setOnClickListener(new View.OnClickListener() {
        
         @Override
         public void onClick(View v) {
          selectImageFromGallery();

         }
        });
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {         
            public void onClick(View view) {
            	//Todo: Null handler
                String name = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                String phoneNumber = inputPhoneNumber.getText().toString();
                String emergencyContact = inputEmergencyContact.getText().toString();
                String height = inputHeight.getText().toString();
                String weight = inputWeight.getText().toString();
                String gender = genderSpinner.getSelectedItem().toString();
                String hair_color = hairColorSpinner.getSelectedItem().toString();
                String eye_color = eyeColorSpinner.getSelectedItem().toString();
                //Check fields aren't blank
                if(name.trim().length() ==0 ||email.trim().length()==0 || password.trim().length() ==0) {
                	registerErrorMsg.setText("Please fill out all fields.");
                	return;                	
                }
                //Validate email
                if(!validateEmail(email)) {
                	registerErrorMsg.setText("Please provide a valid email.");
                	return;
                }
                //Todo: Check spinners
                UserFunctions userFunction = new UserFunctions();
                System.out.println("User clicked register");
                JSONObject json = userFunction.registerUser(name, email, password, bitmap, imagePath, phoneNumber, emergencyContact, height, weight, gender, hair_color, eye_color);
                 
                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {   
                        String res = json.getString(KEY_SUCCESS); 
                        if(Integer.parseInt(res) == 1){
                            // user successfully registered
                        	registerSuccessMsg.setText("Register Success!");
                            // Store user details in SQLite Database
                            DatabaseOperations db = new DatabaseOperations(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");
                             
                            // Clear all previous data in database
                            userFunction.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL));                        
                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);
                            // Close Registration Screen
                            finish();
                        }else{
                            // Error in registration
                        	String err = json.getString(KEY_ERROR); 
                            if(Integer.parseInt(err) == 2)
                            	registerErrorMsg.setText("User already exists");
                            registerErrorMsg.setText("Error occured in registration");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e) {
                	registerErrorMsg.setText("Error occured in registration");
                }
            }
        });
 
        // Link to Login Screen
        registerLink.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
    }
    
    public void addItemsOnSpinners() {
    	genderSpinner = (Spinner) findViewById(R.id.genderspinner);
    	List<String> list = new ArrayList<String>();
    	list.add("Select gender");
    	list.add("Male");
    	list.add("Female");
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, list);
    		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		genderSpinner.setAdapter(dataAdapter);
    	eyeColorSpinner = (Spinner) findViewById(R.id.eyecolorspinner);
        List<String> eyeColorList = new ArrayList<String>();
        eyeColorList.add("Select Eye Color");
        eyeColorList.add("Brown");
        eyeColorList.add("Blue");
        eyeColorList.add("Green");
        ArrayAdapter<String> eyeColorDataAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, eyeColorList);
        eyeColorDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eyeColorSpinner.setAdapter(eyeColorDataAdapter);
        
        hairColorSpinner = (Spinner) findViewById(R.id.haircolorspinner);
        List<String> hairColorList = new ArrayList<String>();
        hairColorList.add("Select Hair Color");
        hairColorList.add("Brown");
        hairColorList.add("Blonde");
        hairColorList.add("Red");
        ArrayAdapter<String> hairColorDataAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, hairColorList);
        hairColorDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hairColorSpinner.setAdapter(hairColorDataAdapter);
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
         Uri selectedImageUri = data.getData();
         imagePath = getPath(selectedImageUri);
         image.setImageURI(selectedImageUri);
         selectImageButton.setText("Change Image");
         if(imagePath == null) {
        	 registerErrorMsg.setText("Image File Path Not Found.");
         }
         else {
        	 bitmap=BitmapFactory.decodeFile(imagePath);
         }
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

    
/*    public void decodeFile(String filePath) {
    	  // Decode image size
    	  BitmapFactory.Options o = new BitmapFactory.Options();
    	  o.inJustDecodeBounds = true;
    	  BitmapFactory.decodeFile(filePath, o);
    	  final int REQUIRED_SIZE = 1024;
    	  int width_tmp = o.outWidth, height_tmp = o.outHeight;
    	  int scale = 1;
    	  while (true) {
    	   if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
    	    break;
    	   width_tmp /= 2;
    	   height_tmp /= 2;
    	   scale *= 2;
    	  }
    	  BitmapFactory.Options o2 = new BitmapFactory.Options();
    	  o2.inSampleSize = scale;
    	  bitmap = BitmapFactory.decodeFile(filePath, o2);

    	  image.setImageBitmap(bitmap);
    	 }*/
    
    public boolean validateEmail(String email) {
    	String regex= "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}";
    	Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email.toUpperCase());
        if (matcher.matches()) {
        	return true;
        }
        return false;
    }
}
