package com.example.manasatpc.stage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Constant value for the story loader ID
    private static final int STORY_LOADER_ID = 1;
    //textView that is display when the list is empty
    private TextView mEmptyTextView;
    //URL for story data from the guardianapis
    private static final String STORY_REQUEST_URL ="https://content.guardianapis.com/search?api-key=8c9e6164-9e81-4e26-a43f-a869c5e96fc1";
    //Adapter for the list of Story
    private AdapterStory mAdapter;
    private View loaderIndictor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find a reference  to the {@link ListView} in the layout
        ListView storyListView = (ListView)findViewById(R.id.list);

        //create anew ArrayAdapter of story
        mAdapter = new AdapterStory(this,new ArrayList<Story>());

        //Set the adapter on the listview
        //so the list can be populated in the user interface
        storyListView.setAdapter(mAdapter);

        //this method will make the user when press an item in listView open details
        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //find the current story that was clicked on
                Story currentStory = mAdapter.getItem(i);

                //Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri storyUri = Uri.parse(currentStory.getmUrl());

                //Create a new intent to view the Story URI
                Intent webSiteIntent = new Intent(Intent.ACTION_VIEW,storyUri);

                //send the intent to launch a new activity
                startActivity(webSiteIntent);
            }
        });

        mEmptyTextView= (TextView)findViewById(R.id.tv_empty_view);
        storyListView.setEmptyView(mEmptyTextView);


        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //If there is a network connection , fetch data
        if (networkInfo != null && networkInfo.isConnected()){
      //      LoaderManager loaderManager = getLoaderManager();
         //   loaderManager.initLoader(STORY_LOADER_ID,null,this);

            StoryTask storyTask = new StoryTask();
            storyTask.execute(STORY_REQUEST_URL);

        }else {
             loaderIndictor = findViewById(R.id.loading_indicator);
            loaderIndictor.setVisibility(View.GONE);
            mEmptyTextView.setText("No Internet Connection.");
        }

            }
/*
    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {
        //Hide loading indicator because teh data has been loaded
        loaderIndictor.setVisibility(View.GONE);

        mEmptyTextView.setText("No Story found");
        mAdapter.clear();

        if (stories != null && !stories.isEmpty()){
            mAdapter.addAll(stories);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mAdapter.clear();

    }

*/
    private class StoryTask extends AsyncTask<String,Void,List<Story>> {

        /*
        this method runs on  a background thread and perform the network request.
        we should not update the UI from a background thread , so we return a list of
        @link Earthquake as the result
         */

        @Override
        protected List<Story> doInBackground(String... url) {
            //Don't perform the request if there are no URLs , or the first URL is null
            if (url.length < 1 || url[0] == null){
                return null;
            }
            List<Story> story =QueryUtils.fetchStoryData(url[0]);

            return story;
        }
        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of earthquake data from a previous
         * query to USGS. Then we update the adapter with the new list of earthquakes,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<Story> story) {
            //Clear the adapter of previous earthquake data
            mAdapter.clear();
            /*
            If tehre is a valid list of @link Earthquake , then add them to the adapter's
            data set. This will trigger the ListView to update

             */
            if (story != null && !story.isEmpty()){
                mAdapter.addAll(story);
            }
        }
    }


}
























