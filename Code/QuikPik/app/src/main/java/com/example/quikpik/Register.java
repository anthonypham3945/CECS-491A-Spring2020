package com.example.quikpik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
/*This class is responsible for registering the user into our firebase database and authenticate them*/
public class Register extends AppCompatActivity {
    private EditText inputEmail, inputPassword; //user input of email & password
    private Button btnSignIn, btnSignUp, btnResetPassword;//instance variables for each button in the screen of this activity
    private ProgressBar progressBar;//progress bar that will appear when credentials are submitted
    private static final String TAG = "DocSnippets";
    private FirebaseAuth mAuth;//user in the firebase db

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);//hooks up this class with the corresponding xml file

        // Access a Cloud Firestore instance from your Activity
        mAuth = FirebaseAuth.getInstance();//access firebase for current user

       if(mAuth.getCurrentUser() != null){ //if the user is already signed in then take them to the maps scree
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        // Create a new user with a first and last name
        final Map<String, Object> user = new HashMap<>();

        // Initialize Firebase Auth

        //linking each instance variable to the buttons in the xml file
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);



        btnSignIn.setOnClickListener(new View.OnClickListener() {//when the login button is clicked take user to login screen
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(newIntent);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {//when register button is clicked
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {//checking if email is written inputted
                    inputEmail.setError("Email is Required!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {//checking if password is inputted
                    inputPassword.setError("Password is Required!");
                    return;
                }

                if (password.length() < 6) {//checking if password is at least 6 characters long
                    inputPassword.setError("Password Must Have More Than 6 Characters!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE); //if all i success the progressbar will appear

                //created user and adds them to the db
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }else{
                            Toast.makeText(Register.this,"Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
