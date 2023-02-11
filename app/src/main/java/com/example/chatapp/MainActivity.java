package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.chatapp.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    private ChatAdapter chatAdapter;
    private ArrayList<String> mChatList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        items();

        binding.btnSend.setOnClickListener(this);

    }

    private void items() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        binding.chatRv.setLayoutManager(linearLayoutManager);
        chatAdapter=new ChatAdapter(mChatList);
        binding.chatRv.setAdapter(chatAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Send:
                String message=binding.etChatBox.getText().toString().trim();
                if (message != null && !TextUtils.isEmpty(message)) {
                    mChatList.add(message);
                    if (chatAdapter != null) {
                        chatAdapter.notifyDataSetChanged();
                    }
                }


                break;

        }
    }
}