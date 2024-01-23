package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.Users;
import com.example.myapplication.R;
import com.example.myapplication.chat_Activity;
import com.example.myapplication.databinding.SingleChatViewBinding;
import com.example.myapplication.signInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersAdapter extends  RecyclerView.Adapter<UsersAdapter.viewHolder>{

    Context context;
    ArrayList<Users>userslist;

    FirebaseDatabase database;


    public UsersAdapter(){

    }
    public UsersAdapter(Context context, ArrayList<Users> userslist) {
        this.context = context;
        this.userslist = userslist;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_chat_view,parent,false);
        return new viewHolder(view);
    }

    @Override
    public int getItemCount() {
        return userslist.size();
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Users users=userslist.get(position);
        holder.binding.sampleName.setText(users.getName());

        Glide.with(context).load(users.getProfileImage())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.profileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, chat_Activity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("uid",users.getUid());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Are you sure?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                userslist.remove(users);
                                notifyDataSetChanged();


                                database.getInstance().getReference()
                                        .child("users")
                                        .child(users.getUid())
                                        .setValue(null);


                            }
                        })


                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                return false;
            }
        });
    }


    public class viewHolder extends RecyclerView.ViewHolder{

        SingleChatViewBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SingleChatViewBinding.bind(itemView);


        }
    }

}
