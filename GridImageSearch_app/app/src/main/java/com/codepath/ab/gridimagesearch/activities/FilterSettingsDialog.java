package com.codepath.ab.gridimagesearch.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.ab.gridimagesearch.R;
import com.codepath.ab.gridimagesearch.helpers.HelperVars;
import com.codepath.ab.gridimagesearch.models.Filters;

/**
 * Created by andrewblaich on 2/28/15.
 */
public class FilterSettingsDialog extends DialogFragment {

    private Spinner spinImageSize;
    private Spinner spinColorFilter;
    private Spinner spinImageType;
    private Spinner spinSafeSearch;
    private EditText etSiteFilter;
    private Button saveBtn;
    private Button cancelBtn;
    private RelativeLayout rLayout;

    private Filters filters;

    public interface FilterDialogListener {
        void onFinishFilterDialog(Filters filter);
    }


    public FilterSettingsDialog() {
        // Empty constructor required for DialogFragment
    }

    public static FilterSettingsDialog newInstance(Filters filters) {
        FilterSettingsDialog frag = new FilterSettingsDialog();
        Bundle args = new Bundle();
//        args.putString("title", title);
        args.putSerializable(HelperVars.INTENT_KEY_FILTER, filters);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_filter_settings, container);;
        Filters filter = (Filters)getArguments().getSerializable(HelperVars.INTENT_KEY_FILTER);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_dark);
//        getDialog().set
        getDialog().setTitle(getString(R.string.filter_title));
        setupViews(view, filter);
        return view;
    }

    private void setBackgroundColor(String match, View view){
//        RelativeLayout rLayout = (RelativeLayout)view.findViewById(R.id.fdSettings);

        if(match.equals("black"))
            rLayout.setBackgroundColor(Color.DKGRAY); //black will force me to write code to change text colors, do later
        else if(match.equals("blue"))
            rLayout.setBackgroundColor(Color.BLUE);
        else if(match.equals("brown"))
            rLayout.setBackgroundColor(Color.parseColor("#A52A2A"));
        else if(match.equals("blue"))
            rLayout.setBackgroundColor(Color.BLUE);
        else if(match.equals("gray"))
            rLayout.setBackgroundColor(Color.GRAY);
        else if(match.equals("green"))
            rLayout.setBackgroundColor(Color.GREEN);
        else if(match.equals("orange"))
            rLayout.setBackgroundColor(Color.parseColor("#FFA500"));
        else if(match.equals("pink"))
            rLayout.setBackgroundColor(Color.parseColor("#FFC0CB"));
        else if(match.equals("purple"))
            rLayout.setBackgroundColor(Color.parseColor("#800080"));
        else if(match.equals("red"))
            rLayout.setBackgroundColor(Color.RED);
        else if(match.equals("teal"))
            rLayout.setBackgroundColor(Color.parseColor("#008080"));
        else if(match.equals("white"))
            rLayout.setBackgroundColor(Color.WHITE);
        else if(match.equals("yellow"))
            rLayout.setBackgroundColor(Color.YELLOW);
        else{
            rLayout.setBackgroundColor(Color.WHITE);
        }
    }

    private void setupViews(View view, Filters filter){
        filters = new Filters();
        rLayout = (RelativeLayout)view.findViewById(R.id.fdSettings);
        saveBtn = (Button)view.findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAction();
            }
        });
        cancelBtn = (Button)view.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAction();
            }
        });

        spinImageSize = (Spinner)view.findViewById(R.id.spinnerImageSize);
        spinColorFilter = (Spinner)view.findViewById(R.id.spinnerColorFilter);
        spinColorFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    setBackgroundColor(spinColorFilter.getItemAtPosition(position).toString(), parent);
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinImageType = (Spinner)view.findViewById(R.id.spinnerImageType);
        spinSafeSearch = (Spinner)view.findViewById(R.id.spinnerSafeSearch);
        etSiteFilter = (EditText)view.findViewById(R.id.etSiteFilter);

//        RelativeLayout rLayout = (RelativeLayout)view.findViewById(R.id.fdSettings);


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
//                            rLayout.setBackgroundColor(Color.parseColor("#000000"));
                            setBackgroundColor(filter.getImageColor(), view);
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

                    etSiteFilter.setText(filter.getSiteSearch());
                }


    }

    private void saveAction(){
        Log.i("INFO", "Save Action called");
        filters.setImageSize(spinImageSize.getSelectedItem().toString());
        filters.setImageColor(spinColorFilter.getSelectedItem().toString());
        filters.setImageType(spinImageType.getSelectedItem().toString());
        filters.setSafeSearch(spinSafeSearch.getSelectedItem().toString());
        filters.setSiteSearch(etSiteFilter.getText().toString());

        FilterDialogListener listener = (FilterDialogListener) getActivity();
        listener.onFinishFilterDialog(filters);

        dismiss();
    }

    private void cancelAction(){
        Log.i("INFO", "Cancel Action called");
        dismiss();
    }
}
