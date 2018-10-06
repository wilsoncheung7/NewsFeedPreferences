package com.wewsun.newsfeedpreferences;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed> {
    public NewsFeedAdapter(Activity context, ArrayList<NewsFeed> NewsFeed) {
        super(context, 0, NewsFeed);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.newsfeed_list_item, parent, false);
        }
        NewsFeed currentNewsFeed = getItem(position);
        TextView titleView = listItemView.findViewById(R.id.title);
        String title = currentNewsFeed.getTitle();
        titleView.setText(title);
        TextView sectionView = listItemView.findViewById(R.id.sectionName);
        String section = currentNewsFeed.getSection();
        sectionView.setText(section);
        TextView authorView = listItemView.findViewById(R.id.author);
        String author = currentNewsFeed.getAuthor();
        authorView.setText(author);
        TextView dateView = listItemView.findViewById(R.id.date);
        String date = currentNewsFeed.getDate();
        dateView.setText(date);
        return listItemView;
    }
}
