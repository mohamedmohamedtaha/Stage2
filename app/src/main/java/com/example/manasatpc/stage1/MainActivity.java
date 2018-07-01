package com.example.manasatpc.stage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {
    //Constant value for the story loader ID
    private static final int STORY_LOADER_ID = 1;
    //URL for story data from the guardianapis
    // private static final String STORY_REQUEST_URL = "https://content.guardianapis.com/search?section=football&show-tags=contributor&api-key=8c9e6164-9e81-4e26-a43f-a869c5e96fc1";
    private static final String STORY_REQUEST_URL = "https://content.guardianapis.com/search";

    //textView that is display when the list is empty
    private TextView mEmptyTextView;
    //Adapter for the list of Story
    private AdapterStory mAdapter;
    private View loaderIndictor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find a reference  to the {@link ListView} in the layout
        ListView storyListView = (ListView) findViewById(R.id.list);

        //create anew ArrayAdapter of story
        mAdapter = new AdapterStory(this, new ArrayList<Story>());

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
                Intent webSiteIntent = new Intent(Intent.ACTION_VIEW, storyUri);

                //send the intent to launch a new activity
                startActivity(webSiteIntent);
            }
        });

        mEmptyTextView = (TextView) findViewById(R.id.tv_empty_view);
        storyListView.setEmptyView(mEmptyTextView);


        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //If there is a network connection , fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(STORY_LOADER_ID, null, this);

        } else {
            loaderIndictor = findViewById(R.id.loading_indicator);
            loaderIndictor.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_internet);
        }

    }

    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String section = sharedPreferences.getString(getString(R.string.settings_department_key),
                getString(R.string.settings_department_default));

        String from_date = sharedPreferences.getString(getString(R.string.settings_from_date_key),
                getString(R.string.settings_from_date_default));

        String to_date = sharedPreferences.getString(getString(R.string.settings_to_date_key),
                getString(R.string.settings_to_date_default));

        String order_date = sharedPreferences.getString(getString(R.string.settings_order_date_key),
                getString(R.string.settings_order_date_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(STORY_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder builder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `section=football`
        builder.appendQueryParameter("section", section);
        builder.appendQueryParameter("from-date", from_date);
        builder.appendQueryParameter("to-date", to_date);
        builder.appendQueryParameter("order-date", order_date);

        builder.appendQueryParameter("show-tags", "contributor");
        builder.appendQueryParameter("api-key", "8c9e6164-9e81-4e26-a43f-a869c5e96fc1");
        // Return the completed
        // https://content.guardianapis.com/search?section=football&show-tags=contributor&api-key=8c9e6164-9e81-4e26-a43f-a869c5e96fc1
        return new StoryLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {
        //Hide loading indicator because teh data has been loaded
        View loafdingIndicator = findViewById(R.id.loading_indicator);
        loafdingIndicator.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.no_story_found);
        mAdapter.clear();

        if (stories != null && !stories.isEmpty()) {
            mAdapter.addAll(stories);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mAdapter.clear();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
























