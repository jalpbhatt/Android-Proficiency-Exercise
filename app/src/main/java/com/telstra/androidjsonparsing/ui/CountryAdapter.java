package com.telstra.androidjsonparsing.ui;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telstra.androidjsonparsing.R;
import com.telstra.androidjsonparsing.imagecache.ImageLoader;
import com.telstra.androidjsonparsing.model.CountryDetails;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private Context mContext;
    private ImageLoader mImageLoader;
    private ArrayList<CountryDetails> mCountyDetailsList;


    public CountryAdapter(ArrayList<CountryDetails> android, Context context) {

        this.mCountyDetailsList = android;
        this.mContext = context;

        if (mContext != null) {
            mImageLoader = new ImageLoader(mContext.getApplicationContext());
        }
    }

    @Override
    public CountryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryAdapter.ViewHolder viewHolder, int i) {

        String title, desc, imageUrl;

        title = mCountyDetailsList.get(i).getTitle();
        desc = mCountyDetailsList.get(i).getDesc();
        imageUrl = mCountyDetailsList.get(i).getImageRef();

        if (!TextUtils.isEmpty(title)) {
            viewHolder.tv_title.setText(title);
        } else {
            // display default message
            viewHolder.tv_title.setText(R.string.default_msg_no_title);
        }

        if (!TextUtils.isEmpty(desc)) {
            viewHolder.tv_desc.setText(desc);
        } else {
            // display default message
            viewHolder.tv_desc.setText(R.string.default_msg_no_desc);
        }

        Log.e(getClass().getName(), "Title = " + title + " URL = " + imageUrl);

        // Load image from URL to dedicated list view item
        mImageLoader.displayImage(imageUrl, viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return mCountyDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title, tv_desc;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            imageView = (ImageView) view.findViewById(R.id.list_row_image);
        }
    }

}