package com.example.lasttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ForgetPassword extends AppCompatActivity {
    Button forgot_btn;
    EditText forgot_email;
    TextView login_with_other_account;
    //firebase variables
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        forgot_btn = (Button)findViewById(R.id.forgot_btn);
        forgot_email = (EditText)findViewById(R.id.forgot_email);
        login_with_other_account = (TextView)findViewById(R.id.login_with_other_account);

        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = forgot_email.getText().toString();
                if(e.equals("")){
                    Toasty.error(ForgetPassword.this,"Please enter your email", Toast.LENGTH_SHORT,true).show();
                }
                mAuth.sendPasswordResetEmail(e)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toasty.success(ForgetPassword.this,"Password Reset send to you mail",Toast.LENGTH_SHORT,true).show();
                                    startActivity(new Intent(ForgetPassword.this,login.class));
                                    finish();
                                }
                                else{
                                    Toasty.error(ForgetPassword.this,task.getException().getMessage(),Toast.LENGTH_SHORT,true).show();
                                }
                            }
                        });
            }
        });

        login_with_other_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPassword.this,login.class));
                finish();
            }
        });
    }
}
