package com.example.team1min.ChatBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.team1min.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatBox extends AppCompatActivity
{
    private ImageButton btn_send, img_send;
    private EmojiconEditText text_send;
    private ImageView emoji_btn;
    private View rootView;
    private EmojIconActions emojIcon;
    private FirebaseUser fuser;
    private List<Chat> mChat;
    private RecyclerView recyclerView;
    private MessageAdapter  messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        btn_send = findViewById(R.id.btn_send);
        img_send = findViewById(R.id.img_send);
        text_send = findViewById(R.id.text_send);
        emoji_btn = findViewById(R.id.emoji_btn);
        rootView = findViewById(R.id.root_view);
        emojIcon = new EmojIconActions(this, rootView, text_send, emoji_btn);
        emojIcon.ShowEmojIcon();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        readMessages(fuser.getUid());

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(), msg);
                }

                else{
                    Toast.makeText(ChatBox.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }

                text_send.setText("");
            }
        });


    }

    private void readMessages(String uid) {
        mChat = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    mChat.add(chat);
                      messageAdapter = new MessageAdapter(ChatBox.this,mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(final String sender, final String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
        userReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getEmail().equals(fuser.getEmail())){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("sender", sender);
                        hashMap.put("message", message);
                        hashMap.put("name", user.getName());

                        reference.child("Chats").push().setValue(hashMap);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void selectImageForMessage(View view) {
    }
}