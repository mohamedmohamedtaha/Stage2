package com.example.manasatpc.stage1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by ManasatPC on 28/06/18.
 */

public class StoryLoader extends AsyncTaskLoader<List<Story>> {
    private String mUrl;

    // constructor
    public StoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    //this method for load data in background
    @Override
    public List<Story> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Story> stories = QueryUtils.fetchStoryData(mUrl);
        return stories;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}


















