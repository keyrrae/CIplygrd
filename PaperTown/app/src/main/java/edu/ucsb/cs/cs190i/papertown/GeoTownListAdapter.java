package edu.ucsb.cs.cs190i.papertown;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.ucsb.cs.cs190i.papertown.models.Town;

public class GeoTownListAdapter extends RecyclerView.Adapter<GeoTownListAdapter.GeoTownListViewHolder>{
    public List<Town> towns;

    public class GeoTownListViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView titleTextView;
        public TextView categoryTextView;

        public GeoTownListViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.geo_town_image);
            titleTextView = (TextView) itemView.findViewById(R.id.geo_town_title);
            categoryTextView = (TextView) itemView.findViewById(R.id.geo_town_category);
        }
    }

    public GeoTownListAdapter(List<Town> towns){
        this.towns = towns;
    }

    @Override
    public GeoTownListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.geo_single_town,parent,false);
        GeoTownListViewHolder holder = new GeoTownListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GeoTownListViewHolder holder, int position) {
//        holder.imageView.setImageURI(Uri.parse(towns.get(position).getImageUrls().get(0)));
        Picasso.with(holder.imageView.getContext()).load(Uri.parse(towns.get(position).getImageUrls().get(0))).into(holder.imageView);

        holder.titleTextView.setText(towns.get(position).getTitle());
        holder.categoryTextView.setText(towns.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return towns.size();
    }


}
