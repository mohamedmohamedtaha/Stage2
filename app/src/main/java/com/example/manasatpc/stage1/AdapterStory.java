package com.example.manasatpc.stage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ManasatPC on 27/06/18.
 */

public class AdapterStory extends ArrayAdapter<Story> {

    public AdapterStory(@NonNull Context context, List<Story> story) {
        super(context, 0, story);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Check if there is an exixting list item view (called convertView) that we can reuse,
        //otherwise ,if convertView is null,then inflate a new list itemlayout
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item_news, parent, false);
        }
        //find the story at the given position in the list of storyes
        Story currentStory = getItem(position);
        //Find the TextViews with IDs
        ImageView iv_imageView = (ImageView) listItemView.findViewById(R.id.im_image_view);
        TextView tv_article_title = (TextView) listItemView.findViewById(R.id.tv_article_title);
        TextView tv_department = (TextView) listItemView.findViewById(R.id.tv_department);
        TextView tv_author_name = (TextView) listItemView.findViewById(R.id.tv_author_name);
        TextView tv_date_published = (TextView) listItemView.findViewById(R.id.tv_date_published);

        //If for sure Is currentStory.getImage_story() is Empty or not
        if (TextUtils.isEmpty(currentStory.getImage_story())) {
            Picasso.with(getContext()).load(R.drawable.no_image)
                    .into(iv_imageView);
        } else {
            Picasso.with(getContext()).load(currentStory.getImage_story())
                    .error(R.drawable.ic_launcher_foreground).into(iv_imageView);
        }

        // Display the content in TextViews
        tv_article_title.setText(currentStory.getmArticle_title());
        tv_department.setText(currentStory.getmDepartment());

        //If for sure Is currentStory.getmAuther_name() is Empty or not
        if (TextUtils.isEmpty(currentStory.getmAuther_name())) {

            tv_author_name.setText(R.string.no_name);

        } else {
            tv_author_name.setText(currentStory.getmAuther_name());

        }
        //For show Date only
        StringBuilder DateOnly = new StringBuilder(currentStory.getmDate_published());
        DateOnly.delete(10, 20);
        tv_date_published.setText(DateOnly);

        return listItemView;
    }
}