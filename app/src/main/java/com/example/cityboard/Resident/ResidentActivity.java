package com.example.cityboard.Resident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cityboard.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ResidentActivity extends AppCompatActivity {

    RecyclerView residentRecyclerView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident);

        residentRecyclerView = findViewById(R.id.residentRecyclerViewID);
        residentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Member> options =
                new FirebaseRecyclerOptions.Builder<Member>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/0/listItem"), Member.class)
                        .build();

        adapter = new MyAdapter(options);
        residentRecyclerView.setAdapter(adapter);

    }

    /*@Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Member, ViewHolder>firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Member, ViewHolder>(
                        Member.class,
                        R.layout.image,
                        ViewHolder.class,
                        reference

                ) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {

                    }

                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return null;
                    }
                };
    }*/

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