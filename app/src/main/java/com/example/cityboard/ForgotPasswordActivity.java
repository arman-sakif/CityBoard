package com.example.cityboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {


    private TextView registerPage;
    private TextView  loginButton;
    private Button sendResetBtn;
    private EditText resetEmail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        registerPage =  findViewById(R.id.redirectSignUpID);
        loginButton =   findViewById(R.id.redirectLoginID);
        sendResetBtn = findViewById(R.id.loginButtonID);
        resetEmail = findViewById(R.id.forgotEmail);

        auth = FirebaseAuth.getInstance();

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadRegisterActivity = new Intent(ForgotPasswordActivity.this, RegisterActivity.class);
                startActivity(intentLoadRegisterActivity);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadHomePageActivity = new Intent(ForgotPasswordActivity.this, LoginSignUpActivity.class);
                startActivity(intentLoadHomePageActivity);
            }
        });

        //Sending Reset Pass link to Email
        sendResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailTxt = resetEmail.getText().toString();

                auth.sendPasswordResetEmail(emailTxt).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginSignUpActivity.class);
                            Toast.makeText(ForgotPasswordActivity.this, "Pass Reset email Sent to "+emailTxt, Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });






    }
}