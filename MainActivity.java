package com.example.team1min.starting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.team1min.R;
import com.example.team1min.registration.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startModule();
    }

    public void startModule()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();

        /*if (user != null) {
            String phoneNumber = user.getPhoneNumber() ;
            String email = user.getEmail() ;

            if(!email.equals("")){
                signUpCompletionChecking(user);
            }
            else if(!phoneNumber.equals("")){
                goToNextPage();
            }

            else {
                goToHomePageActivity();
            }
        }

        else {
            goToHomePageActivity();
        }*/

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToHomePageActivity(){
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }
}