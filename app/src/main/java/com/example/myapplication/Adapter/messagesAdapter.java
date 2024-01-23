package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Message;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemReceiveBinding;
import com.example.myapplication.databinding.ItemSentBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class messagesAdapter extends  RecyclerView.Adapter{

    ArrayList<Message>messages;
    Context context;

    final  int itemSend=1;
    final  int itemReceive=2;
    String senderRoom,receiverRoom;

    public messagesAdapter(Context context,ArrayList<Message> messages,String senderRoom,String receiverRoom ) {
        this.messages = messages;
        this.context = context;
        this.senderRoom=senderRoom;
        this.receiverRoom=receiverRoom;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==itemSend){
            View view= LayoutInflater.from(context).inflate(R.layout.item_sent,parent,false);
            return new sendViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false);
            return new receiveViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        Message message=messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return itemSend;
        }else {
            return itemReceive;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message=messages.get(position);

        int reactions[]= new int[]{

                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if(holder.getClass()==sendViewHolder.class){
                sendViewHolder viewHolder =(sendViewHolder) holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }else{

                receiveViewHolder viewHolder =(receiveViewHolder) holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            message.setFeeling(pos);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(message.getMessageId())
                    .setValue(message);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .child(message.getMessageId())
                    .setValue(message);

            return true;

        });


        if(holder.getClass()==sendViewHolder.class){
            sendViewHolder viewHolder =(sendViewHolder) holder;
            viewHolder.binding.message.setText(message.getMessage());

            if(message.getFeeling()>=0){

                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);

            }else{
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }

            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //popup.onTouch(v,event);
                    return false;
                }
            });


            //delete message
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageId())
                                    .setValue(null);
                    FirebaseDatabase.getInstance().getReference()
                            .child("chats")
                            .child(receiverRoom)
                            .child("messages")
                            .child(message.getMessageId())
                            .setValue(null);

                    Toast.makeText(context, message.getMessage(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });


        }
        else{

            receiveViewHolder viewHolder =(receiveViewHolder)holder;
            viewHolder.binding.message.setText(message.getMessage());

            if(message.getFeeling()>=0){
                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }else{
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }


            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return messages.size();
    }

    public  class sendViewHolder extends RecyclerView.ViewHolder{

        ItemSentBinding binding;
        public sendViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemSentBinding.bind(itemView);


        }
    }

    public class receiveViewHolder extends RecyclerView.ViewHolder{

        ItemReceiveBinding binding;
        public receiveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemReceiveBinding.bind(itemView);



        }
    }






}
