package com.example.manasatpc.stage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ManasatPC on 27/06/18.
 */

public class AdapterStory extends ArrayAdapter<Story> {
    public AdapterStory(@NonNull Context context, List<Story> story) {
        super(context,0, story);
    }
    //Return the formatted Date String(i.e "Mar3,2000") from a Date object
    private String formatDate(Date dataObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd,yyyy");
        return dateFormat.format(dataObject);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Check if there is an exixting list item view (called convertView) that we can reuse,
        //otherwise ,if convertView is null,then inflate a new list itemlayout
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item_news,parent,false);
        }
        //find the story at the given position in the list of storyes
        Story currentStory = getItem(position);
        //Find the TextViews with IDs
        ImageView iv_imageView = (ImageView)listItemView.findViewById(R.id.im_image_view);
        TextView tv_article_title = (TextView)listItemView.findViewById(R.id.tv_article_title);
        TextView tv_department = (TextView)listItemView.findViewById(R.id.tv_department);
        TextView tv_author_name = (TextView)listItemView.findViewById(R.id.tv_author_name);
        TextView tv_date_published = (TextView)listItemView.findViewById(R.id.tv_date_published);

        // Display the content in TextViews
        iv_imageView.setImageResource(currentStory.getImage_story());
        tv_article_title.setText(currentStory.getmArticle_title());
        tv_department.setText(currentStory.getmDepartment());
        tv_author_name.setText(currentStory.getmAuther_name());


        //Create anew Date object from the time in miliseconds of hte story
        Date dataObject =new Date(currentStory.getmDate_published());

        //Format the date String
        String formattedDate = formatDate(dataObject);

        tv_date_published.setText(formattedDate);

        return super.getView(position, convertView, parent);
    }
}


























