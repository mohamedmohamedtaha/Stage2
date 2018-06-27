package com.example.manasatpc.stage1;

/**
 * Created by ManasatPC on 27/06/18.
 */

public class Story {
    private int image_story;
    private String mAuther_name, mDepartment, mArticle_title, mUrl;
    private Long mDate_published;

    public Story(/*int image_story, String mAuther_name,*/ String mDepartment, String mArticle_title, String mUrl, Long mDate_published) {
      //  this.image_story = image_story;
       // this.mAuther_name = mAuther_name;
        this.mDepartment = mDepartment;
        this.mArticle_title = mArticle_title;
        this.mUrl = mUrl;
        this.mDate_published = mDate_published;
    }

    public int getImage_story() {
        return image_story;
    }

    public String getmAuther_name() {
        return mAuther_name;
    }

    public String getmDepartment() {
        return mDepartment;
    }

    public String getmArticle_title() {
        return mArticle_title;
    }

    public String getmUrl() {
        return mUrl;
    }

    public Long getmDate_published() {
        return mDate_published;
    }
}
