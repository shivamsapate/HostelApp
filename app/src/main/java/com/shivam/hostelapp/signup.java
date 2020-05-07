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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class signup extends AppCompatActivity {


    Button signtologin,signin;
    EditText user,pass,email,phone;
    Spinner spinner;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        signin = findViewById(R.id.sign_btn);
        signtologin = findViewById(R.id.signtologin);
        user = findViewById(R.id.sign_username);
        pass = findViewById(R.id.sign_password);
        email =  findViewById(R.id.Email);
        phone = findViewById(R.id.sign_phone);

        signtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signup.this,login.class);
                startActivity(i);
            }
        });

        spinner = findViewById(R.id.sp);
        final String text = spinner.getSelectedItem().toString();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passw = pass.getText().toString();
                String emailid = email.getText().toString();
                String phnno = phone.getText().toString();
                registeration(emailid,phnno,passw);

            }
        });



    }
    private void registeration(String emailid, String phnno,String passw){
        Toast.makeText(getApplicationContext(),"in reg",Toast.LENGTH_LONG).show();
        mAuth = FirebaseAuth.getInstance();

        if (emailid.isEmpty()){
            email.setError("Email field is empty");
            email.requestFocus();
            return;
        }
        if (passw.isEmpty()){
            pass.setError("Password field is empty");
            pass.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()){
            email.setError("Email not in correct format");
            email.requestFocus();
            return;
        }
        if (phnno.length()<10){
            phone.setError("Phone No is not correct ");
            phone.requestFocus();
            return;
        }
        //Toast.makeText(getApplicationContext(),"registeration succesfull",Toast.LENGTH_LONG).show();

        mAuth.createUserWithEmailAndPassword(emailid,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    addData();
                }
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void addData(){

        String usrname = user.getText().toString();
        String passw = pass.getText().toString();
        String emailid = email.getText().toString();
        String phnno = phone.getText().toString();
        final String text = spinner.getSelectedItem().toString();



        if(!usrname.isEmpty() && !passw.isEmpty() && !emailid.isEmpty() && !phnno.isEmpty()) {

            CollectionReference myref = db.collection(text);
            Userhelperclass save = new Userhelperclass();
            save.setEmail(emailid);
            save.setPassword(passw);
            save.setPhoneno(phnno);
            save.setUsername(usrname);

            myref.document(emailid).set(save).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();

                    }
                    else {
                        String e = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(),e,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        else {
            Toast.makeText(getApplicationContext(),"fill data frist",Toast.LENGTH_LONG).show();
        }
    }

}
