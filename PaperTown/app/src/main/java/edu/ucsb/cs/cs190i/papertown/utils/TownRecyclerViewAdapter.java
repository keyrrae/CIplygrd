/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.UserSingleton;

/**
 * Created by Zhenyu on 2017-06-11.
 */

public class TownRecyclerViewAdapter extends RecyclerView.Adapter<TownRecyclerViewAdapter.CustomViewHolder> {
    private List<Town> feedItemList;
    private Context mContext;

    private List<Boolean> selectionColorBar = new ArrayList<>();

    private CardView cardView;

    public TownRecyclerViewAdapter(Context context, List<Town> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;

        for(int i = 0 ; i < feedItemList.size();i++){
            selectionColorBar.add(false);
        }
    }

    public void setSelectionColorBar(int index){
        for(int i = 0 ; i < selectionColorBar.size();i++){
            selectionColorBar.set(i,false);
        }
        //selectionColorBar.set(index,true);
        notifyDataSetChanged();
    }

    @Override
    public TownRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.geo_single_town, null);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.geo_single_town,viewGroup,false);
        TownRecyclerViewAdapter.CustomViewHolder viewHolder = new TownRecyclerViewAdapter.CustomViewHolder(view);

        cardView = (CardView) view.findViewById(R.id.geo_card);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TownRecyclerViewAdapter.CustomViewHolder customViewHolder, int i) {
        Town feedItem = feedItemList.get(i);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;
        Log.i("TAG","customViewHolder.cardView = "+cardView);
        Log.i("TAG","customViewHolder.cardView = "+cardView.getLayoutParams());
        Log.i("TAG","customViewHolder.cardView = "+cardView.getLayoutParams().width);
        cardView.getLayoutParams().width = (int)(width*0.6f);

        if(selectionColorBar.get(i)) {
            customViewHolder.geo_town_pick_bar.setBackgroundColor(Color.BLUE);
        }
        else{
            customViewHolder.geo_town_pick_bar.setBackgroundColor(Color.TRANSPARENT);
        }

        //Render image using Picasso library
        Picasso.with(mContext).load(feedItem.getImageUrls().get(0))
                .error(R.drawable.dummyimage)
                .placeholder(R.drawable.dummyimage)
                .into(customViewHolder.imageView);
        //customViewHolder.geo_town_pick_bar.setBackgroundColor(Color.TRANSPARENT);
        //Picasso.with(imageView.getContext()).load(Uri.parse(towns.get(position).getImageUrls().get(0))).into(imageView);

        //Setting text view title
        customViewHolder.textView.setText(feedItem.getTitle());
        customViewHolder.categoryTextView.setText(feedItem.getCategory());
        customViewHolder.countTextView.setText(feedItem.getNumOfLikes()+" likes");





        List<String> likes = UserSingleton.getInstance().getLikes();
        for(int j = 0 ; j<likes.size();j++){
            if(likes.get(j).equals(feedItem.getId())){
                customViewHolder.imageButton.setImageResource(R.drawable.ic_favorite_white_18dp);
            }
            else{
                customViewHolder.imageButton.setImageResource(R.drawable.ic_favorite_border_white_18dp);
            }
        }

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private TextView categoryTextView;
        private TextView countTextView;
        private ImageView geo_town_pick_bar;
        private Context context;
        private ImageButton imageButton;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.geo_town_image);
            this.geo_town_pick_bar = (ImageView) view.findViewById(R.id.geo_town_pick_bar);
            this.textView = (TextView) view.findViewById(R.id.geo_town_title);
            this.categoryTextView = (TextView) view.findViewById(R.id.geo_town_category);
            this.countTextView = (TextView) view.findViewById(R.id.geo_town_count);
            this.imageButton = (ImageButton) view.findViewById(R.id.geo_town_visit_button);
        }
    }
}