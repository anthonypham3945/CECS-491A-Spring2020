package com.example.quikpik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
/*This class is responsible for the login screen and its function*/
public class Login extends AppCompatActivity {
   //FirebaseDatabase database;
   //DatabaseReference users;
    //instance variable for the login page
   EditText inputEmail, inputPassword;
   Button btnSignIn, btnRegister, forgotPassword;
   ProgressBar progressBar;
   FirebaseAuth mAuth;//current user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//joins script to the login xml file

        //assigns instance variables to its xml counterpart
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        btnSignIn = findViewById(R.id.sign_in_button);
        btnRegister = findViewById(R.id.btn_register_button);
        forgotPassword = findViewById(R.id.btn_reset_password);

        btnRegister.setOnClickListener(new View.OnClickListener() {//when the register button is clicked
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));//take user to the register screen
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {//when the login button is clicked
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {//check if the user entered an email
                    inputEmail.setError("Email is Required!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {//check if user entered a valid password
                    inputPassword.setError("Password is Required!");
                    return;
                }

                //authenticate user with the inputted email and password
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {//if the task is completed
                        if(task.isSuccessful()){//if task is successful
                            //display success message
                            Toast.makeText(Login.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            //take user to maps screen
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            progressBar.setVisibility(View.VISIBLE);//progressbar becomes visible
                        }else{//if user info is wrong prompt message
                            Toast.makeText(Login.this,"Email or Password was Incorrect! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));//take user to the forgot password screen
            }
        });
    }
}
