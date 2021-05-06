package com.example.cityboard;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.cert.PolicyNode;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class CreatePostActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef;
    ActionBar actionBar;

    //views
    EditText postTitleET, postDescriptionET, postPriceET;
    ImageView postImageIV;
    Button postPublishBT;
    Spinner postSpinner;

    //user info
    String userName, userEmail , userID, dp;

    //image picked will be send in this uri
    Uri image_rui = null;

    //progress bar
    ProgressDialog pd;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 69;
    private static final int STORAGE_REQUEST_CODE = 420;

    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 690;
    private static final int IMAGE_PICK_GALLERY_CODE = 4200;

    String[] categoryItems;

    //permissions array
    String cameraPermissions[];
    String storagePermissions[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Post");
        // enable back button in action bar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //initialize permissions
        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);


        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();
        actionBar.setSubtitle(userEmail);


        //*********get some information of current user to include in post******************
        //****FIREBASE WORK*****

        userDbRef = FirebaseDatabase.getInstance().getReference("users"); //  is "users* in DB? *****
        Query query = userDbRef.orderByChild("email").equalTo(userEmail);  // is "email" in DB????
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot ds: snapshot.getChildren()){

                    userName = ""+ ds.child("name").getValue(); //*******????
                    userEmail = ""+ ds.child("email").getValue(); //*************????
                    dp = ""+ ds.child("image").getValue();  //***********????

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        categoryItems = getResources().getStringArray(R.array.Category_Items);

        //initialize views
        postTitleET = findViewById(R.id.postTitleTextID);
        postDescriptionET = findViewById(R.id.postDescriptionTextID);
        postPriceET = findViewById(R.id.postProductPriceTextID);
        postImageIV = findViewById(R.id.postImageImageID);
        postPublishBT = findViewById(R.id.postPublishButtonID);
        postSpinner = findViewById(R.id.postCategorySpinnerID);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_view,R.id.spinnerTextViewID, categoryItems);
        postSpinner.setAdapter(spinnerAdapter);

        //get image from camera, gallery on click on listener

        postImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pic dialogue
                showImagePickDialogue();

            }
        });


        //Publish button onClick Listener
        postPublishBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get title, description from edit texts
                String postTitle = postTitleET.getText().toString().trim();
                String postDescription = postDescriptionET.getText().toString().trim();
                String postPrice = postPriceET.getText().toString().trim();
                String postCategoryItem = postSpinner.getSelectedItem().toString();
                if(TextUtils.isEmpty(postTitle)){
                    Toast.makeText(CreatePostActivity.this,"Enter Title",Toast.LENGTH_SHORT);
                    return;
                }
                if(TextUtils.isEmpty(postDescription)){
                    Toast.makeText(CreatePostActivity.this,"Enter Description",Toast.LENGTH_SHORT);
                    return;
                }
                if(TextUtils.isEmpty(postPrice)){
                    Toast.makeText(CreatePostActivity.this,"Enter Price",Toast.LENGTH_SHORT);
                    return;
                }
                if(image_rui==null){
                    //post without image
                    uploadData(postTitle,postDescription,postPrice,postCategoryItem,"NoImage");
                }else{

                    uploadData(postTitle,postDescription,postPrice,postCategoryItem,String.valueOf(image_rui));

                }
            }

        });
    }

    private void uploadData(String postTitle, String postDescription, String postPrice, String postCategoryItem, String uri) {

        pd.setMessage("Publishing post...");
        pd.show();

        //for post-image-name ,post-id, post-publish-time
        String timeStamp = String.valueOf(System.currentTimeMillis());
        Long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy, h:mm a");
        String dateString = sdf.format(date);
        String filePathAndName = "Posts/" + "post_" + timeStamp;


        if(!uri.equals("NoImage")){
            //post with image

            if (postCategoryItem.equals("Resident")){
                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                ref.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // image is uploaded to firebase storage now get it's URl
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());

                        String downloadUri = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()){
                            //url is received , upload post to firebase database

                            HashMap<Object, String> hashMap = new HashMap<>();
                            //***************put post info ************************************* ???
                            hashMap.put("uid",userID);
                            hashMap.put("uName",userName);
                            hashMap.put("uEmail",userEmail);
                            hashMap.put("pId",timeStamp);
                            hashMap.put("name",postTitle);
                            hashMap.put("pDescription",postDescription);
                            hashMap.put("image",downloadUri);
                            hashMap.put("pTime",dateString);
                            hashMap.put("price",postPrice);

                            // path to store post
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MyData/0/listItem");
                            //put data iin this ref

                            ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //added in database
                                    pd.dismiss();
                                    Toast.makeText(CreatePostActivity.this,"Post published",Toast.LENGTH_SHORT).show();
                                    //reset views
                                    postTitleET.setText("");
                                    postDescriptionET.setText("");
                                    postImageIV.setImageURI(null);
                                    postPriceET.setText("");
                                    image_rui = null;

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed adding post in database
                                    pd.dismiss();
                                    Toast.makeText(CreatePostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();


                                }
                            });


                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed uploading image
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }

            else if (postCategoryItem.equals("Teaching")){
                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                ref.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // image is uploaded to firebase storage now get it's URl
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());

                        String downloadUri = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()){
                            //url is received , upload post to firebase database

                            HashMap<Object, String> hashMap = new HashMap<>();
                            //***************put post info ************************************* ???
                            hashMap.put("uid",userID);
                            hashMap.put("uName",userName);
                            hashMap.put("uEmail",userEmail);
                            hashMap.put("pId",timeStamp);
                            hashMap.put("name",postTitle);
                            hashMap.put("pDescription",postDescription);
                            hashMap.put("image",downloadUri);
                            hashMap.put("pTime",dateString);
                            hashMap.put("price",postPrice);

                            // path to store post
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MyData/1/listItem");
                            //put data iin this ref

                            ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //added in database
                                    pd.dismiss();
                                    Toast.makeText(CreatePostActivity.this,"Post published",Toast.LENGTH_SHORT).show();
                                    //reset views
                                    postTitleET.setText("");
                                    postDescriptionET.setText("");
                                    postImageIV.setImageURI(null);
                                    postPriceET.setText("");
                                    image_rui = null;

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed adding post in database
                                    pd.dismiss();
                                    Toast.makeText(CreatePostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();


                                }
                            });


                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed uploading image
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }

            else if (postCategoryItem.equals("Product")){
                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                ref.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // image is uploaded to firebase storage now get it's URl
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());

                        String downloadUri = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()){
                            //url is received , upload post to firebase database

                            HashMap<Object, String> hashMap = new HashMap<>();
                            //***************put post info ************************************* ???
                            hashMap.put("uid",userID);
                            hashMap.put("uName",userName);
                            hashMap.put("uEmail",userEmail);
                            hashMap.put("pId",timeStamp);
                            hashMap.put("name",postTitle);
                            hashMap.put("pDescription",postDescription);
                            hashMap.put("image",downloadUri);
                            hashMap.put("pTime",dateString);
                            hashMap.put("price",postPrice);

                            // path to store post
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MyData/2/listItem");
                            //put data iin this ref

                            ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //added in database
                                    pd.dismiss();
                                    Toast.makeText(CreatePostActivity.this,"Post published",Toast.LENGTH_SHORT).show();
                                    //reset views
                                    postTitleET.setText("");
                                    postDescriptionET.setText("");
                                    postImageIV.setImageURI(null);
                                    postPriceET.setText("");
                                    image_rui = null;

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed adding post in database
                                    pd.dismiss();
                                    Toast.makeText(CreatePostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();


                                }
                            });


                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed uploading image
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }

            else if (postCategoryItem.equals("Service")){
                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                ref.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // image is uploaded to firebase storage now get it's URl
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());

                        String downloadUri = uriTask.getResult().toString();

                        if(uriTask.isSuccessful()){
                            //url is received , upload post to firebase database

                            HashMap<Object, String> hashMap = new HashMap<>();
                            //***************put post info ************************************* ???
                            hashMap.put("uid",userID);
                            hashMap.put("uName",userName);
                            hashMap.put("uEmail",userEmail);
                            hashMap.put("pId",timeStamp);
                            hashMap.put("name",postTitle);
                            hashMap.put("pDescription",postDescription);
                            hashMap.put("image",downloadUri);
                            hashMap.put("pTime",dateString);
                            hashMap.put("price",postPrice);

                            // path to store post
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MyData/3/listItem");
                            //put data iin this ref

                            ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //added in database
                                    pd.dismiss();
                                    Toast.makeText(CreatePostActivity.this,"Post published",Toast.LENGTH_SHORT).show();
                                    //reset views
                                    postTitleET.setText("");
                                    postDescriptionET.setText("");
                                    postImageIV.setImageURI(null);
                                    postPriceET.setText("");
                                    image_rui = null;

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //failed adding post in database
                                    pd.dismiss();
                                    Toast.makeText(CreatePostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();


                                }
                            });


                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed uploading image
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }

        }else{
            //post without image
            HashMap<Object, String> hashMap = new HashMap<>();
            //***************put post info ************************************* ???
            hashMap.put("uid",userID);
            hashMap.put("uName",userName);
            hashMap.put("uEmail",userEmail);
            hashMap.put("pId",timeStamp);
            hashMap.put("name",postTitle);
            hashMap.put("pDescription",postDescription);
            hashMap.put("image","https://firebasestorage.googleapis.com/v0/b/cityboarddemo.appspot.com/o/Profiles%2FuUqlqaG3kMMHmnQAtNLxqGmtQzq1?alt=media&token=456575c5-d5ed-4fae-9187-215990c66127");
            hashMap.put("pTime",dateString);
            hashMap.put("price",postPrice);

            // path to store post
            if(postCategoryItem.equals("Resident")) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("MyData/0/listItem");
                //put data iin this ref

                ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added in database
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                        postTitleET.setText("");
                        postDescriptionET.setText("");
                        postPriceET.setText("");
                        postImageIV.setImageURI(null);
                        image_rui = null;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding post in database
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });
            }

            else if(postCategoryItem.equals("Teaching")){
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("MyData/1/listItem");
                //put data iin this ref

                ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added in database
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                        postTitleET.setText("");
                        postDescriptionET.setText("");
                        postPriceET.setText("");
                        postImageIV.setImageURI(null);
                        image_rui = null;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding post in database
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });
            }

            else if(postCategoryItem.equals("Product")){
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("MyData/2/listItem");
                //put data iin this ref

                ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added in database
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                        postTitleET.setText("");
                        postDescriptionET.setText("");
                        postPriceET.setText("");
                        postImageIV.setImageURI(null);
                        image_rui = null;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding post in database
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });
            }

            else if (postCategoryItem.equals("Service")){
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("MyData/3/listItem");
                //put data iin this ref

                ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added in database
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                        postTitleET.setText("");
                        postDescriptionET.setText("");
                        postPriceET.setText("");
                        postImageIV.setImageURI(null);
                        image_rui = null;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding post in database
                        pd.dismiss();
                        Toast.makeText(CreatePostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });
            }


        }

    }


    private void showImagePickDialogue() {
        // options to pick from camera/gallery in dialogue
        String options[] = {"Camera","Gallery"};
        //dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        //set options dialogue
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedOption) {
                if(selectedOption == 0){
                    // camera selected
                    if(!checkCameraPermission())
                        requestCameraPermission();
                    else
                        pickImageFromCamera();


                }else if(selectedOption == 1){
                    //gallery selected
                    if(!checkStoragePermission())
                        requestStoragePermission();
                    else
                        pickImageFromGallery();

                }
            }
        });
        //Create and show
        builder.create().show();
    }


    private boolean checkStoragePermission(){
        //check if storage permission is enabled or not
        // return true if enabled and false if not
        boolean permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return permissionResult;

    }
    private void requestStoragePermission(){
        //request runtime storage permission
        ActivityCompat.requestPermissions(this,storagePermissions, STORAGE_REQUEST_CODE);

    }


    private boolean checkCameraPermission(){
        //check if Camera permission is enabled or not
        // return true if enabled and false if not
        boolean permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean permissionResultOne = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return permissionResult && permissionResultOne;

    }
    private void requestCameraPermission(){
        //request runtime camera permission
        ActivityCompat.requestPermissions(this,cameraPermissions, CAMERA_REQUEST_CODE);

    }

    private void pickImageFromCamera(){
        //intent to pick image from camera

        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.TITLE,"Temp Descr");
        image_rui = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);

    }
    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }



    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user  != null) {
            //user is signed in stay  here
            //set email of logged in user
            //myProfileTv.setText(user.getEmail());
            userEmail = user.getEmail();
            userID = user.getUid(); //*****************?????

        }else {
            //user not signed in, go to loginSignup activity
            startActivity(new Intent(this,LoginSignUpActivity.class));

            finish();
        }

    }

    public boolean onSupportNavigateUp(){

        onBackPressed(); // goes to previous activity
        return super.onSupportNavigateUp();
    }

    // handle permissions results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // this method is called when user selecs allow or deny from permissions request dialogue
        // here is handled permissions requests (ALLOW or DENY)

        switch (requestCode){

            case CAMERA_REQUEST_CODE: {

                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(true){
                        //both permissions granted
                        pickImageFromCamera();
                    }else {
                        //both permissions denied

                        Toast.makeText(this, "Camera & Storage permissions necessary", Toast.LENGTH_SHORT).show();
                    }

                }else {

                }

            } break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length>0){
                    boolean storageAccepted = true;
                    if(storageAccepted){
                        //Storage permissions granted
                        pickImageFromGallery();
                    }else {
                        //both permissions denied

                        Toast.makeText(this, "Storage permission necessary", Toast.LENGTH_SHORT).show();
                    }

                }else{

                }

            } break;

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this method will be called after picking image from camera or gallery
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery, get uri of the image
                image_rui = data.getData();
                //set image view
                postImageIV.setImageURI(image_rui);

            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //image is picked from camera, get uri of the image
                //set image view

                postImageIV.setImageURI(image_rui);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}