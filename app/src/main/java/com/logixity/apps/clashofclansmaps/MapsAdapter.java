package com.logixity.apps.clashofclansmaps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ahmed on 16/07/2017.
 */

public class MapsAdapter extends RecyclerView.Adapter<MapsAdapter.ViewHolder> {


    List<MapModel> mapsList;
    Context context;

    public MapsAdapter(List<MapModel> mapsList, Context context) {
        super();
        this.mapsList = mapsList;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MapModel model = mapsList.get(position);

        holder.mapImage.setImageResource(model.getMapImage());
        holder.mapName.setText(model.getMapName());
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,MapViewActivity.class);
                i.putExtra("mapImage",model.getMapImage());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mapsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mapImage;
        public TextView mapName;
        public Button viewBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            mapImage = (ImageView) itemView.findViewById(R.id.mapImage);
            mapName = (TextView) itemView.findViewById(R.id.mapName);
            viewBtn = (Button) itemView.findViewById(R.id.viewMapBtn);
        }
    }
}
