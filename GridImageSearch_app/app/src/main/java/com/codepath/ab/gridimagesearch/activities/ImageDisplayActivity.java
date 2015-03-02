package com.codepath.ab.gridimagesearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.ab.gridimagesearch.R;
import com.codepath.ab.gridimagesearch.extras.TouchImageView;
import com.codepath.ab.gridimagesearch.helpers.HelperMethods;
import com.codepath.ab.gridimagesearch.helpers.HelperVars;
import com.codepath.ab.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDisplayActivity extends ActionBarActivity {

    private ShareActionProvider miShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
//        getSupportActionBar().hide();
        if(HelperMethods.isNetworkAvailable(this)) {
            ImageResult image = (ImageResult) getIntent().getSerializableExtra(HelperVars.INTENT_KEY_RESULT);
            TouchImageView ivImageResult = (TouchImageView) findViewById(R.id.ivImageresult);
            Picasso.with(this).load(image.fullUrl).placeholder(R.mipmap.ic_launcher).into(ivImageResult, new Callback() {
                @Override
                public void onSuccess() {
                    // Setup share intent now that image has loaded
                    setupShareIntent();
                }

                @Override
                public void onError() {
                    // ...
                }
            });
        }else{
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }
    }

    public void setupShareIntent() {
        // Fetch Bitmap Uri locally
        try {
            ImageView ivImage = (ImageView) findViewById(R.id.ivImageresult);
            Uri bmpUri = getLocalBitmapUri(ivImage); // see previous remote images section
            // Create share intent as described above
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Attach share event to the menu item provider
            miShareAction.setShareIntent(shareIntent);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        // Return true to display menu
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
