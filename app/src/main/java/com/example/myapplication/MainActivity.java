package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.myapplication.Adapter.UsersAdapter;
import com.example.myapplication.Models.Users;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<Users>usersList;
    UsersAdapter usersAdapter;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());


        dialog=new ProgressDialog(this );
        dialog.setMessage("Please wait a second");
        dialog.setCancelable(false);

        database=FirebaseDatabase.getInstance();
        usersList=new ArrayList<>();

        usersAdapter =new UsersAdapter(this,usersList);
        binding.recyclerView.setAdapter(usersAdapter);


        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.meeting:{
                        startActivity(new Intent(MainActivity.this,meet_activity.class));
                    }
                    case R.id.chats:
                }
                return false;
            }
        });

        database.getReference()
                .child("users")
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersList.clear();
                for(DataSnapshot snapshot1  :snapshot.getChildren()){
                    Users users=snapshot1.getValue(Users.class);

                    if(! users.getUid().equals (FirebaseAuth.getInstance().getUid())){
                        usersList.add(users);
                    }

                }
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    //end on create method

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.setting:{

                startActivity(new Intent(MainActivity.this,my_info.class));
                break;
            }
            case R.id.logOut:{


                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MainActivity.this,signInActivity.class));
                                finishAffinity();
                            }
                        })


                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;
            }
            case R.id.meeting:{

                startActivity(new Intent(MainActivity.this,chat_Activity.class));

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Are you sure want to close application?")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}