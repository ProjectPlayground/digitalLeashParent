package com.geogehigbie.digitalleashparent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import layout.FragmentBadChild;
import layout.FragmentDataParent;
import layout.FragmentGoodChild;
import okhttp3.OkHttpClient;

public class MainActivity extends FragmentActivity {

    private String userID;
    private String radius;
    private String longitude;
    private String latitude;

    private JSONObject parentJSON;
    private String JSONString;

    private String URLString;

    private RelativeLayout relativeLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private boolean isGoodChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadFirstFragment();

    }

    public void loadFirstFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new FragmentDataParent());
        fragmentTransaction.commit();

    }


    public void onClickCreate(View view){

        playSoundEffects();
        getParentData();

    }


    public void onClickCheckStatus(View view){

        playSoundEffects();
        getChildStatus();

        if(isGoodChild){
            loadGoodChildFragment();

        }
        else{
            loadBadChildFragment();

        }

    }


    public void onClickUpdate(View view){

        playSoundEffects();
        getParentData();

    }



    public void playSoundEffects(){

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click_on_sound);
        mediaPlayer.start();

    }

    public void getParentData() {
        EditText parentUserID = (EditText) findViewById(R.id.usernameEditText);
        EditText parentRadius = (EditText) findViewById(R.id.radiusEditText);
        EditText parentLongitute = (EditText) findViewById(R.id.longitudeEditText);
        EditText parentLatitude = (EditText) findViewById(R.id.latitudeEditText);

        userID = parentUserID.getText().toString();
        radius = parentRadius.getText().toString();
        longitude = parentLongitute.getText().toString();
        latitude = parentLatitude.getText().toString();

        createJSON();

    }


        public void getChildStatus() {

            isGoodChild = false;

    }


    public void loadBadChildFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FragmentBadChild());
        fragmentTransaction.commit();

    }

    public void loadGoodChildFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new FragmentGoodChild());
        fragmentTransaction.commit();

    }


//    public void setUpFragment() {
//
//        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.fragment_container, new FragmentFirstPage()).commit();
//        fragmentTransaction.replace(R.id.fragment_container, new FragmentMainLayout()).commit();
//        //TODO: after some delay add the main layout
//
//        //fragmentTransaction.replace(R.id.fragment_container, new FragmentMainLayout()).commit();
//    }

    public void loadFraments() {
        if (isGoodChild) {
            fragmentTransaction.replace(R.id.fragment_container, new FragmentGoodChild()).commit();
        }

        fragmentTransaction.replace(R.id.fragment_container, new FragmentBadChild()).commit();

    }






    public void createJSON() {
        parentJSON = new JSONObject();

        try {
            parentJSON.put("userID", userID);
            parentJSON.put("radius", radius);
            parentJSON.put("longitute", longitude);
            parentJSON.put("latitude", latitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //need to do HTTP request activity on seperate thread with an Asynchronous request

    //makes use of OKHTTP to post


    public void HTTPRequestRun(String userID, String URL, String JSON) {
        this.userID = userID;

        URLString = "https://turntotech.firebaseio.com/digitalleash/" + "userID" + ".json.";
        OkHttpClient client = new OkHttpClient();

//        String post(String url, String json) throws IOException {
//            RequestBody body = RequestBody.create(JSON, json);
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .build();
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        }

//        public class PostExample {
//
//            OkHttpClient client = new OkHttpClient();
//
//            String post(String url, String json) throws IOException {
//                RequestBody body = RequestBody.create(JSON, json);
//                Request request = new Request.Builder()
//                        .url(url)
//                        .post(body)
//                        .build();
//                try (Response response = client.newCall(request).execute()) {
//                    return response.body().string();
//                }
//            }
//
//
//        }

//    public void senddatatoserver(View v) {
//
//        URLString = "https://turntotech.firebaseio.com/digitalleash/" + "userID" + ".json.";
//
//        if (parentJSON.length() > 0) {
//            new SendJsonDataToServer().execute(String.valueOf(parentJSON));
//            #call to async class
//        }


    }
// TODO: AsyncTask

//    class SendDataToServer extends AsyncTask<String,String,String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//        }
//
//
//        @Override
//        protected void onPostExecute(String s) {
//        }
//
//    }
//}
//
//}
}
