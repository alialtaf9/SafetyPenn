package edu.upenn.cis350.safetypenn;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
 
public class EditSettingsActivity extends Activity {
	
	Button btnReturnToMainScreen;
	
	EditText inputPassword;
	Button updatePasswordButton;
	
	EditText inputEmergencyContact;
	Button updateEmergencyContactButton;
	
	Button selectImageButton;
    ImageView image;
    Button updateImageButton;
     
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
        
        btnReturnToMainScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				finish();
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
      image.setImageURI(selectedImage);
      selectImageButton.setText("Change Profile Photo");
     }
     
    }
}
