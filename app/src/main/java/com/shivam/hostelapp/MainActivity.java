package com.shivam.hostelapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {


    private static int SPLASH_SCREEN=5000;
    String s;
    String Email;
    Animation topanim,bottomanim;
    ImageView image;
    TextView logo,slogn;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference myref_customer= db.collection("Customer");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //animation
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //hooks
        image= findViewById(R.id.imageView3);
        logo= findViewById(R.id.textView);
        slogn = findViewById(R.id.textView2);

        image.setAnimation(topanim);
        logo.setAnimation(bottomanim);
        slogn.setAnimation(bottomanim);

        final FirebaseUser usr = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                if (usr != null) {
                    Email = usr.getEmail();
                    //Toast.makeText(getApplicationContext(),Email,Toast.LENGTH_LONG).show();
                    myref_customer.document(Email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()){
                                s = documentSnapshot.get("email").toString();
                            }
                            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                            if (s!=null){
                                finish();
                                Intent i = new Intent(MainActivity.this,customer.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                            else {
                                finish();
                                Intent i = new Intent(MainActivity.this,dealer.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }

                        }
                    });
                }
                else {
                    Intent i = new Intent(MainActivity.this,login.class);

                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Pair[] pairs = new Pair[2];
                    pairs[0]=new Pair<View,String>(image,"logo_image");
                    pairs[1]=new Pair<View,String>(logo,"logo_text");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(i,options.toBundle());
                }

            }
        },SPLASH_SCREEN);
    }

}

