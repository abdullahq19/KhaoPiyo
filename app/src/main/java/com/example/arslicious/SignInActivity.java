package com.example.arslicious;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    AppCompatButton btnSignIn, btnSign_up;
    EditText etSignInEmail, etSignInPassword;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnSignIn = findViewById(R.id.btnSignIn);
        etSignInEmail = findViewById(R.id.etSignInEmail);
        etSignInPassword = findViewById(R.id.etSignInPassword);
        btnSign_up = findViewById(R.id.btnSign_up);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

//       for Status bar colour
        getWindow().setStatusBarColor(ContextCompat.getColor(SignInActivity.this, R.color.teal_700));

//        For Sign Up Activity
        btnSign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

//        For Email and Password Check
        auth = FirebaseAuth.getInstance();
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = etSignInEmail.getText().toString();
                String txt_Password = etSignInPassword.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_Password)) {
                    Toast.makeText(SignInActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txt_email, txt_Password);
                    progressBar.setVisibility(View.VISIBLE);
//                    ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
//                    progressDialog.setMessage("Processing please wait");
//                    progressDialog.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
//            there is some user Logged in
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
        }
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignInActivity.this, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        });
    }
}