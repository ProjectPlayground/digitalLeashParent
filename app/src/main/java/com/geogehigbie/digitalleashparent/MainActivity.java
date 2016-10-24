package com.geogehigbie.digitalleashparent;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadFirstFragment();

//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().hide();

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


    public void getChildStatus() {

        Random random = new Random();
        int number = random.nextInt(2);


        if (number == 0) {
            isGoodChild = false;
        } else {
            isGoodChild = true;
        }

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
                        .addHeader("X-HTTP-Method-Override","PUT")
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
}


//    public void basicURLConnection(){
//
//        createURL(userIDParent);
//        HttpURLConnection connection = null;
//        BufferedReader reader = null;
//
//        try {
//            URL url = new URL(URLString);
//            connection = (HttpURLConnection) url.openConnection();
//
//            InputStream stream = connection.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(stream));
//            StringBuffer buffer = new StringBuffer();
//
//            String line= "";
//
//            while((line = reader.readLine()) != null){
//
//            }
//
//
//        }catch(MalformedURLException exception){
//            exception.printStackTrace();
//        }catch(IOException exception){
//            exception.printStackTrace();
//        }finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//            try {
//                if(reader != null) {
//                    reader.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//   }


//    public void senddatatoserver(View v) {
//
//        //calls the createJSONFunction
//        String JSONString = createJSON();
//        String URLString = createURL(userIDParent);
//
//        class SendDataToServer extends AsyncTask<String,String,String> {
//
//            @Override
//            protected String doInBackground(String... params) {
//
//                //creates a new OKHttpClient
//                OkHttpClient client = new OkHttpClient();
//
//                //defines a request being sent to server
//                RequestBody postData = new FormBody.Builder()
//                        .add("type", "json")
//                        .build();
//
//                //
//                Request request = new Request.Builder()
//                        .url(URLString);
//                .post(postData);
//                .build();
//
//                //Execute the request
//                Response response = client.newCall(request).execute();
//                String result = response.body().string();
//                return result;
//
//            }
//
//            catch(
//            Exception e
//            )
//
//            {
//                return null;
//            }
//        }
//
//
//
//
//
//            @Override
//            protected void onPostExecute(String s) {
//            }
//
//        }
//    }

//add background inline class here
//        class SendJsonDataToServer extends Async<String,String,String>{
//
//
//        }


//need to do HTTP request activity on seperate thread with an Asynchronous request

//makes use of OKHTTP to post

//    public String post(String url, String json) throws IOException {
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        Response response = client.newCall(request).execute();
//
//        return response.body().string();
//    }


//    public void HTTPRequestRun(String userID, String URL, String JSON) {
//        userID = userIDParent;
//
//        URLString = "https://turntotech.firebaseio.com/digitalleash/" + "userID" + ".json.";
//
//        createJSON();
//        post(URLString, JSONString);
//
//    }


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

