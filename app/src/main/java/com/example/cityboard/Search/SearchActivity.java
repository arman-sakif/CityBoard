package com.example.cityboard.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cityboard.CreatePostActivity;
import com.example.cityboard.HomePageActivity;
import com.example.cityboard.R;
import com.example.cityboard.Search.MyAdapter;
import com.example.cityboard.SetupProfileActivity;
import com.example.cityboard.chats.MessagingActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class SearchActivity extends AppCompatActivity {

    RecyclerView searchRecyclerView;
    MyAdapter adapter;
    BottomNavigationView bottomNavigationView;

    Spinner categorySpinner;
    String[] categoryItems;

    Spinner priceOrderSpinner;
    String[] orderByPrice;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        categorySpinner = findViewById(R.id.categorySpinnerID);
        categoryItems = getResources().getStringArray(R.array.Category_Items);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_view,R.id.spinnerTextViewID, categoryItems);
        categorySpinner.setAdapter(spinnerAdapter);

        priceOrderSpinner = findViewById(R.id.priceOrderSpinnerID);
        orderByPrice = getResources().getStringArray(R.array.price_order);
        ArrayAdapter<String> priceSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_view,R.id.spinnerTextViewID, orderByPrice);
        priceOrderSpinner.setAdapter(priceSpinnerAdapter);

        searchRecyclerView = findViewById(R.id.searchRecyclerViewID);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottomNavigationView = findViewById(R.id.bottomNavID);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setSelectedItemId(R.id.appBarSearchID);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryItem = categorySpinner.getSelectedItem().toString();
                String priceOrder = priceOrderSpinner.getSelectedItem().toString();
                //Toast.makeText(SearchActivity.this, ""+categoryItem+priceOrder, Toast.LENGTH_SHORT).show();
                myClickItem(categoryItem, priceOrder);

                priceOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String categoryItem = categorySpinner.getSelectedItem().toString();
                        String priceOrder = priceOrderSpinner.getSelectedItem().toString();
                        //Toast.makeText(SearchActivity.this, ""+categoryItem+priceOrder, Toast.LENGTH_SHORT).show();
                        myClickItem(categoryItem, priceOrder);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //return;
            }
        });

        FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/0/listItem"), Member.class)
                        .build();

        adapter = new MyAdapter(options);
        searchRecyclerView.setAdapter(adapter);

        floatingActionButton =(FloatingActionButton) findViewById(R.id.searchFloatButtonID);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SearchActivity.this, "FAB", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });


    }

    private void myClickItem(String categoryItem, String priceOrder) {
        //Toast.makeText(SearchActivity.this, ""+categoryItem+priceOrder, Toast.LENGTH_SHORT).show();
        if (categoryItem.equals("Resident")){
            //String priceOrder = priceOrderSpinner.getSelectedItem().toString();
            if (priceOrder.equals("Old to New")){
                FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                        new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/0/listItem"), Member.class)
                                .build();

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setReverseLayout(false);
                layoutManager.setStackFromEnd(false);
                searchRecyclerView.setLayoutManager(layoutManager);
                adapter = new MyAdapter(options);
                adapter.startListening();
                searchRecyclerView.setAdapter(adapter);
            }
            else if (priceOrder.equals("New to Old")){
                FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                        new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/0/listItem"), Member.class)
                                .build();

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                searchRecyclerView.setLayoutManager(layoutManager);
                adapter = new MyAdapter(options);
                adapter.startListening();
                searchRecyclerView.setAdapter(adapter);
            }

        }
        else if (categoryItem.equals("Teaching")){
            if (priceOrder.equals("Old to New")){
                FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                        new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/1/listItem"), Member.class)
                                .build();

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setReverseLayout(false);
                layoutManager.setStackFromEnd(false);
                searchRecyclerView.setLayoutManager(layoutManager);
                adapter = new MyAdapter(options);
                adapter.startListening();
                searchRecyclerView.setAdapter(adapter);
            }
            else if (priceOrder.equals("New to Old")){
                FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                        new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/1/listItem"), Member.class)
                                .build();

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                searchRecyclerView.setLayoutManager(layoutManager);
                adapter = new MyAdapter(options);
                adapter.startListening();
                searchRecyclerView.setAdapter(adapter);
            }
        }
        else if (categoryItem.equals("Product")){
            if (priceOrder.equals("Old to New")){
                FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                        new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/2/listItem"), Member.class)
                                .build();

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setReverseLayout(false);
                layoutManager.setStackFromEnd(false);
                searchRecyclerView.setLayoutManager(layoutManager);
                adapter = new MyAdapter(options);
                adapter.startListening();
                searchRecyclerView.setAdapter(adapter);
            }
            else if (priceOrder.equals("New to Old")){
                FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                        new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/2/listItem"), Member.class)
                                .build();

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                searchRecyclerView.setLayoutManager(layoutManager);
                adapter = new MyAdapter(options);
                adapter.startListening();
                searchRecyclerView.setAdapter(adapter);
            }
        }
        else if (categoryItem.equals("Service")){
            if (priceOrder.equals("Old to New")){
                FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                        new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/3/listItem"), Member.class)
                                .build();

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setReverseLayout(false);
                layoutManager.setStackFromEnd(false);
                searchRecyclerView.setLayoutManager(layoutManager);
                adapter = new MyAdapter(options);
                adapter.startListening();
                searchRecyclerView.setAdapter(adapter);
            }
            else if (priceOrder.equals("New to Old")){
                FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                        new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/3/listItem"), Member.class)
                                .build();

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                searchRecyclerView.setLayoutManager(layoutManager);
                adapter = new MyAdapter(options);
                adapter.startListening();
                searchRecyclerView.setAdapter(adapter);
            }
        }

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
                super.finish();
                Intent chatIntent = new Intent(this, MessagingActivity.class);
                this.startActivity(chatIntent);
                break;
            case R.id.appBarSearchID:
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.searchMenuID);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){


            @Override
            public boolean onQueryTextSubmit(String query) {
                String categoryItem = categorySpinner.getSelectedItem().toString();
                processSearch(query, categoryItem);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String categoryItem = categorySpinner.getSelectedItem().toString();
                processSearch(newText, categoryItem);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processSearch(String s, String categoryItem){

        if (categoryItem.equals("Resident")){
            FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                    new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/0/listItem").orderByChild("name").startAt(s).endAt(s+"\uf8ff"), Member.class)
                            .build();

            adapter = new MyAdapter(options);
            adapter.startListening();
            searchRecyclerView.setAdapter(adapter);
        }

        else if (categoryItem.equals("Teaching")){
            FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                    new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/1/listItem").orderByChild("name").startAt(s).endAt(s+"\uf8ff"), Member.class)
                            .build();

            adapter = new MyAdapter(options);
            adapter.startListening();
            searchRecyclerView.setAdapter(adapter);
        }

        else if (categoryItem.equals("Product")){
            FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                    new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/2/listItem").orderByChild("name").startAt(s).endAt(s+"\uf8ff"), Member.class)
                            .build();

            adapter = new MyAdapter(options);
            adapter.startListening();
            searchRecyclerView.setAdapter(adapter);
        }

        else if (categoryItem.equals("Service")){
            FirebaseRecyclerOptions<com.example.cityboard.Search.Member> options =
                    new FirebaseRecyclerOptions.Builder<com.example.cityboard.Search.Member>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("MyData/3/listItem").orderByChild("name").startAt(s).endAt(s+"\uf8ff"), Member.class)
                            .build();

            adapter = new MyAdapter(options);
            adapter.startListening();
            searchRecyclerView.setAdapter(adapter);
        }
    }

}