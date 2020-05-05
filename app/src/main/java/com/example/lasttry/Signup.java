package com.example.lasttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import es.dmoral.toasty.Toasty;

public class Signup extends AppCompatActivity {
    Button signup_btn;
    public String reflink="";
    EditText email,password,repassword,name;
    TextView already_have_account;
    public int a=0;
    //firebase variables
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        repassword = (EditText)findViewById(R.id.repassword);
        signup_btn = (Button)findViewById(R.id.signup_btn);
        already_have_account = (TextView)findViewById(R.id.already_have_account);
        signup_btn.setEnabled(false);
        signup_btn.setText("Can't Signup");
        // Write a message to the database


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String referlink = deepLink.toString();
                            try
                            {
                                referlink = referlink.substring(referlink.lastIndexOf("=")+1);
                                String custid = referlink.substring(0, referlink.indexOf("-"));
                                reflink = custid;
                                Toasty.success(Signup.this,"Refrral link is there",Toast.LENGTH_LONG,true).show();
                                signup_btn.setEnabled(true);
                                signup_btn.setText("Signup");
                            }catch (Exception e)
                            {
                                Toasty.error(Signup.this,""+e.toString(),Toast.LENGTH_LONG,true).show();
                            }

                        }
                        else{
                            Toasty.error(Signup.this,"No Refrral link is there",Toast.LENGTH_LONG,true).show();
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(Signup.this,""+e.toString(),Toast.LENGTH_LONG,true).show();
                    }
                });






        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String e = email.getText().toString().trim();
                String p = password.getText().toString().trim();
                String rp = repassword.getText().toString().trim();

                if(e.equals("") || p.equals("") || rp.equals("")){
                    Toasty.error(Signup.this,"All fileds are required",Toast.LENGTH_SHORT,true).show();
                }
                else if(p.length()<5){
                    Toasty.error(Signup.this,"Password is short",Toast.LENGTH_SHORT,true).show();
                }
                else if(!p.equals(rp)){
                    Toasty.error(Signup.this,"Password don't match",Toast.LENGTH_SHORT,true).show();
                }
                else {
                    Toasty.info(Signup.this,"We are Signing you in",Toasty.LENGTH_SHORT,true).show();
                    mAuth.createUserWithEmailAndPassword(e,p)
                            .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                user.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toasty.success(Signup.this,"Registerd,Check ur mail to get verified",Toasty.LENGTH_LONG,true).show();


                                                                    giveReferPointsToSender(reflink);
                                                                    startActivity(new Intent(Signup.this, login.class));
                                                                    finish();
                                                                }
                                                                else{
                                                                    Toasty.error(Signup.this,task.getException().getMessage(),Toast.LENGTH_SHORT,true).show();
                                                                }
                                                            }

                                                            private void giveReferPointsToSender(String reflink) {
                                                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                            final DatabaseReference myRef = database.getReference(reflink);
                                                                            myRef.addValueEventListener(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                      a = Integer.parseInt(dataSnapshot.child("points").getValue().toString());

                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }

                                                                            });
                                                                a = a+100;
                                                                myRef.child("points").setValue(a);


                                                            }
                                                        });

                                    }
                                    else{
                                        Toasty.error(Signup.this,task.getException().getMessage(),Toast.LENGTH_SHORT,true).show();
                                    }
                                }
                            });

                }
            }
        });

        already_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this, login.class));
                finish();
            }
        });
    }
}
