package com.magicalmethods.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.magicalmethods.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by SWE
 */

public class RegisterActivity extends AppCompatActivity {

    ImageView image1;
    private ProgressDialog progressDialog;
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        image1 = (ImageView) findViewById(R.id.imageView1);
        btnSignIn = (Button) findViewById(R.id.sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_Register);
        inputEmail = (EditText) findViewById(R.id.frg_email);
        inputPassword = (EditText) findViewById(R.id.password);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                inputEmail.setText("");
                inputPassword.setText("");
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Please wait...");
                progressDialog.show();


                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               // Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (task.isSuccessful()) {
                                    progressDialog.setMessage("Registering user...");
                                    progressDialog.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendEmailVerification();
                                            progressDialog.cancel();

                                            new AlertDialog.Builder(RegisterActivity.this)
                                                    .setMessage("Registered Successfully. Please check your mail to activate account.")
                                            .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                }
                                            })
                                            .setCancelable(false)
                                            .show();
                                        }
                                    }, 2000);


                                    //finish();
                                } else {
                                    progressDialog.cancel();
                                    Exception e=task.getException();
                                    Toast.makeText(RegisterActivity.this, "Authentication failed." + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Check your Email for verification", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       /* if(newConfig.orientation== Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(getApplicationContext(),"You are in Portrait mode",Toast.LENGTH_SHORT).show();
        }
        else if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(getApplicationContext(),"You are in Landscape mode",Toast.LENGTH_SHORT).show();

        }*/
    }

    @Override
    public void onBackPressed() {
        
    }
}
