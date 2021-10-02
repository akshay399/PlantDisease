package com.example.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    private TextView banner;
    private Button registerUser;
    private EditText editTextFullName, editTextPhone, editTextEmail, editTextPassword;
    private ProgressBar progressBar;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register_user);

        //banner
        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName= (EditText) findViewById(R.id.fullName);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPhone = (EditText) findViewById(R.id.phone);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;

        }

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString();
        String phone    = editTextPhone.getText().toString();

        if(TextUtils.isEmpty(fullName)){
            editTextFullName.setError("Name is Required.");
            editTextFullName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(email)){
            editTextEmail.setError("Email is Required.");
            editTextEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            editTextPassword.setError("Password is Required.");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("Password must be equal or more than 6 chatacters.");
            editTextPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(phone)){
            editTextPhone.setError("Phone is Required.");
            editTextPhone.requestFocus();
            return;
        }

        //valid email format checker
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    User user = new User(fullName,phone,email);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterUser.this,"User has been resgistered successfully",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                            else{
                                Toast.makeText(RegisterUser.this,"User has not  been resgistered successfully",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterUser.this,"User has not  been resgistered successfully",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}