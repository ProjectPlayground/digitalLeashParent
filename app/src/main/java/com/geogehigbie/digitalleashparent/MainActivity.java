package com.geogehigbie.digitalleashparent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String userID;
    private String radius;
    private String longitude;
    private String latitude;

    private JSONObject parentJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getParentData();

        getChildStatus();

        try {
            createJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getParentData(){
        EditText parentUserID = (EditText) findViewById(R.id.usernameEditText);
        EditText parentRadius = (EditText) findViewById(R.id.radiusEditText);
        EditText parentLongitute = (EditText) findViewById(R.id.longitudeEditText);
        EditText parentLatitude = (EditText) findViewById(R.id.latitudeEditText);

        userID = parentUserID.getText().toString();
        radius = parentRadius.getText().toString();
        longitude = parentLongitute.getText().toString();
        latitude = parentLatitude.getText().toString();

    }


    public void getChildStatus(){

    }

    public void createJSON() throws JSONException {
        JSONObject parentJSON = new JSONObject();
        parentJSON.put("userID", userID);
        parentJSON.put("radius", radius);
        parentJSON.put("longitute", longitude);
        parentJSON.put("latitude", latitude);


    }

    //need to do HTTP request activity on seperate thread with an Asynchronous request
}
