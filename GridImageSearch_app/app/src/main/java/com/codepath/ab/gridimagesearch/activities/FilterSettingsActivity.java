package com.codepath.ab.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.codepath.ab.gridimagesearch.R;
import com.codepath.ab.gridimagesearch.helpers.HelperVars;
import com.codepath.ab.gridimagesearch.models.Filters;

public class FilterSettingsActivity extends ActionBarActivity {

    private Spinner spinImageSize;
    private Spinner spinColorFilter;
    private Spinner spinImageType;
    private Spinner spinSafeSearch;

    private Filters filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_settings);
        Intent i = getIntent();
        setupViews(i);
    }

    private void setupViews(Intent i){
        filters = new Filters();
        spinImageSize = (Spinner)findViewById(R.id.spinnerImageSize);
//        spinColorFilter = (Spinner)findViewById(R.id.spinnerColorFilter);
//        spinImageType = (Spinner)findViewById(R.id.spinnerImageType);
//        spinSafeSearch = (Spinner)findViewById(R.id.spinnerSafeSearch);

//        Intent i = getIntent();
        if(i!=null) {
            if (i.getExtras().containsKey(HelperVars.INTENT_KEY_FILTER)) {
                Filters filter = (Filters) i.getSerializableExtra(HelperVars.INTENT_KEY_FILTER);
                if (filter != null) {
                    for (int j = 0; j < spinImageSize.getCount(); j++) {
                        if (spinImageSize.getItemAtPosition(j).toString().equals(filter.getImageSize())) {
                            spinImageSize.setSelection(j);
                            break;
                        }
                    }

                    for (int j = 0; j < spinColorFilter.getCount(); j++) {
                        if (spinColorFilter.getItemAtPosition(j).toString().equals(filter.getImageColor())) {
                            spinColorFilter.setSelection(j);
                            break;
                        }
                    }

                    for (int j = 0; j < spinImageType.getCount(); j++) {
                        if (spinImageType.getItemAtPosition(j).toString().equals(filter.getImageType())) {
                            spinImageType.setSelection(j);
                            break;
                        }
                    }

                    for (int j = 0; j < spinSafeSearch.getCount(); j++) {
                        if (spinSafeSearch.getItemAtPosition(j).toString().equals(filter.getSafeSearch())) {
                            spinSafeSearch.setSelection(j);
                            break;
                        }
                    }
                }
            }
        }


    }

    public void saveAction(View v){

        Log.i("INFO", "Save Action called");

        filters.setImageSize(spinImageSize.getSelectedItem().toString());
        filters.setImageColor(spinColorFilter.getSelectedItem().toString());
        filters.setImageType(spinImageType.getSelectedItem().toString());
        filters.setSafeSearch(spinSafeSearch.getSelectedItem().toString());

        Intent res = new Intent();
        res.putExtra(HelperVars.INTENT_KEY_FILTER, filters);
        setResult(RESULT_OK, res);

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
