package com.roh44x.medlinker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDiagnosis extends AppCompatActivity implements View.OnClickListener {



    private EditText diseaseField;
    private Button addDisease;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diagnosis);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("disease");

        diseaseField = (EditText) findViewById(R.id.addDisease);
        addDisease = (Button) findViewById(R.id.addDiseasebtn);

        addDisease.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if(i == R.id.addDiseasebtn){
            mDatabase.setValue(diseaseField.getText().toString());
        }
    }
}
