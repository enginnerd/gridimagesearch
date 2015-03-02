package com.codepath.ab.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.Toast;

import com.codepath.ab.gridimagesearch.Listeners.EndlessScrollListener;
import com.codepath.ab.gridimagesearch.R;
import com.codepath.ab.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.ab.gridimagesearch.helpers.HelperMethods;
import com.codepath.ab.gridimagesearch.helpers.HelperVars;
import com.codepath.ab.gridimagesearch.helpers.UrlVars;
import com.codepath.ab.gridimagesearch.models.Filters;
import com.codepath.ab.gridimagesearch.models.ImageResult;
import com.codepath.ab.gridimagesearch.models.PageResult;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragmentActivity extends ActionBarActivity implements FilterSettingsDialog.FilterDialogListener {

    private static StaggeredGridView gvResults;
    private static ArrayList<ImageResult> imageResults;
    private static ImageResultsAdapter aImageResults;
    private static ArrayList<PageResult> pageResults;
    private static int start = 0;
    private static String g_query = null;
    private static Filters filter = null;
    private static Context mContext;

    @Override
    public void onFinishFilterDialog(Filters f){
        Log.i("INFO", "onFinishFilterDialog called");
        filter = f;
    }

    private static void setupViews(View view){

//        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (StaggeredGridView)view.findViewById(R.id.gvResults);

        gvResults.setAdapter(aImageResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                //Todo: use page and totalItemsCount vars to be more flexible that dynamic use of start var
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });


        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(mContext, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(position);
                i.putExtra(HelperVars.INTENT_KEY_RESULT, result);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        mContext = this;
        //create data source for list
        imageResults = new ArrayList<ImageResult>();
        //attach data source to adapter
        aImageResults = new ImageResultsAdapter(mContext, imageResults);
        //link adapter to grid view


//        setupViews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.miActionSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
//                Toast.makeText(SearchFragmentActivity.this, "Text Submitted: " + query, Toast.LENGTH_SHORT).show();
                if (HelperMethods.isNetworkAvailable(SearchFragmentActivity.this)) {
                    onImageSearch(query);
                } else {

                    Toast.makeText(SearchFragmentActivity.this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.miSettings) {
            Log.i("INFO", "miActionSearch called");
            FragmentManager fm = getSupportFragmentManager();
            FilterSettingsDialog filterSettingsDialog = FilterSettingsDialog.newInstance(filter);
            filterSettingsDialog.show(fm, getString(R.string.filter_title));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_search, container, false);
            setupViews(rootView);
            return rootView;
        }
    }



    private static String getUrl(){
        String query = g_query;
        String searchUrl = UrlVars.BASE_URL + query + UrlVars.SIZE + UrlVars.START + start;
        if(filter!=null){
            if(!filter.getImageColor().equals("none")){
                searchUrl += UrlVars.imgcolor+filter.getImageColor();
            }
            if(!filter.getImageSize().equals("none")){
                searchUrl += UrlVars.imgsz+filter.getImageSize();
            }
            if(!filter.getImageType().equals("none")){
                searchUrl += UrlVars.imgtype+filter.getImageType();
            }
            if(!filter.getSafeSearch().equals("none")){
                searchUrl += UrlVars.safe+filter.getSafeSearch();
            }
            if(filter.getSiteSearch()!=null) {
                if (!filter.getSiteSearch().isEmpty()) {
                    searchUrl += UrlVars.as_sitesearch + filter.getSiteSearch();
                }
            }
        }else{
            Log.i("INFO", "Filter is null");
        }
        Log.i("SEARCHURL", "Search url: " + searchUrl);
        return searchUrl;
    }

    private static boolean updateStart(){
        for(PageResult p : pageResults){
            if(Integer.parseInt(p.start) > start){
                start = Integer.parseInt(p.start);
                return true;
            }
        }
        return false;
    }

    private void onImageSearch(String query){
        g_query = query;//change
        start = 0;      //reset
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = getUrl();
        Log.i("INFO", "URL: " + searchUrl);
        client.get(searchUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJson = null;
                JSONArray pageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    pageResultsJson = response.getJSONObject("responseData").getJSONObject("cursor").getJSONArray("pages");
                    pageResults = PageResult.fromJsonArray(pageResultsJson);
                    imageResults.clear();
                    aImageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));
                }
                catch(JSONException e){
                    Toast.makeText(mContext, "No images returned.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    // Append more data into the adapter
    public static void customLoadMoreDataFromApi(int offset) {
        if(HelperMethods.isNetworkAvailable(mContext)) {
            // This method probably sends out a network request and appends new data items to your adapter.
            // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
            // Deserialize API response and then construct new objects to append to the adapter
            AsyncHttpClient client = new AsyncHttpClient();
            //get next start value to use
            if (updateStart()) {
                String searchUrl = getUrl();
                client.get(searchUrl, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray imageResultsJson = null;
                        try {
                            imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
//                    imageResults.clear(); //don't clear
                            aImageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                //do nothing we're at the end
                Toast.makeText(mContext, "No more results", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(mContext, "Network not available", Toast.LENGTH_SHORT).show();
        }

    }
}
