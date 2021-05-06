package com.example.cityboard.chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cityboard.databinding.ActivityChatBinding;
import com.example.cityboard.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    //in this chat activity we will show chats
    //so we need adapaters and arraylist
    MessagesAdapter adapter;
    ArrayList<Message> messages;

    String senderRoom;
    String receiverRoom;
    String senderUid, receiverUid;

    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(this,messages);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this)); //we can also do this in xml
        binding.recyclerView.setAdapter(adapter);

        String name = getIntent().getStringExtra("name");
        receiverUid = getIntent().getStringExtra("uid");
        senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid; //this is string concat
        receiverRoom = receiverUid + senderUid;

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        //time to read chats
        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear(); //messages is the arraylist, we clear previous buffer
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Message message = snapshot1.getValue(Message.class);
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //binding.recyclerView.setAdapter(adapter);

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageTxt = binding.messageBox.getText().toString();
                Date date = new Date();
                Message message = new Message(messageTxt, senderUid, date.getTime()); //message, senderId, timestamp
                binding.messageBox.setText("");

                //we want the random key that push() generates to be same for sender and receiver
                String randomkey = database.getReference().push().getKey();

                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());

                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);
                //getting last message and time above before anything else
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomkey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) //onsuccess sending the same thing on receiver's end
                    {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(randomkey)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });


        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChatActivity.this, "select photos from gallery", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);
            }
        });
        getSupportActionBar().setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); ///this will enable going back
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 25)
        {
            if(data != null)
            {
                if(data.getData()!=null)
                {
                    Uri selectedImage = data.getData(); //storing the path of user selected image's storage
                    //we will create a new node chats, and that node need unique nodes inside
                    //for that we'll use time from app in miliseconds
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis()+"");
                    dialog.show(); //uploading image.. progress dialog
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filepath = uri.toString();

                                        //copy from sendBtn
                                        String messageTxt = binding.messageBox.getText().toString();
                                        Date date = new Date();
                                        Message message = new Message(messageTxt, senderUid, date.getTime()); //message, senderId, timestamp
                                        message.setMessage("photo");
                                        message.setImageUrl(filepath);
                                        binding.messageBox.setText("");

                                        //we want the random key that push() generates to be same for sender and receiver
                                        String randomkey = database.getReference().push().getKey();

                                        HashMap<String, Object> lastMsgObj = new HashMap<>();
                                        lastMsgObj.put("lastMsg", message.getMessage());
                                        lastMsgObj.put("lastMsgTime", date.getTime());

                                        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                                        database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);
                                        //getting last message and time above before anything else
                                        database.getReference().child("chats")
                                                .child(senderRoom)
                                                .child("messages")
                                                .child(randomkey)
                                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) //onsuccess sending the same thing on receiver's end
                                            {
                                                database.getReference().child("chats")
                                                        .child(receiverRoom)
                                                        .child("messages")
                                                        .child(randomkey)
                                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });
                                            }
                                        });

                                        //Toast.makeText(ChatActivity.this, filepath, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });


                }
            }
        }
    }

    //if you click back_arrow on top, this will finish current activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}