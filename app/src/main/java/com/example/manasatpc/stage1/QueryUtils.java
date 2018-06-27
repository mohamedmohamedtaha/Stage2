package com.example.manasatpc.stage1;

import android.text.TextUtils;
import android.widget.Toast;

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

/**
 * Created by ManasatPC on 27/06/18.
 */
 //Class for Helper methods related to requesting and receiving earthquake data from guardianapis .

public class QueryUtils {
    public QueryUtils() {
    }
    //Return new URL object from the given string URL
    private static URL createUrl(String stringUrl){
        URL url =null;
        try {
            url =new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    //Make a HTTP request to the given URL and return a String as the response
    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse = "";
        //If the URL is null, then return early
        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream =null;
        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request was successful (response code 200),
            // then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
               // Toast.makeText(get, "Error response code:"+ urlConnection.getResponseCode(), Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e){
            // Toast.makeText(get, "Problem retriving the Story JSON results", Toast.LENGTH_SHORT).show();

        }finally {
            if (urlConnection!= null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    //convert the InputStream into a String which contains the whole JSON response from server
    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    //Return an Event object by parsing out information about the first story from theinput StoryJSON string
    private static List<Story>extractFeatureFromJson(String storyJSON){
        //if the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(storyJSON)){
            return null;
        }
        //Create an empty ArrayList that we can start adding story to
        List<Story> stores = new ArrayList<>();
        /*try to parse the JSON response string . if there's a problem with the way the JSON
        is fromatted ,  a JSONException exception object will be thrown .
        catch the Exception so the app doesn't crash , and print the Error message to the user
       */
        try {
            //Create a JSONObject from the JSON response string
            JSONObject baseJsonRespone = new JSONObject(storyJSON);
            /*
            Extract a JSONArray associated with the key called "results",
            which represents a list of feature (or story)
             */
            JSONObject storyObject = baseJsonRespone.getJSONObject("response");

            JSONArray results = baseJsonRespone.getJSONArray("results");

            //if there are results in the storyArray
            //if storyArray.length()> 0) for each story int the storyArray ,create an link Story object
            for (int i = 0; i<results.length();i++){
                //Get a single story at position i within the list of story
                JSONObject currentStory = results.getJSONObject(i);

                //for a given story ,extract the JSONObject associated with the key
                //called "results" ,which represents a list of all properties for that story
              //  JSONArray resultss = currentStory.getJSONArray("results");

                Long date = currentStory.getLong("webPublicationDate");
                String title = currentStory.getString("webTitle");
                String url = currentStory.getString("webUrl");
                String department = currentStory.getString("sectionName");
                //create a new link Story object with the arguments
                Story story = new Story(department,title,url,date);

                //Add the new link story tot he list of stories
                stores.add(story);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return the list of stories
        return stores;
    }
    //Query the Story dataset and return an Story object to represent a single Story
    public static List<Story> fetchStoryData(String requestUrl){
        //Cretae URL object
        URL url = createUrl(requestUrl);

        //perform HTTP request to the URL and receive aJSON response back
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Extract relevant fields from JSON esponse and create an Story object
        List<Story> story =extractFeatureFromJson(jsonResponse);
        //Return the list Story
        return story;
    }
}















