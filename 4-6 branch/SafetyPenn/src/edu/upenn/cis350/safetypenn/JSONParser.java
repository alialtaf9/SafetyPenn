package edu.upenn.cis350.safetypenn;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;
import android.util.Log;
 
public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    protected final String TAG = this.getClass().getName();
 
    // constructor
    public JSONParser() {
 
    }

    //MultiPart Entity for Pic Upload to Server
    //Will need to use this method instead of getJSONFromURL for registration
    //Sigh.
    public JSONObject getJSONMultiPart(String url, String registerTag, Bitmap bitmap, String name, String email, String password, String filePath, String phoneNumber, String emergencyContact, String height, String weight, String gender, String hair_color, String eye_color) {
    	   try {
    		    HttpClient httpClient = new DefaultHttpClient();
    		    HttpPost httpPost = new HttpPost(url);

    		    MultipartEntity entity = new MultipartEntity(
    		      HttpMultipartMode.BROWSER_COMPATIBLE);

/*    		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		    bitmap.compress(CompressFormat.JPEG, 100, bos);
    		    byte[] data = bos.toByteArray();
    		    String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);*/
    	        File file = new File(filePath);
    	        FileBody cbFile = new FileBody(file);  
    		    entity.addPart("tag", new StringBody(registerTag, Charset.forName("UTF-8")));
    		    entity.addPart("name", new StringBody(name, Charset.forName("UTF-8")));
    		    entity.addPart("email", new StringBody(email, Charset.forName("UTF-8")));
    		    entity.addPart("password", new StringBody(password, Charset.forName("UTF-8")));
    		    entity.addPart("user_number", new StringBody(phoneNumber, Charset.forName("UTF-8")));
    		    entity.addPart("emergency_number", new StringBody(emergencyContact, Charset.forName("UTF-8")));
    		    entity.addPart("height", new StringBody(height, Charset.forName("UTF-8")));
    		    entity.addPart("weight", new StringBody(weight, Charset.forName("UTF-8")));
    		    entity.addPart("gender", new StringBody(gender, Charset.forName("UTF-8")));
    		    entity.addPart("hair_color", new StringBody(hair_color, Charset.forName("UTF-8")));
    		    entity.addPart("eye_color", new StringBody(eye_color, Charset.forName("UTF-8")));
    		    entity.addPart("picture", cbFile);
    		    httpPost.setEntity(entity);
                System.out.println("Set entity");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                System.out.println("Received content");
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           } catch (ClientProtocolException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
    	   
           try {
               BufferedReader reader = new BufferedReader(new InputStreamReader(
                       is, "iso-8859-1"), 8);
               StringBuilder sb = new StringBuilder();
               String line = null;
               while ((line = reader.readLine()) != null) {
                   sb.append(line + "n");
               }
               is.close();
               json = sb.toString();
               Log.e("JSON", json);
           } catch (Exception e) {
               Log.e("Buffer Error", "Error converting result " + e.toString());
           }
    
           // try parse the string to a JSON object
           try {
               jObj = new JSONObject(json);            
           } catch (JSONException e) {
               Log.e("JSON Parser", "Error parsing data " + e.toString());
           }
    
           // return JSON String
           return jObj;
    }
    
    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
 
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            System.out.println("Set entity");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            System.out.println("Received content");
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);            
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }
}
