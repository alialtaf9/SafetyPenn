package edu.upenn.cis350.safetypenn;

import java.util.ArrayList;
import java.util.List;

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
    Bitmap bitmap;
    Button selectImageButton;
    ImageView image;
     
    // JSON Response node names
    private final String KEY_SUCCESS = "success";
    private final String KEY_ERROR = "error";
    private final String KEY_ERROR_MSG = "error_msg";
    private final String KEY_UID = "uid";
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
        btnRegister = (Button) findViewById(R.id.btnRegister);
        registerLink = (TextView) findViewById(R.id.login_link);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);
        registerSuccessMsg = (TextView) findViewById(R.id.register_success);
        selectImageButton = (Button) findViewById(R.id.selectImageButton);
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
                UserFunctions userFunction = new UserFunctions();
                System.out.println("User clicked register");
                JSONObject json = userFunction.registerUser(name, email, password);
                 
                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        registerSuccessMsg.setText("Register Success!");
                        String res = json.getString(KEY_SUCCESS); 
                        if(Integer.parseInt(res) == 1){
                            // user successfully registred
                            // Store user details in SQLite Database
                            DatabaseOperations db = new DatabaseOperations(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");
                             
                            // Clear all previous data in database
                            userFunction.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID));                        
                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);
                            // Close Registration Screen
                            finish();
                        }else{
                            // Error in registration
                            registerErrorMsg.setText("Error occured in registration");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
      String[] filePathColumn = { MediaStore.Images.Media.DATA };

      Cursor cursor = getContentResolver().query(selectedImage,
        filePathColumn, null, null, null);
      cursor.moveToFirst();

      int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
      String picturePath = cursor.getString(columnIndex);
      cursor.close();

      decodeFile(picturePath);
     }
     
    }
    
    public void decodeFile(String filePath) {
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
    	 }
}
