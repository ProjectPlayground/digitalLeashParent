package com.geogehigbie.digitalleashparent;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import layout.FragmentBadChild;
import layout.FragmentDataParent;
import layout.FragmentGoodChild;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.geogehigbie.digitalleashparent.Post.JSON;

public class MainActivity extends FragmentActivity {

    private String userIDParent;
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

    //HTTP variables
    private OkHttpClient client = new OkHttpClient();   //from OkHttp


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadFirstFragment();

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

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

        setContentView(view); //sends data to server

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


    public void messageToast(String string) {

        Toast toast = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG);
        toast.show();

    }



    public void onClickBadChild(View view){
        String warningMessage = "Your kid is playing in traffic! Panic!!!";

        playSoundEffects();
        messageToast(warningMessage);
        loadFirstFragment();

    }

    public void onClickGoodChild(View view){
        String partyMessage = "Your kid is alive. Get Drunk!";

        playSoundEffects();
        messageToast(partyMessage);
        loadFirstFragment();
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

        userIDParent = parentUserID.getText().toString();
        radius = parentRadius.getText().toString();
        longitude = parentLongitute.getText().toString();
        latitude = parentLatitude.getText().toString();

        createJSON();

    }


    public void getChildStatus() {

        Random random = new Random();
        int number =  random.nextInt(2);


        if(number == 0){
            isGoodChild = false;
        }
        else{
            isGoodChild = true;
        }

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


    public String createJSON() {
        parentJSON = new JSONObject();

        try {
            parentJSON.put("userID", userIDParent);
            parentJSON.put("radius", radius);
            parentJSON.put("longitute", longitude);
            parentJSON.put("latitude", latitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }

       JSONString = parentJSON.toString();
        return JSONString;

    }

    public String createURL(String userID){
        String URLBase = "https://turntotech.firebaseio.com/digitalleash/";
        userID = userID + ".json.";

        URLString = URLBase + userID;
        return URLString;
    }

    public void sendData(){
        String URLString = createURL(userIDParent);
        String JSONString = createJSON();

        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... params) {
                return getServerResponse();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }
    }

    private String getServerResponse(){
        return null;
    }

    public void basicURLConnection(){

        createURL(userIDParent);
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(URLString);
            connection = (HttpURLConnection) url.openConnection();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line= "";

            while((line = reader.readLine()) != null){

            }


        }catch(MalformedURLException exception){
            exception.printStackTrace();
        }catch(IOException exception){
            exception.printStackTrace();
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

   }


    public void senddatatoserver(View v) {

        //calls the createJSONFunction
        String JSONString = createJSON();
        String URLString = createURL(userIDParent);

        class SendDataToServer extends AsyncTask<String,String,String> {

            @Override
            protected String doInBackground(String... params) {

                //creates a new OKHttpClient
                OkHttpClient client = new OkHttpClient();

                //defines a request being sent to server
                RequestBody postData = new FormBody.Builder()
                        .add("type", "json")
                        .build();

                //
                Request request = new Request.Builder()
                        .url(URLString);
                .post(postData);
                .build();

                //Execute the request
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                return result;

            }

            catch(
            Exception e
            )

            {
                return null;
            }
        }





            @Override
            protected void onPostExecute(String s) {
            }

        }
    }

//add background inline class here
        class SendJsonDataToServer extends Async<String,String,String>{
        }




    //need to do HTTP request activity on seperate thread with an Asynchronous request

    //makes use of OKHTTP to post

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }


    public void HTTPRequestRun(String userID, String URL, String JSON) {
        userID = userIDParent;

        URLString = "https://turntotech.firebaseio.com/digitalleash/" + "userID" + ".json.";

        createJSON();
        post(URLString, JSONString);

    }





        public class PostExample {

            OkHttpClient client = new OkHttpClient();

            String post(String url, String json) throws IOException {
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    return response.body().string();
                }
            }


        }

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
