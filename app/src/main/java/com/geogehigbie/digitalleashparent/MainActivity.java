package com.geogehigbie.digitalleashparent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String userID;
    private String radius;
    private String longitude;
    private String latitude;

    private JSONObject parentJSON;
    private String JSONString;

    private String URLString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getParentData();

        getChildStatus();


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

        createJSON();

    }


    public void getChildStatus(){

    }


    public void createJSON(){
        parentJSON = new JSONObject();

        try{
            parentJSON.put("userID", userID);
            parentJSON.put("radius", radius);
            parentJSON.put("longitute", longitude);
            parentJSON.put("latitude", latitude);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }


    //need to do HTTP request activity on seperate thread with an Asynchronous request

    //makes use of OKHTTP to post


    public void HTTPRequestRun(String userID, String URL, String JSON){
        this.userID = userID;

        URLString = "https://turntotech.firebaseio.com/digitalleash/" + "userID" + ".json.";
        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

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

    public void senddatatoserver(View v) {

        URLString = "https://turntotech.firebaseio.com/digitalleash/" + "userID" + ".json.";

        if (parentJSON.length() > 0) {
            new SendJsonDataToServer().execute(String.valueOf(parentJSON));
            #call to async class
        }



    }
// TODO: AsyncTask

    class SendDataToServer extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

        }


        @Override
        protected void onPostExecute(String s) {
        }

    }
}

}
