package com.example.cityboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cityboard.chats.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;



public class RegisterActivity extends AppCompatActivity {
    private TextView loginPage;
    private TextView alreadyHaveAnAccount;

    //arman
    private EditText username, email, phone, pass;
    private Button register;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Account...");
        dialog.setCancelable(false);

        loginPage = findViewById(R.id.loginTextID);
        alreadyHaveAnAccount = findViewById(R.id.haveAnAccountTextID);

        //arman
        register = findViewById(R.id.registerid);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phonenumber);
        pass = findViewById(R.id.pass);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadLoginActivity = new Intent(RegisterActivity.this, LoginSignUpActivity.class);
                RegisterActivity.this.startActivity(intentLoadLoginActivity);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUsername = username.getText().toString();
                String textEmail = email.getText().toString();
                String textPhone = phone.getText().toString();
                String textPass = pass.getText().toString();

                if (TextUtils.isEmpty(textUsername) || TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPhone) || TextUtils.isEmpty(textPass))
                    Toast.makeText(RegisterActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                else if (textPass.length() < 6)
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                else {
                    registerUser(textUsername, textEmail, textPhone, textPass);
                }


            }
        });

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadLoginActivity = new Intent(RegisterActivity.this, LoginSignUpActivity.class);
                RegisterActivity.this.startActivity(intentLoadLoginActivity);
            }
        });
    }

    private void registerUser(String name, String email, String phone, String pass) {
        dialog.show();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                String uid = mAuth.getCurrentUser().getUid();
                User user = new User(uid, name, phone, "No Image",email);


                mRootRef.child("users")
                        .child(uid)
                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, HomePageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                });


            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}