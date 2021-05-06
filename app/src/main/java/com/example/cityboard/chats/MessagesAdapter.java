package com.example.cityboard.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityboard.R;
import com.example.cityboard.databinding.ItemReceiveBinding;
import com.example.cityboard.databinding.ItemSentBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter{

    Context context;
    ArrayList<Message> messages; ///Inside the chat we'll show conversation, so we need arralist of messages

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    public MessagesAdapter(Context context, ArrayList<Message> messages){
        this.context = context;
        this.messages = messages;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent,parent,false);
            return new SentViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false);
            return new ReceiverViewHolder(view);
        }
        //return null; removed
    }
    //we have two view type, so we'll use a logic to select which view type
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        //if the message sender ID and current user's (who's using) ID same, then message sending is happening
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return ITEM_SENT;
        }else{
            return ITEM_RECEIVE;
        }
        //return super.getItemViewType(position); removed
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position); //where our bindviewholder has reached, we get messages of that point
        if(holder.getClass() == SentViewHolder.class){
            SentViewHolder viewHolder = (SentViewHolder)holder; //typecasting
            if(message.getMessage().equals("photo")){
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE); //now image shows, message gone
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.image);

            }
            viewHolder.binding.message.setText(message.getMessage());
        }else{
            ReceiverViewHolder viewHolder = (ReceiverViewHolder)holder; //typecasting
            if(message.getMessage().equals("photo")){
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE); //now image shows, message gone
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(viewHolder.binding.image);

            }
            viewHolder.binding.message.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        //if(messages!=null)
        return messages.size();
        //else return 0;
    }

    public class SentViewHolder extends RecyclerView.ViewHolder{

        ItemSentBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSentBinding.bind(itemView);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        ItemReceiveBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }
}

