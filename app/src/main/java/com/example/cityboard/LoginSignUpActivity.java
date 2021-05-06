package com.example.cityboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginSignUpActivity extends AppCompatActivity {

    private TextView registerPage;
    private TextView forgotPassword;
    private Button   loginButton;
    //arman
    private EditText email, pass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        registerPage =  findViewById(R.id.signupTextID);
        loginButton = (Button) findViewById(R.id.loginButtonID);
        forgotPassword = (TextView) findViewById(R.id.helpMeLoginTextID);

        //arman
        email = findViewById(R.id.loginemailId);
        pass = findViewById(R.id.loginpassId);

        mAuth = FirebaseAuth.getInstance();


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadForgotPassActivity = new Intent(LoginSignUpActivity.this, ForgotPasswordActivity.class);
                startActivity(intentLoadForgotPassActivity);
            }
        });


        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadRegisterActivity = new Intent(LoginSignUpActivity.this, RegisterActivity.class);
                startActivity(intentLoadRegisterActivity);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = email.getText().toString();
                String textPass = pass.getText().toString();
                if(TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPass))
                    Toast.makeText(LoginSignUpActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                else
                    loginUser(textEmail, textPass);
            }
        });
    }
    private void loginUser(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginSignUpActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginSignUpActivity.this, HomePageActivity.class); ///next activity here
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginSignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}