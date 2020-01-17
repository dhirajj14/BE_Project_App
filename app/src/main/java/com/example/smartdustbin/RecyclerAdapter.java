package com.example.smartdustbin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHoder> {
    String area;
    String distance_filled;
    String dustbin_id;
    List<Dustbin_status_items> list;
    Context context;

    public RecyclerAdapter(List<Dustbin_status_items> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.ds_card_layout, parent, false);
        MyHoder myHoder = new MyHoder(view);


        return myHoder;
    }

    @Override
    public void onBindViewHolder(final MyHoder holder, final int position) {
        Dustbin_status_items mylist = list.get(position);
        holder.area.setText(mylist.area);
        holder.distance.setText("Currently dustbin is "+mylist.getDistance()+ "% filled");
        holder.dustbin_fill = mylist.distance;
        holder.id = mylist.id;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView a =(TextView) holder.itemView.findViewById(R.id.ds_text_heading);
                area = a.getText().toString();
                distance_filled = holder.dustbin_fill;
                dustbin_id = holder.id;
                Dustbin_status ds = new Dustbin_status();
                Intent detail_status_view = new Intent(view.getContext(), Detail_status.class);
                detail_status_view.putExtra("area_name",area);
                detail_status_view.putExtra("dustbin_filled",distance_filled);
                detail_status_view.putExtra("id",dustbin_id);
                view.getContext().startActivity(detail_status_view);
            }
        });

    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try {
            if (list.size() == 0) {

                arr = 0;

            } else {

                arr = list.size();
            }


        } catch (Exception e) {


        }

        return arr;

    }

    class MyHoder extends RecyclerView.ViewHolder {
        TextView area, distance;
        Button cb;
        String dustbin_fill,id;


        public MyHoder(View itemView) {
            super(itemView);
            area = (TextView) itemView.findViewById(R.id.ds_text_heading);
            distance = (TextView) itemView.findViewById(R.id.ds_text_percentage);
        }
    }
}