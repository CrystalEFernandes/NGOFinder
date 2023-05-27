package com.example.ngofinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        ImageButton b1 = (ImageButton)findViewById(R.id.createaccButton);
        loginbtn = findViewById(R.id.loginbtn);
        EditText email = (EditText) findViewById(R.id.emailText);
        EditText password = (EditText) findViewById(R.id.passwordText);
        EditText name = (EditText) findViewById(R.id.nameText);
        EditText username = (EditText) findViewById(R.id.usernameText);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user=new User(name.getText().toString(), username.getText().toString(),email.getText().toString(),password.getText().toString());
                            String id=task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignUp.this,"User data saved",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignUp.this,mainMenu.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUp.this,"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,LoginPage.class);
                startActivity(intent);
            }
        });
    }
}