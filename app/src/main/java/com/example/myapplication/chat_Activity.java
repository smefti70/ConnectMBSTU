package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.Adapter.messagesAdapter;
import com.example.myapplication.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

public class chat_Activity extends AppCompatActivity {

    String senderRoom,receiverRoom;

    ActivityChatBinding binding;

    messagesAdapter adapter;
    ArrayList<Message>messagesList;

    FirebaseDatabase database;
    FirebaseStorage storage;

    String senderUid;
    String receiverUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database=FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        messagesList=new ArrayList<>();


        String  name = getIntent().getStringExtra("name");
        receiverUid=getIntent().getStringExtra("uid");
        senderUid=  FirebaseAuth.getInstance().getUid();


        senderRoom=senderUid+receiverUid;
        receiverRoom=receiverUid+senderUid;

        adapter=new messagesAdapter(this,messagesList,senderRoom,receiverRoom);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(chat_Activity.this));
        binding.recyclerView.setAdapter(adapter);


        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesList.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
                            message.setMessageId(snapshot1.getKey());
                            messagesList.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(chat_Activity.this));
//        binding.recyclerView.setAdapter(adapter);



        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();
                String messageText=binding.messageBox.getText().toString();

                if(messageText.isEmpty()){
                    binding.messageBox.setError("Please type your message");
                    return;
                }

                binding.messageBox.setText("");
                Message message=new Message(messageText,senderUid,date.getTime());

//                binding.recyclerView.setLayoutManager(new LinearLayoutManager(chat_Activity.this));
//                binding.recyclerView.setAdapter(adapter);

                String randomKey = database.getReference().push().getKey();

                database.getReference()
                        .child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .child(randomKey)
                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });




                database.getReference().child("chats")
                                .child(senderRoom)
                                        .child("messages")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        messagesList.clear();
                                                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                                                            Message message = snapshot1.getValue(Message.class);
                                                            message.setMessageId(snapshot1.getKey());
                                                            messagesList.add(message);
                                                        }
                                                        adapter.notifyDataSetChanged();

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });


            }
        });




        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}