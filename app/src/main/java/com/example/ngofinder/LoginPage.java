package com.example.ngofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageButton googleBtn;
    ImageButton fb_btn;
    FirebaseAuth auth;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        ImageButton b1=(ImageButton)findViewById(R.id.Login);
        Button b2=(Button)findViewById(R.id.signupbtn);
        EditText email = (EditText) findViewById(R.id.emailText);
        EditText password = (EditText) findViewById(R.id.passwordText);
        ImageView logo=findViewById(R.id.logoimg);
        TextView welcometxt=findViewById(R.id.welcometxt);
        TextView random=findViewById(R.id.random);
        auth=FirebaseAuth.getInstance();
        vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);


        googleBtn = (ImageButton)findViewById(R.id.google_btn);
        fb_btn = (ImageButton)findViewById(R.id.fb_btn);
        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(100);
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            navigateToSecondActivity();
        }

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                vibrator.vibrate(100);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                if(password.length()==0 || email.length()==0){
                    Toast.makeText(getApplicationContext(), "Enter Email and Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginPage.this, mainMenu.class);
                                    Toast.makeText(LoginPage.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginPage.this, "Enter correct Email or Password", Toast.LENGTH_SHORT).show();
                                }
                            }

                });
                }
        });

    b2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            vibrator.vibrate(100);
            Intent intent=new Intent(LoginPage.this,SignUp.class);
            Pair[] pairs=new Pair[7];
            pairs[0]=new Pair<View,String>(logo,"logo_image");
            pairs[1]=new Pair<View,String>(welcometxt,"logo_txt");
            pairs[2]=new Pair<View,String>(random,"logo_desc");
            pairs[3]=new Pair<View,String>(b2,"already_trans");
            pairs[4]=new Pair<View,String>(email,"username_trans");
            pairs[5]=new Pair<View,String>(password,"password_trans");
            pairs[6]=new Pair<View,String>(b1,"login_trans");
            ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(LoginPage.this,pairs);
            startActivity(intent,options.toBundle());
        }
    });
    }

    private void signIn() {
        Intent signInIntent =  gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(LoginPage.this,mainMenu.class);
        startActivity(intent);
    }
}