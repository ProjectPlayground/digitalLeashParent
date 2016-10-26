package com.geogehigbie.digitalleashparent;

import android.location.Location;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import layout.FragmentBadChild;
import layout.FragmentDataParent;
import layout.FragmentGoodChild;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends FragmentActivity {

    private String userIDParent;
    private String radius;
    private String longitude;
    private String latitude;

    private JSONObject parentJSON;
    private String JSONString;

    private String URLString;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private boolean isGoodChild;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String childLatitudeString;
    private String childLongitudeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadFirstFragment();


    }

    public void loadFirstFragment() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new FragmentDataParent());
        fragmentTransaction.commit();

    }


    public void onClickCreate(View view) {

        playSoundEffects();
        getParentData();

        sendJSONData();

        setTextViews();

    }


    public void onClickCheckStatus(View view) {

        playSoundEffects();
        getChildStatus();


        if (isGoodChild) {
            loadGoodChildFragment();

        } else {
            loadBadChildFragment();

        }

        setTextViews();

    }


    public void onClickUpdate(View view) {

        playSoundEffects();
        setTextViews();
        getParentData();
        sendJSONData();

    }


    public void messageToast(String string) {

        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG);
        toast.show();

    }


    public void onClickBadChild(View view) {
        String warningMessage;

        Random random = new Random();
        int number = random.nextInt(4);

        if (number == 0) {
            warningMessage = "Your kid is playing in traffic and smoking crack! Panic!!!";
        } else if (number == 1) {
            warningMessage = "Your kid is robbing a bank. You failed as a parent! Panic!!!";
        } else if (number == 2) {
            warningMessage = "Maybe you should have taken that parenting class...your kids is running wild";
        } else {
            warningMessage = "The cops have your child. You are truly a terrible parent. Panic!!!";
        }

        playSoundEffects();
        messageToast(warningMessage);
        getSupportFragmentManager().popBackStack();


    }

    public void onClickGoodChild(View view) {

        String partyMessage;

        Random random = new Random();
        int number = random.nextInt(4);

        if (number == 0) {
            partyMessage = "Your kid is alive but has ADHD. Get Drunk!";
        } else if (number == 1) {
            partyMessage = "Your kid is NOT robbing a bank. Get Drunk!";
        } else if (number == 3) {
            partyMessage = "Maybe you should have taken that parenting class...you are relying too much on your phone. YOU SUCK!";
        } else {
            partyMessage = "Your kid is NOT smoking crack. Get Drunk!";
        }


        playSoundEffects();
        messageToast(partyMessage);
        // loadFirstFragment();
        getSupportFragmentManager().popBackStack();
    }


    public void playSoundEffects() {

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click_on_sound);
        mediaPlayer.start();

    }


    public void getParentData() {
        EditText parentUserID = (EditText) findViewById(R.id.usernameEditText);
        EditText parentRadius = (EditText) findViewById(R.id.radiusEditText);
        EditText parentLongitude = (EditText) findViewById(R.id.longitudeEditText);
        EditText parentLatitude = (EditText) findViewById(R.id.latitudeEditText);

        userIDParent = parentUserID.getText().toString();
        radius = parentRadius.getText().toString();
        longitude = parentLongitude.getText().toString();
        latitude = parentLatitude.getText().toString();

        createJSON();

    }

    public void setTextViews() {
        EditText parentUserID = (EditText) findViewById(R.id.usernameEditText);
        EditText parentRadius = (EditText) findViewById(R.id.radiusEditText);
        EditText parentLongitude = (EditText) findViewById(R.id.longitudeEditText);
        EditText parentLatitude = (EditText) findViewById(R.id.latitudeEditText);

        userIDParent = parentUserID.getText().toString();
        radius = parentRadius.getText().toString();
        longitude = parentLongitude.getText().toString();
        latitude = parentLatitude.getText().toString();

        parentUserID.setText(userIDParent);
        parentRadius.setText(radius);
        parentLongitude.setText(longitude);
        parentLatitude.setText(latitude);
    }


    public void loadBadChildFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new FragmentBadChild()).addToBackStack(null);//.commit();
        fragmentTransaction.commit();

    }


    public void loadGoodChildFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new FragmentGoodChild());
        fragmentTransaction.addToBackStack(null);//.commit();
        fragmentTransaction.commit();

    }


    public String createJSON() {
        parentJSON = new JSONObject();

        try {
            parentJSON.put("username", userIDParent);
            parentJSON.put("radius", radius);
            parentJSON.put("longitude", longitude);
            parentJSON.put("latitude", latitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONString = parentJSON.toString();
        return JSONString;

    }

    public String createURL(String userID) {
        String URLBase = "https://turntotech.firebaseio.com/digitalleash/";
        userID = userID + ".json";

        URLString = URLBase + userID;
        return URLString;
    }


    public class SendJSONData extends AsyncTask<Void, Void, String> {

        String JSONString = createJSON();
        String URLString = createURL(userIDParent);

        @Override
        protected String doInBackground(Void... params) {

            Log.d("doInBackground", "called");

            try {
                Log.d("try", "called");

                OkHttpClient client = new OkHttpClient();

                RequestBody postdata = RequestBody.create(JSON, JSONString);

                Request request = new Request.Builder()
                        .url(URLString)
                        .addHeader("X-HTTP-Method-Override", "PUT")
                        .post(postdata)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("response", "isSuccessful");
                } else {
                    Log.d("response", response.message());

                }

                String result = response.body().string();
                return result;

            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
                return null;
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void sendJSONData() {

        JSONString = createJSON();
        URLString = createURL(userIDParent);

        SendJSONData sendJSONData = new SendJSONData();
        sendJSONData.execute();

    }


    public void getChildStatus() {

        //get JSON data from server


        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URLString)
                    .build();
            Response responses = null;

            try {
                responses = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String jsonData = responses.body().string();
            JSONObject Jobject = new JSONObject(jsonData);

            childLatitudeString = Jobject.getString("child_latitude");
            childLongitudeString = Jobject.getString("child_longitude");

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.getMessage());
        }


        //defines the location
        Location childCurrentLocation = new Location("childCurrentLocation");
        Location childShouldBeLocation = new Location("childShouldBeLocation");

        //converts the location from a string to a double
        double childCurrentLatitude = Double.parseDouble(childLatitudeString);
        double childCurrentLongitude = Double.parseDouble(childLongitudeString);
        double latitudeDoubleShouldBe = Double.parseDouble(latitude);
        double longitudeDoubleShouldBe = Double.parseDouble(longitude);

        //sets the latitude and longitude for the current location of the child

        childCurrentLocation.setLatitude(childCurrentLatitude);
        childCurrentLocation.setLongitude(childCurrentLongitude);

        //sets the latitude and longitude for the location at which the child should be
        childShouldBeLocation.setLatitude(latitudeDoubleShouldBe);
        childShouldBeLocation.setLongitude(longitudeDoubleShouldBe);


        String TAGLatitude = "LAT";
        String TAGLong = "LONG";


        Log.d(TAGLatitude, childLatitudeString);
        Log.d(TAGLong, childLongitudeString);


    }

//randome method no longer needed
//        Random random = new Random();
//        int number = random.nextInt(2);
//
//
//        if (number == 0) {
//            isGoodChild = false;
//        } else {
//            isGoodChild = true;
//        }

}

