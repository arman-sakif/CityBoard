package com.example.cityboard.Resident;

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

import com.bumptech.glide.Glide;
import com.example.cityboard.DescActivity;
import com.example.cityboard.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAdapter extends FirebaseRecyclerAdapter<Member,MyAdapter.MyViewHolder> {

    /*Context context;

    public MyAdapter(@NonNull FirebaseRecyclerOptions<Member> options, Context context) {
        super(options);
        this.context = context;
    }*/

    public MyAdapter(@NonNull FirebaseRecyclerOptions<Member> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Member model) {


        holder.mTitleTV.setText(model.getName());
        holder.mPriceTV.setText("Price: "+model.getPrice()+" TK");
        holder.mTimeTV.setText("Posted on: "+model.getpTime());
        Glide.with(holder.mImageIV.getContext()).load(model.getImage()).into(holder.mImageIV);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.image_resident,parent,false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageIV;
        TextView mTitleTV;
        TextView mPriceTV;
        TextView mTimeTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageIV = itemView.findViewById(R.id.residentImageViewID);
            mTitleTV = itemView.findViewById(R.id.residentTitleTextViewID);
            mPriceTV = itemView.findViewById(R.id.residentPriceTextView);
            mTimeTV = itemView.findViewById(R.id.residentTimeTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Toast.makeText(this.itemView.getContext(), "Position"+position, Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString("name",getItem(position).getName());
            bundle.putString("image",getItem(position).getImage());
            bundle.putString("price",getItem(position).getPrice());
            bundle.putString("pTime",getItem(position).getpTime());
            bundle.putString("pDescription",getItem(position).getpDescription());
            bundle.putString("uName",getItem(position).getuName());
            bundle.putString("uEmail",getItem(position).getuEmail());
            bundle.putString("uid",getItem(position).getUid());
            bundle.putString("pId",getItem(position).getpId());

            Intent descIntent = new Intent(this.itemView.getContext(), DescActivity.class);
            //descIntent.putExtra("item", getItem(position).getName());
            descIntent.putExtras(bundle);
            this.itemView.getContext().startActivity(descIntent);
        }
    }


   /* public void setDetails(Context context, String title, String image){

        TextView mTitleTV = (TextView) view.findViewById(R.id.residentTitleTextViewID);
        ImageView mImageIV = (ImageView) view.findViewById(R.id.residentImageViewID);

        mTitleTV.setText(title);
        Picasso.get().load(image).into(mImageIV);

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        itemView.startAnimation(animation);
    }*/

}
