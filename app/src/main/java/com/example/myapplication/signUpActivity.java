package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Models.Users;
import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;

    ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.createUserWithEmailAndPassword(
                        binding.mailBox.getText().toString(),binding.passBox.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    Users user=new Users(binding.nameBox.getText().toString(),binding.mailBox.getText().toString(),binding.passBox                                      .getText().toString());
                                    String id=task.getResult().getUser().getUid();

                                    database.getReference()
                                            .child("Users")
                                            .child(id)
                                            .setValue(user);

                                //    Intent intent = new Intent(signUpActivity.this,signInActivity.class);
                                    auth.signOut();
                                    Intent intent = new Intent(signUpActivity.this,signInActivity.class);
                                    startActivity(intent);
                               //     Toast.makeText(signUpActivity.this, "SignUp success", Toast.LENGTH_SHORT).show();


                                }else{
                                    Toast.makeText(signUpActivity.this, "SignUp Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signUpActivity.this,signInActivity.class));
            }
        });











    }
}