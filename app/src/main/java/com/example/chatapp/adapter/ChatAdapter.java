package com.example.chatapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.model.ChatModel;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private ArrayList<ChatModel> mChatList;
    public ChatAdapter(ArrayList<ChatModel> mChatList) {
        this.mChatList=mChatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater lf=LayoutInflater.from(parent.getContext());
        View view=lf.inflate(R.layout.item_chat,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        switch (mChatList.get(position).getSender()){
            case 1:
                holder.mTextViewRight.setText(mChatList.get(position).getMessage());
                holder.mTextViewRight.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.mTextViewLeft.setText(mChatList.get(position).getMessage());
                holder.mTextViewLeft.setVisibility(View.VISIBLE);

                break;
        }

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView mTextViewLeft,mTextViewRight;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewLeft=itemView.findViewById(R.id.chat_left);
            mTextViewRight=itemView.findViewById(R.id.chat_right);
        }
    }
}
