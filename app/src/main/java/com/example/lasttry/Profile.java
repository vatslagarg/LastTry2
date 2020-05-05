package com.example.lasttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class Profile extends AppCompatActivity {
    Button submit_btn,date_btn;
    TextView state_name,date_text;
    EditText name;
    CheckBox check_btn;
    Calendar calender;
    DatePickerDialog datePickerDialog;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        submit_btn = (Button) findViewById(R.id.submit_btn);
        state_name =(TextView)findViewById(R.id.state_name);
        name= (EditText)findViewById(R.id.name);
        check_btn = (CheckBox)findViewById(R.id.check_btn);
        date_btn = (Button)findViewById(R.id.date_btn);
        date_text=(TextView)findViewById(R.id.date_text);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        String uid =mAuth.getCurrentUser().getUid();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(uid);


     Spinner spinner = (Spinner) findViewById(R.id.state_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.india_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s= parent.getItemAtPosition(position).toString();
                state_name.setText(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = name.getText().toString();
                String s = state_name.getText().toString();
                String d = date_text.getText().toString();
                if(n.equals("")){
                    Toasty.error(Profile.this,"Enter your name",Toast.LENGTH_SHORT,true).show();
                }
                else if(s.equals("Choose State")){
                    Toasty.error(Profile.this,"Choose your state",Toast.LENGTH_SHORT,true).show();
                }
                else if(!check_btn.isChecked()){
                    Toasty.error(Profile.this,"Privacy Policy is not checked",Toast.LENGTH_SHORT,true).show();
                }
                else if(d.equals("date of birth")){
                    Toasty.error(Profile.this,"Set Date of Birth",Toast.LENGTH_SHORT,true).show();
                }
                else {
                    myRef.child("name").setValue(n);
                    myRef.child("state").setValue(s);
                    myRef.child("points").setValue(0);
                    myRef.child("profile").setValue(1);
                    myRef.child("dob").setValue(d);
                    Toasty.success(Profile.this,"Successfully updated",Toast.LENGTH_SHORT,true).show();
                    startActivity(new Intent(Profile.this, Dashboard.class));

                }
            }
        });

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calender = Calendar.getInstance();
                int day = calender.get(Calendar.DAY_OF_MONTH);
                int month = calender.get(Calendar.MONTH);
                int year = calender.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(Profile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date_text.setText(i+"/"+(i1+1)+"/"+i2);
                    }
                },day,month,year);
                datePickerDialog.show();
            }
        });

    }


}
