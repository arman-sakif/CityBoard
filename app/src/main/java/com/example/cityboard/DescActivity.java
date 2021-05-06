package com.example.cityboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cityboard.chats.ChatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DescActivity extends AppCompatActivity {

    ImageView imageView;
    TextView pNameTV, priceTV, timeTV, descTV, uNameTV;
    FloatingActionButton floatingActionButton;

    String userId, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);

        imageView = findViewById(R.id.descImageViewID);
        pNameTV = findViewById(R.id.descTextViewID);
        priceTV = findViewById(R.id.descPriceTextViewID);
        timeTV = findViewById(R.id.descTimeID);
        descTV = findViewById(R.id.descDetailsID);
        uNameTV = findViewById(R.id.descUserNameID);


       /* Intent intent = getIntent();
        String item = intent.getStringExtra("item");
        Toast.makeText(this, ""+item, Toast.LENGTH_SHORT).show();*/
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            String name = bundle.getString("name");
            String image = bundle.getString("image");
            String price = bundle.getString("price");
            String pTime = bundle.getString("pTime");
            String pDescription = bundle.getString("pDescription");
            String uName = bundle.getString("uName");
            String uEmail = bundle.getString("uEmail");
            String uid = bundle.getString("uid");
            userId = uid;
            username = uName;
            String pId = bundle.getString("pId");



            pNameTV.setText(name);
            priceTV.setText("Price: "+price+" TK");
            timeTV.setText("Posted on: "+pTime);
            descTV.setText(pDescription);
            uNameTV.setText(uName);
            Glide.with(this)
                    .load(image)
                    .into(imageView);
        }


        floatingActionButton =(FloatingActionButton) findViewById(R.id.descFloatButtonID);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("global name: "+username);
                //System.out.println("global id: "+userId);

                Intent intent = new Intent(DescActivity.this, ChatActivity.class);
                intent.putExtra("name", username);
                intent.putExtra("uid", userId);
                startActivity(intent);
            }
        });

    }
}