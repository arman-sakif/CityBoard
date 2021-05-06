package com.example.cityboard.Adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cityboard.HomePageActivity;
import com.example.cityboard.Items.ItemsActivity;
import com.example.cityboard.Model.ItemData;
import com.example.cityboard.Model.ItemGroup;
import com.example.cityboard.R;
import com.example.cityboard.Resident.ResidentActivity;
import com.example.cityboard.Services.ServicesActivity;
import com.example.cityboard.Teaching.TeachingActivity;


import java.util.List;

public class MyItemGroupAdapter extends RecyclerView.Adapter<MyItemGroupAdapter.MyViewHolder> {

    private Context context;
    private List<ItemGroup> dataList;

    public MyItemGroupAdapter(Context context, List<ItemGroup> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_group, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        myViewHolder.item_title.setText(dataList.get(position).getHeaderTitle());
        List<ItemData> itemData = dataList.get(position).getListItem();
        MyItemAdapter itemListAdapter = new MyItemAdapter(context,itemData);
        myViewHolder.recycler_view_item_list.setHasFixedSize(true);
        myViewHolder.recycler_view_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        myViewHolder.recycler_view_item_list.setAdapter(itemListAdapter);

        myViewHolder.recycler_view_item_list.setNestedScrollingEnabled(false);

        myViewHolder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Button more: "+myViewHolder.item_title.getText(), Toast.LENGTH_SHORT).show();
                String category = (String) myViewHolder.item_title.getText();
                if(category.equals("Resident")){
                    Intent intent = new Intent(context, ResidentActivity.class);
                    context.startActivity(intent);
                }
                if(category.equals("Teaching")){
                    Intent intent = new Intent(context, TeachingActivity.class);
                    context.startActivity(intent);
                }
                if(category.equals("Items")){
                    Intent intent = new Intent(context, ItemsActivity.class);
                    context.startActivity(intent);
                }
                if(category.equals("Services")){
                    Intent intent = new Intent(context, ServicesActivity.class);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dataList != null ? dataList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView item_title;
        RecyclerView recycler_view_item_list;
        Button moreButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.itemTitleID);
            moreButton = (Button) itemView.findViewById(R.id.moreButtonID);
            recycler_view_item_list = (RecyclerView) itemView.findViewById(R.id.recyclerViewListID);
        }
    }
}
