package com.example.cityboard.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cityboard.CreatePostActivity;
import com.example.cityboard.HomePageActivity;
import com.example.cityboard.R;
import com.example.cityboard.Search.SearchActivity;
import com.example.cityboard.SetupProfileActivity;
import com.example.cityboard.databinding.ActivityMainBinding;
import com.example.cityboard.databinding.ActivityMessagingBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MessagingActivity extends AppCompatActivity {

    ActivityMessagingBinding binding;
    FirebaseDatabase database;
    ArrayList<User> users;
    UsersAdapter usersAdapter;
    ProgressDialog dialog;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    //ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.bottomNavID);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setSelectedItemId(R.id.appBarChatID);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });

        floatingActionButton =(FloatingActionButton) findViewById(R.id.msgFloatButtonID);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SearchActivity.this, "FAB", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MessagingActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(this, users);
        binding.recyclerView.setAdapter(usersAdapter);

        //add a pd here
        dialog = new ProgressDialog(this);
        dialog.setMessage("Getting Chat Profiles...");
        dialog.setCancelable(false);
        dialog.show();

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);
                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid())) //the current user should not see himself
                        users.add(user);
                }
                //will dissmiss proggres diag here
                dialog.dismiss();
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.finish();
        Intent intent = new Intent(this, HomePageActivity.class);
        this.startActivity(intent);
    }

    private void myClickItem(MenuItem item){
        switch(item.getItemId()){
            case R.id.appBarChatID:
                Toast.makeText(this, "Chat", Toast.LENGTH_SHORT).show();
                break;
            case R.id.appBarSearchID:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                super.finish();
                Intent searchIntent = new Intent(this, SearchActivity.class);
                this.startActivity(searchIntent);
                break;
            case R.id.appBarProfileID:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                super.finish();
                Intent profileIntent = new Intent(this, SetupProfileActivity.class);
                this.startActivity(profileIntent);
                break;
            case R.id.appBarHomeID:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                super.finish();
                Intent intent = new Intent(this, HomePageActivity.class);
                this.startActivity(intent);
                break;
        }
    }



    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.invite:
                Toast.makeText(this, "Invite", Toast.LENGTH_SHORT).show();
                break;
            case R.id.groups:
                Toast.makeText(this, "Groups", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/
}