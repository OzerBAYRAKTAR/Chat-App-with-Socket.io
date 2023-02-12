package com.example.chatapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.adapter.ChatAdapter;
import com.example.chatapp.model.ChatModel;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int MESSAGE_SENDER_ME = 1;
    private static final int MESSAGE_SENDER_OTHER = 2;
    ActivityMainBinding binding;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatModel> mChatList= new ArrayList<>();
    private Socket mSocket;
    private boolean isConnected;
    private String myUserName = "funs";
    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        items();
        mSocket.connect();
        onSocketConnect();

        binding.btnSend.setOnClickListener(this);

    }

    private void onSocketConnect() {
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnected);
        mSocket.on(Socket.EVENT_CONNECT_ERROR,onDisconnected);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT,onDisconnected);
        mSocket.on("new message",onNewMessage);
        mSocket.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT,onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT,onDisconnected);
        mSocket.off(Socket.EVENT_CONNECT_ERROR,onDisconnected);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT,onDisconnected);
        mSocket.off("new message",onNewMessage);
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
                    mChatList.add(new ChatModel(message,myUserName,MESSAGE_SENDER_ME));
                    binding.etChatBox.setText("");
                    mSocket.emit("new Message", message);
                    if (chatAdapter != null) {
                        chatAdapter.notifyDataSetChanged();
                    }
                }


                break;

        }
    }
    private Emitter.Listener onConnect= new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        mSocket.emit("add user", myUserName);
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        isConnected = true;
                    }

                }
            });
        }
    };
    private Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                }
            });

        }
    };
    private Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject dataReceived = (JSONObject) args[0];
                    String userName, message;
                    try {
                        userName = dataReceived.getString("username");
                        message = dataReceived.getString("message ");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();
                    mChatList.add(new ChatModel(message,myUserName,MESSAGE_SENDER_OTHER));
                    binding.etChatBox.setText("");
                    if (chatAdapter != null) {
                        chatAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };
}
