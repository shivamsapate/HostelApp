package com.shivam.hostelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {
    Button gologin, signin;
    EditText editTextEmail, editTextPassword;
    TextView forgot;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference myref_customer= db.collection("Customer");
    String s;
    FirebaseAuth mAuth;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        gologin = findViewById(R.id.gologin);
        signin = findViewById(R.id.logintosign);
        forgot = findViewById(R.id.forgotpass);
        editTextEmail = findViewById(R.id.login_email);
        editTextPassword = findViewById(R.id.pass);
        bar = findViewById(R.id.progressBar2);
        bar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this,signup.class);
                startActivity(i);
            }
        });

        gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPass();
            }
        });

    }

    private void ForgotPass(){
        String email = editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"check your email",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getdata(){


        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }



        bar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                bar.setVisibility(View.GONE);

                if (task.isSuccessful()) {

                    myref_customer.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                s = documentSnapshot.get("email").toString();
                            }
                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                            if (s!=null){
                                finish();
                                Intent i = new Intent(login.this,customer.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                            else {
                                finish();
                                Intent i = new Intent(login.this,dealer.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }

                        }
                    }); }



                else {
                    editTextEmail.setError("Wrong email");
                    editTextEmail.requestFocus();
                    editTextPassword.setError("Wrong Password");
                    editTextPassword.requestFocus();
                    return;

                }
            }
        });

    }


}
