package com.example.apitester.network;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.apitester.MainActivity;
import com.example.apitester.pojo.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryUtils {

    static Toast mToast;
    private static final String LOG_TAG = "Query Utils";

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem building the URL "+ e.getLocalizedMessage());
        }
        return url;
    }

    public static int makeGetHttpRequest(String urll, int method, ArrayList<String> headers) throws IOException {
        String jsonResponse = "";
        String mUrl= urll;
        if (mUrl == null) {
            return -1;
        }
        URL url = createUrl(mUrl);
        if (url == null){
            return -1;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            Log.i("TAG", "mmmmf3: ");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            for (int i = 0; i <headers.size() ; i++){
                urlConnection.setRequestProperty("x-authorization"+i+1, headers.get(i));
            }

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        Log.i("TAG", "makeHttpRequest: ");
                        inputStream = urlConnection.getInputStream();
                        jsonResponse = readFromStream(inputStream);
                    }
                extractFeatureFromJson(jsonResponse);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return urlConnection.getResponseCode();
    }

    public static int makePostHttpRequest(String urll, int method, ArrayList<String> headers, HashMap<String,String> data) throws IOException {
        String jsonResponse = "";
        String mUrl= urll;
        if (mUrl == null) {
            return -1;
        }
        URL url = createUrl(mUrl);
        if (url == null) {
            return -1;
        }
        Log.i("TAGd", "mmmmf2: ");
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            Log.i("TAG", "mmmmf3: ");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            for (int i = 0; i <headers.size() ; i++){
                urlConnection.setRequestProperty("x-authorization"+i+1, headers.get(i));
            }

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(paramsToString(data));
                writer.flush();
                writer.close();
                os.close();
                urlConnection.connect();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return urlConnection.getResponseCode();
    }
    private static String paramsToString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        Uri.Builder builder = new Uri.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().getEncodedQuery();
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static int extractFeatureFromJson(String postJSON) {
        if (TextUtils.isEmpty(postJSON)) {
            return -1;
        }


        try {

            JSONArray postArray = new JSONArray(postJSON);

            for (int i = 0; i < postArray.length(); i++) {

                JSONObject currentPost = postArray.getJSONObject(i);


                int userId = currentPost.getInt("userId");


                int id = currentPost.getInt("id");


                String title = currentPost.getString("title");

                String body = currentPost.getString("body");

                Post post = new Post(title, body ,userId, id);

                MainActivity.posts.add(post);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return 1;
    }

}
