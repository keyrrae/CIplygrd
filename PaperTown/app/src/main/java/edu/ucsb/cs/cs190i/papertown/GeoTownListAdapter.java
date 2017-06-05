/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.ucsb.cs.cs190i.papertown.models.Town;

/**
 * Created by EYE on 07/05/2017.
 */

public class GeoTownListAdapter extends RecyclerView.Adapter<GeoTownListAdapter.GeoTownListViewHolder>{
    private List<Town> towns;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView categoryTextView;
    private TextView countTextView;
    private CardView cardView;
    private Context context;

    public class GeoTownListViewHolder extends RecyclerView.ViewHolder{
        public GeoTownListViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.geo_town_image);
            titleTextView = (TextView) itemView.findViewById(R.id.geo_town_title);
            categoryTextView = (TextView) itemView.findViewById(R.id.geo_town_category);
            countTextView = (TextView) itemView.findViewById(R.id.geo_town_count);
            cardView = (CardView) itemView.findViewById(R.id.geo_card);
        }
    }

    public void setTowns(List<Town> towns){
        this.towns = towns;
        notifyDataSetChanged();
    }

    public GeoTownListAdapter(List<Town> towns, Context contextInput){
        this.towns = towns;
        this.context = contextInput;
    }

    @Override
    public GeoTownListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.geo_single_town,parent,false);
        GeoTownListViewHolder holder = new GeoTownListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GeoTownListViewHolder holder, int position) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;
        cardView.getLayoutParams().width = (int)(width*0.6f);

        Picasso.with(imageView.getContext()).load(Uri.parse(towns.get(position).getImageUrls().get(0))).into(imageView);

        titleTextView.setText(towns.get(position).getTitle());
        categoryTextView.setText(towns.get(position).getCategory());
        countTextView.setText(""+towns.get(position).getNumOfLikes());
    }

    @Override
    public int getItemCount() {
        return towns.size();
    }


}
