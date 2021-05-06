package com.example.cityboard.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cityboard.R;
import com.example.cityboard.Services.Member;
import com.example.cityboard.Services.MyAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ServicesActivity extends AppCompatActivity {

    RecyclerView servicesRecyclerView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        servicesRecyclerView = findViewById(R.id.servicesRecyclerViewID);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Member> options =
                new FirebaseRecyclerOptions.Builder<Member>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/3/listItem"), Member.class)
                        .build();

        adapter = new MyAdapter(options);
        servicesRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}