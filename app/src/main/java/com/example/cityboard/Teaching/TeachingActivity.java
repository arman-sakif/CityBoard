package com.example.cityboard.Teaching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cityboard.R;
import com.example.cityboard.Teaching.Member;
import com.example.cityboard.Teaching.MyAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class TeachingActivity extends AppCompatActivity {

    RecyclerView teachingRecyclerView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching);

        teachingRecyclerView = findViewById(R.id.teachingRecyclerViewID);
        teachingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<com.example.cityboard.Teaching.Member> options =
                new FirebaseRecyclerOptions.Builder<com.example.cityboard.Teaching.Member>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/1/listItem"), Member.class)
                        .build();

        adapter = new MyAdapter(options);
        teachingRecyclerView.setAdapter(adapter);
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