package com.example.cityboard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityboard.DescActivity;
import com.example.cityboard.Interface.IItemClickListener;
import com.example.cityboard.Model.ItemData;
import com.example.cityboard.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.MyViewHolder> {

    private Context context;
    private List<ItemData> itemDataList;

    public MyItemAdapter(Context context, List<ItemData> itemDataList) {
        this.context = context;
        this.itemDataList = itemDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        String name = itemDataList.get(i).getName();
        String image = itemDataList.get(i).getImage();
        myViewHolder.txt_item_title.setText(name);
        Picasso.get().load(image).into(myViewHolder.img_item);

        myViewHolder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Toast.makeText(context, ""+itemDataList.get(i).getName(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("name",itemDataList.get(i).getName());
                bundle.putString("image",itemDataList.get(i).getImage());
                bundle.putString("price",itemDataList.get(i).getPrice());
                bundle.putString("pTime",itemDataList.get(i).getpTime());
                bundle.putString("pDescription",itemDataList.get(i).getpDescription());
                bundle.putString("uName",itemDataList.get(i).getuName());
                bundle.putString("uEmail",itemDataList.get(i).getuEmail());
                bundle.putString("uid",itemDataList.get(i).getUid());
                bundle.putString("pId",itemDataList.get(i).getpId());
                Intent descIntent = new Intent(context, DescActivity.class);
                descIntent.putExtras(bundle);
                context.startActivity(descIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (itemDataList.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_item_title;
        ImageView img_item;

        IItemClickListener iItemClickListener;

        public void setiItemClickListener(IItemClickListener iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_item_title = (TextView)itemView.findViewById(R.id.tvTitleID);
            img_item = (ImageView)itemView.findViewById(R.id.itemImageID);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iItemClickListener.onItemClickListener(v, getAdapterPosition());
        }
    }
}
