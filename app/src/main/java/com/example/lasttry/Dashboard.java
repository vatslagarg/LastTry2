package com.example.lasttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import es.dmoral.toasty.Toasty;

public class Dashboard extends AppCompatActivity {
    Button logout_btn,profile_btn,create_link_btn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        logout_btn = (Button)findViewById(R.id.logout_btn);
        profile_btn = (Button)findViewById(R.id.profile_btn);
        create_link_btn = (Button)findViewById(R.id.create_link_btn);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toasty.success(Dashboard.this,"Successfully Logout",Toast.LENGTH_SHORT,true).show();
                startActivity(new Intent(Dashboard.this,login.class));
                finish();
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(Dashboard.this,UserProfile.class));
            }
        });


        create_link_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReferlink(uid);
            }

            private void createReferlink(String uid) {
                String sharelinktext = "https://lasttry.page.link/?" +
                        "link=http://www.lasttry.com/myrefer.php?uid=" +uid+ "-" +
                        "&apn=" + getPackageName() +
                        "&st=" + "My Refer Link" +
                        "&sd=" + "Reward coins 20" +
                        "&si=" + "https://drive.google.com/open?id=10Zu2KRDshU-4q7_s2SrABd-VKQGa-Ra1";

                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLongLink(Uri.parse(sharelinktext))
                        .buildShortDynamicLink()
                        .addOnCompleteListener(Dashboard.this, new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                    Log.e("main", "short " + shortLink);

                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                                    intent.setType("text/plain");
                                    startActivity(intent);

                                } else {
                                    Log.e("main", "error" + task.getException());
                                }
                            }
                        });
            }


            });

        }
}