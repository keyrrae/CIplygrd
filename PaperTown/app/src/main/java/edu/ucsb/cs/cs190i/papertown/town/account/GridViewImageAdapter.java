/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.account;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.R;
import edu.ucsb.cs.cs190i.papertown.models.Town;

/**
 * Created by Zhenyu on 2017-06-03.
 */

public class GridViewImageAdapter extends BaseAdapter {
    private Context mContext;
    public Integer[] mThumbIds;
    private ArrayList<Uri> uriList;
    public List<Town> towns = new ArrayList<>();
    // Constructor
    public GridViewImageAdapter(Context c,  List<Town> townsIn) {
        mContext = c;
        towns = townsIn;
    }

    public int getCount() {
        return towns.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout item;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            item = (RelativeLayout) inflater.inflate(R.layout.grid_account_item, null, true);
            ImageView image = (ImageView) item.findViewById(R.id.grid_item_image);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if(this.towns.get(position).getImageUrls()!=null&&this.towns.get(position).getImageUrls().size()>0&&this.towns.get(position).getImageUrls().get(0)!=null&&!this.towns.get(position).getImageUrls().get(0).isEmpty()) {
                String imagePath = this.towns.get(position).getImageUrls().get(0);
                Picasso.with(image.getContext()).load(imagePath)
                        .error(R.drawable.dummyimage)
                        .placeholder(R.drawable.dummyimage)
                        .into(image);
            }
            else{
                Picasso.with(image.getContext()).load(R.drawable.dummyimage)
                        .error(R.drawable.dummyimage)
                        .placeholder(R.drawable.dummyimage)
                        .into(image);
            }

            TextView text = (TextView) item.findViewById(R.id.grid_item_title);
            text.setText(this.towns.get(position).getTitle());
        }
        else {
            item  = (RelativeLayout) convertView;
        }
        return item;
    }
}
