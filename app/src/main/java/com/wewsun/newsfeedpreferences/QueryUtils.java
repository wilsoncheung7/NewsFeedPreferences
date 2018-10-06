package com.wewsun.newsfeedpreferences;

import android.text.TextUtils;
import android.util.Log;
import android.support.v4.app.ActivityCompat;

import com.wewsun.newsfeedpreferences.NewsFeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;


public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
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
        return jsonResponse;
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

    private static List<NewsFeed> extractFeatureFromJson(String newsfeedJSON){

        if (TextUtils.isEmpty(newsfeedJSON)){
            return null;
        }

        List<NewsFeed> newsfeeds = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsfeedJSON);

            JSONObject newsfeedObject = baseJsonResponse.getJSONObject("response");
            JSONArray newsfeedArray = newsfeedObject.getJSONArray("results");


            for (int i = 0; i <newsfeedArray.length(); i++){
                JSONObject currentArray = newsfeedArray.getJSONObject(i);
                String author = "";
                String title = currentArray.getString("webTitle");
                String time = currentArray.getString( "webPublicationDate");
                String url = currentArray.getString("webUrl");
                String section = currentArray.getString("sectionName");
//                JSONObject resultsObject = baseJsonResponse.getJSONObject("results");
                JSONArray tagsArray = currentArray.getJSONArray("tags");
                for (int j = 0; j<tagsArray.length(); j++) {
                    JSONObject tagsObject = tagsArray.getJSONObject(j);
                    author = tagsObject.getString("webTitle");
                }
                NewsFeed newsFeed = new NewsFeed(title, time, url, section, author);
                newsfeeds.add(newsFeed);
            }
        }

        catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return newsfeeds;
    }

    public static List<NewsFeed> fetchnNewsfeedData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<NewsFeed> newsFeeds = extractFeatureFromJson(jsonResponse);

        return newsFeeds;
    }
}
