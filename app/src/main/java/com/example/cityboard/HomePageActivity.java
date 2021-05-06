package com.example.cityboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cityboard.Adapter.MyItemGroupAdapter;
import com.example.cityboard.Interface.IFirebaseLoadListener;
import com.example.cityboard.Model.ItemData;
import com.example.cityboard.Model.ItemGroup;
import com.example.cityboard.Resident.ResidentActivity;
import com.example.cityboard.Search.SearchActivity;
import com.example.cityboard.chats.MessagingActivity;
import com.example.cityboard.chats.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class HomePageActivity extends AppCompatActivity implements IFirebaseLoadListener, BottomNavigationView.OnNavigationItemReselectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    AlertDialog dialog;
    IFirebaseLoadListener iFirebaseLoadListener;

    RecyclerView my_recycler_view;

    DatabaseReference myData;

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    FirebaseDatabase database;
    FirebaseAuth auth;
    ImageView navProfileImage;
    TextView navUsername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home_page);

      //  setContentView(R.layout.activity_navigation_drawer);

        drawerLayout = findViewById(R.id.NavDrawerID);
        View headerView = drawerLayout.getRootView();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigationID);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected); ///items
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        navProfileImage = (ImageView) header.findViewById(R.id.drawerProfileImage);
        navUsername = (TextView) header.findViewById(R.id.drawerUserName);


        bottomNavigationView = findViewById(R.id.bottomNavID);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setSelectedItemId(R.id.appBarHomeID);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });


        myData = FirebaseDatabase.getInstance().getReference("Data");
        dialog = new SpotsDialog.Builder().setContext(this).build();
        iFirebaseLoadListener = this;

        my_recycler_view = (RecyclerView) findViewById(R.id.firstRecyclerViewID);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this));


        floatingActionButton =(FloatingActionButton) findViewById(R.id.floatButtonID);
        ///setting profile image and username on the nav drawer
        database.getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            User user = snapshot1.getValue(User.class);
                            if(user.getUid().equals(auth.getUid()))
                            {
                                System.out.println(user.getName());
                                navUsername.setText(user.getName());
                                Glide.with(HomePageActivity.this).load(user.getProfileImage())
                                        .placeholder(R.drawable.avatar)
                                        .into(navProfileImage);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomePageActivity.this, "FAB", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePageActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });

        getFirebaseData();


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void myClickItem(MenuItem item){
        switch(item.getItemId()){
            case R.id.appBarChatID:
                Toast.makeText(this, "Chat", Toast.LENGTH_SHORT).show();
                super.finish();
                Intent chatIntent = new Intent(this, MessagingActivity.class);
                this.startActivity(chatIntent);
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
                break;
        }
    }

    private void getFirebaseData() {
        dialog.show();
        myData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ItemGroup> itemGroups = new ArrayList<>();
                for(DataSnapshot groupSnapShot:snapshot.getChildren() ){
                    ItemGroup itemGroup = new ItemGroup();
                    itemGroup.setHeaderTitle(groupSnapShot.child("headerTitle").getValue(true).toString());
                    GenericTypeIndicator<ArrayList<ItemData>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<ItemData>>() {};
                    itemGroup.setListItem(groupSnapShot.child("listItem").getValue(genericTypeIndicator));
                    itemGroups.add(itemGroup);
                }
                iFirebaseLoadListener.onFirebaseLoadSuccess(itemGroups);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                iFirebaseLoadListener.onFirebaseLoadFailed(error.getMessage());

            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<ItemGroup> itemGroupList) {

        MyItemGroupAdapter adapter = new MyItemGroupAdapter(this, itemGroupList);
        my_recycler_view.setAdapter(adapter);
        dialog.dismiss();

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.Logout)
        {
            Intent intent = new Intent(this,LoginSignUpActivity.class);
            auth.signOut();
            Toast.makeText(HomePageActivity.this, "loggin out", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }


    }
}