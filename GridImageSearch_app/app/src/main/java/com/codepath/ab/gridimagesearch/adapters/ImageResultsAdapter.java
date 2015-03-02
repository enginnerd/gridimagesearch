package com.codepath.ab.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.ab.gridimagesearch.R;
import com.codepath.ab.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by andrewblaich on 2/26/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    private static class ViewHolder {
        TextView tvTitle;
        ImageView ivImage;
    }

    public ImageResultsAdapter(Context context, List<ImageResult> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
//        return super.getView(position, convertView, parent);
        final ViewHolder viewHolder;
        ImageResult imageInfo = getItem(position);
        if(convertView==null){
            viewHolder =  new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_results, parent, false);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);
        viewHolder.tvTitle.setText(Html.fromHtml(imageInfo.title));
        Picasso.with(getContext()).load(imageInfo.thumbUrl).fit().centerCrop().placeholder(R.mipmap.ic_launcher).into(viewHolder.ivImage);

        return convertView;
    }
}
