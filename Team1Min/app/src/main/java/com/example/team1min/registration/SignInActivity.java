package com.example.team1min.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team1min.R;
import com.example.team1min.starting.HomePageActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient ;
    private static final int RC_SIGN_IN = 9003 ;
    private FirebaseAuth mAuth ;
    private CallbackManager mCallbackManager ;

    private CheckBox checkBox ;
    private TextInputEditText emailEditText, passwordEditText ;
    private String emailString, passwordString ;

    private String tutorName, tutorProfilePicUri, tutorEmail, tutorUid, tutorGender;

    private DatabaseReference myRefCandidateTutorInfo, myRefVerifiedTutorInfo, myRefApproveInfo;

    private int authFlag = 0 ;
    private String authType = "" ;

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Intent intent = getIntent() ;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        FacebookSdk.sdkInitialize(getApplicationContext());

        mAuth = FirebaseAuth.getInstance() ;

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null) {
            authFlag = 1 ;
            //isApprovedChecking(currentUser);
        }

        emailEditText = findViewById(R.id.email_edit_text) ;
        passwordEditText = findViewById(R.id.password_edit_text) ;
        checkBox = findViewById(R.id.checkbox) ;

        if(intent.getStringExtra("intentFlag")!=null){
            if(intent.getStringExtra("intentFlag").equals("googleSignIn")){
                goToSignInWithGoogle(null);
            }
            else if(intent.getStringExtra("intentFlag").equals("facebookSignIn")){
                goToSignInWithFacebook(null);
            }
        }

        rememberPasswordOperation("get",null,null);
    }

    public void rememberPasswordOperation(String type, String email, String password){
        if(type.equals("get")){
            SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
            String emailStr = pref.getString(PREF_EMAIL, null);
            String passwordStr = pref.getString(PREF_PASSWORD, null);

            if (emailStr == null || passwordStr == null) {
                return;
            }

            checkBox.setChecked(true);

            emailEditText.setText(emailStr);
            passwordEditText.setText(passwordStr);
        }
        else if(type.equals("put")){
            getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                    .edit()
                    .putString(PREF_EMAIL, email)
                    .putString(PREF_PASSWORD, password)
                    .commit();
        }
    }

    public void goToTutorSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void signInCompletion(View view){

        emailString = emailEditText.getText().toString().trim() ;
        passwordString = passwordEditText.getText().toString().trim() ;

        if(emailString.equals("")){
            emailEditText.setError("");
            return;
        }

        if(passwordString.equals("")){
            passwordEditText.setError("");
            return ;
        }

        if(checkBox.isChecked()){
            rememberPasswordOperation("put",emailString, passwordString);
        }else{
            rememberPasswordOperation("put",null, null);
        }

        if(!emailString.equals("") && !passwordString.equals("")) {
            mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser() ;
                        if(user.isEmailVerified()){
                            authType = "signIn" ;
                            //isApprovedChecking(user);
                            Toast.makeText(getApplicationContext(), "sign in successfully", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(getApplicationContext(), "something wrong", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "something wrong, try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        Intent intent  = new Intent(this,HomePageActivity.class);
        startActivity(intent);
        finish();

    }

    public void goToSignInWithGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null)
                    firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"something wrong google sign in, try again" ,Toast.LENGTH_SHORT).show() ;
            }
        }
        else{
            mCallbackManager.onActivityResult(requestCode,resultCode,data) ;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    authType = "google" ;
                    //isApprovedChecking(user);
                } else {
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToSignInWithFacebook(View view){
        mCallbackManager = CallbackManager.Factory.create() ;

        LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            authType = "facebook" ;
                           // isApprovedChecking(user);
                        } else {
                            Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void goToVerifiedTutorHomePageActivity(){
        ArrayList<String> userInfo = new ArrayList<>() ;
        userInfo.add(tutorName) ;
        if(tutorProfilePicUri == null){
            if(mAuth.getCurrentUser().getPhotoUrl()==null){
                userInfo.add("") ;
            }
            else {
                userInfo.add(mAuth.getCurrentUser().getPhotoUrl().toString()) ;
            }
        }
        else {
            userInfo.add(tutorProfilePicUri) ;
        }
        userInfo.add(tutorEmail);
        userInfo.add(tutorUid);
        userInfo.add(tutorGender);


        if(tutorEmail.charAt(0)=='-'){
            tutorEmail = tutorEmail.substring(1,tutorEmail.length()) ;
        }

        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putStringArrayListExtra("userInfo",userInfo);
        startActivity(intent);
        finish();
    }


    public void goToBackPageActivity(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        goToBackPageActivity(null);
    }

    public void showDialogForForgotPassword(View view){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_adapter_for_dialog_box_forgot_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText editText = dialog.findViewById(R.id.email_edit_text) ;
        final TextInputLayout textInputLayout = dialog.findViewById(R.id.textInputLayout) ;
        final Button submit = dialog.findViewById(R.id.submit) ;
        final TextView textViewMessage = dialog.findViewById(R.id.message) ;
        final ProgressBar progressBar = dialog.findViewById(R.id.progress_bar) ;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString() ;
                progressBar.setVisibility(View.VISIBLE);

                if(!email.equals("")){
                    mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "A Link has been sent to this email.", Toast.LENGTH_SHORT).show();
                            textViewMessage.setText("A Reset Link has been sent to this email. Please Check Your Email.");
                            textViewMessage.setTextSize(15);
                            textViewMessage.setTextColor(Color.WHITE);
                            submit.setVisibility(View.INVISIBLE);
                            textInputLayout.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }) ;
                }
                else{
                    editText.setError("");
                }
            }
        });

        dialog.show();
    }
}