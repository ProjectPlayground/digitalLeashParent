package com.geogehigbie.digitalleashparent;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.geogehigbie.digitalleashparent.Post.JSON;

/**
 * Created by georgehigbie on 10/18/16.
 */

public class PostHTTP {

    OkHttpClient client = new OkHttpClient();


    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
