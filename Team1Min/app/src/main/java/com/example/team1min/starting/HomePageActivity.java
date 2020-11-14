package com.example.team1min.starting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.team1min.ChatBox.ChatBox;
import com.example.team1min.R;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void goToChatBox(View view) {
        Intent intent  = new Intent(this, ChatBox.class);
        startActivity(intent);
        finish();
    }
}