package com.shivam.hostelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class dealer extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.dealer_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                if (mAuth.getCurrentUser()==null) {
                    finish();
                    startActivity(new Intent(dealer.this,login.class));
                }
            }
        });
    }

}
