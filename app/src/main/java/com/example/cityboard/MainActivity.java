package com.example.cityboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {



    /*private TextView registerPage;
    private Button loginButton;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseDatabase.getInstance().getReference().child("Users").child("Admin").setValue("Arman"); //testing

        /*registerPage =  findViewById(R.id.signupTextID);
        loginButton = (Button) findViewById(R.id.loginButtonID);

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoadRegisterActivity = new Intent(MainActivity.this,RegisterActivity.class);
                MainActivity.this.startActivity(intentLoadRegisterActivity);
            }
        });*/



    }
}