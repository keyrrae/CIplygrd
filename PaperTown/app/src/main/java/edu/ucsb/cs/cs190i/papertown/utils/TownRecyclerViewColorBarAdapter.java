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

public class TownRecyclerViewColorBarAdapter extends RecyclerView.Adapter<TownRecyclerViewColorBarAdapter.CustomViewHolder> {
    private List<Town> feedItemList;
    private Context mContext;

    private List<Boolean> selectionColorBar = new ArrayList<>();

    private CardView cardView;

    public TownRecyclerViewColorBarAdapter(Context context, List<Town> feedItemList) {
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
    public TownRecyclerViewColorBarAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.geo_single_town, null);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.geo_single_town,viewGroup,false);
        TownRecyclerViewColorBarAdapter.CustomViewHolder viewHolder = new TownRecyclerViewColorBarAdapter.CustomViewHolder(view);

        cardView = (CardView) view.findViewById(R.id.geo_card);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TownRecyclerViewColorBarAdapter.CustomViewHolder customViewHolder, int i) {
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



        List<String> likes = UserSingleton.getInstance().getLikes();
        for(int j = 0 ; j<likes.size();j++){
            if(likes.get(j).equals(feedItem.getId())){
                customViewHolder.imageButton.setImageResource(R.drawable.ic_favorite_white_18dp);
                break;
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
        }
    }
}