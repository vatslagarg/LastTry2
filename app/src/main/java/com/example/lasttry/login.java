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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class login extends AppCompatActivity {
    Button login_btn;
    EditText email,password;
    TextView create_new_account,forgot_password;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        login_btn = (Button)findViewById(R.id.login_btn);
        create_new_account = (TextView)findViewById(R.id.create_new_account);
        forgot_password = (TextView)findViewById(R.id.forgot_password);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString().trim();
                String p = password.getText().toString().trim();
                if (e.equals("") || p.equals("")) {
                    Toasty.error(login.this,"All fileds are required", Toast.LENGTH_SHORT,true).show();
                } else if (p.length() < 3) {
                    Toasty.error(login.this,"Password is too short",Toast.LENGTH_SHORT,true).show();
                } else {
                    Toasty.info(login.this,"Authenticating...",Toasty.LENGTH_SHORT,true).show();
                    mAuth.signInWithEmailAndPassword(e,p)
                            .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(user.isEmailVerified()) {
                                            Toasty.success(login.this, "Authenticated", Toasty.LENGTH_SHORT, true).show();
                                            startActivity(new Intent(login.this, Dashboard.class));
                                            finish();
                                        }
                                        else{
                                            Toasty.error(login.this,"Email is not verified check you mail to get verification",Toasty.LENGTH_SHORT,true).show();
                                        }
                                    }
                                    else{
                                        Toasty.error(login.this,task.getException().getMessage(),Toasty.LENGTH_SHORT,true).show();
                                    }
                                }
                            });

                }
            }
        });

        create_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, Signup.class));
                finish();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,ForgetPassword.class));
                finish();
            }
        });
    }
}
