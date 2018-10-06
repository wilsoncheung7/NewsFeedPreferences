package com.wewsun.newsfeedpreferences;

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

import com.wewsun.newsfeedpreferences.NewsFeed;
import com.wewsun.newsfeedpreferences.NewsFeedAdapter;
import com.wewsun.newsfeedpreferences.NewsFeedLoader;
import com.wewsun.newsfeedpreferences.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsFeed>> {

    private TextView mEmptyStateTextView;


    private static final int NEWSFEED_LOADER_ID = 1;

    private NewsFeedAdapter mAdapter;

    private static final String MTR_REQUEST_URL =
            "https://content.guardianapis.com/search?show-tags=contributor&show-fields=thumbnail&api-key=d9ee44bf-13b1-4765-b571-e60895d3f70d";


    @Override
    // This method initialize the contents of the Activity's options main.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        ListView newsfeedListView = findViewById(R.id.list);

        mAdapter = new NewsFeedAdapter(this, new ArrayList<NewsFeed>());
        newsfeedListView.setEmptyView(mEmptyStateTextView);

        newsfeedListView.setAdapter(mAdapter);

        newsfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                NewsFeed currentNewsFeed = mAdapter.getItem(position);

                Uri newsfeedUri = Uri.parse(currentNewsFeed.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsfeedUri);

                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWSFEED_LOADER_ID, null, this);
        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    public Loader<List<NewsFeed>> onCreateLoader(int i, Bundle bundle){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String cat = sharedPreferences.getString(getString(R.string.setting_order_by_key),getString(R.string.settings_category_default));

        Uri baseUri = Uri.parse(MTR_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("section", cat);

        return new NewsFeedLoader(this, uriBuilder.toString());
    }

    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> newsFeeds) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_news_feed);

        mAdapter.clear();

        if (newsFeeds != null && !newsFeeds.isEmpty()) {
            mAdapter.addAll(newsFeeds);
            mEmptyStateTextView.setVisibility(View.INVISIBLE);
        }
        else {

        }
    }
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {

        mAdapter.clear();
    }

}
