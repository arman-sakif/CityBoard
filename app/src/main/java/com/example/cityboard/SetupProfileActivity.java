package com.example.cityboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cityboard.Search.SearchActivity;
import com.example.cityboard.chats.MessagingActivity;
import com.example.cityboard.chats.User;
import com.example.cityboard.databinding.ActivitySetupProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupProfileActivity extends AppCompatActivity {

    ActivitySetupProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage ;

    ProgressDialog dialog;

    BottomNavigationView bottomNavigationView;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = findViewById(R.id.bottomNavID);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setSelectedItemId(R.id.appBarProfileID);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });

        floatingActionButton =(FloatingActionButton) findViewById(R.id.profileFloatButtonID);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SearchActivity.this, "FAB", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SetupProfileActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile Image...");
        dialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        String uid = auth.getCurrentUser().getUid();

        database.getReference().child("users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            User user = snapshot1.getValue(User.class);
                            if(user.getUid().equals(auth.getUid()))
                            {
                                binding.nameBox.setText(user.getName());
                                binding.emailbox.setText(user.getEmail());
                                binding.phonenum.setText(user.getPhoneNumber());
                                if (!user.getProfileImage().equals("No Image"))
                                {
                                    Glide.with(SetupProfileActivity.this).load(user.getProfileImage()) ///setting profile image in row_conversation
                                            .placeholder(R.drawable.avatar)
                                            .into(binding.imageView);
                                }
                                //this would show if user already has an image
                                //but this conflicts with when user uploads an image so it's commented out
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        getSupportActionBar().hide();

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String name = binding.nameBox.getText().toString();
                if(name.isEmpty()){
                    binding.nameBox.setError("Please Type a Name");
                    return;
                }*/

                //dialog.show();

                if(selectedImage != null)
                {
                    dialog.show();
                    StorageReference reference = storage.getReference().child("Profiles").child(uid); //child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        database.getReference().child("users")
                                                .child(uid)
                                                .child("profileImage")
                                                .setValue(imageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialog.dismiss();
                                                Toast.makeText(SetupProfileActivity.this, "Profile Image Updated", Toast.LENGTH_SHORT).show();
                                                /*Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();*/
                                            }
                                        });
                                    }
                                });
                            }else{
                                Toast.makeText(SetupProfileActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(SetupProfileActivity.this, "No Image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null){
            if(data.getData()!=null){
                Toast.makeText(this, "Got Image, press button", Toast.LENGTH_SHORT).show();
                selectedImage = data.getData();
                binding.imageView.setImageURI(selectedImage);

            }
        }
    }

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
                super.finish();
                Intent searchIntent = new Intent(this, SearchActivity.class);
                this.startActivity(searchIntent);
                break;
            case R.id.appBarProfileID:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.appBarHomeID:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                super.finish();
                Intent intent = new Intent(this, HomePageActivity.class);
                this.startActivity(intent);
                break;
        }
    }
}