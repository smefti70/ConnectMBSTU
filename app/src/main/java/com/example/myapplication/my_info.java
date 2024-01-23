package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.Users;
import com.example.myapplication.databinding.ActivityMyInfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class my_info extends AppCompatActivity {

    ActivityMyInfoBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityMyInfoBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        database.getReference()
                .child("users")
            //    .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            Users users= snapshot1.getValue(Users.class);

                            if(auth.getUid().equals(users.getUid())){

                                binding.myInfoName.setText("Name: "+users.getName());
                                binding.myInfoEmail.setText("Email: "+users.getEmail());

                                Glide.with(my_info.this).load(users.getProfileImage())
                                        .placeholder(R.drawable.avatar)
                                        .into(binding.profileImage);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(my_info.this,activity_setup_profile.class));
            }
        });



    }
}