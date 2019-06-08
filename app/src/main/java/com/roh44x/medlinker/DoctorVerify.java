package com.roh44x.medlinker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roh44x.medlinker.components.Doctor;

public class DoctorVerify extends AppCompatActivity implements View.OnClickListener {

    public EditText diplomaURL;
    public Button button;
    private FirebaseAuth mAuth;
    private DatabaseReference doctorsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);


        initFirebase();
        diplomaURL = (EditText)findViewById(R.id.diploma);
        button = (Button)findViewById(R.id.butonache);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUrl(diplomaURL.getText().toString());
            }
        });

    }

    private void initFirebase(){
        mAuth = FirebaseAuth.getInstance();
        doctorsReference = FirebaseDatabase.getInstance().getReference().child("Doctors").child(mAuth.getCurrentUser().getUid());
    }

    private void submitUrl(String url){

        doctorsReference.child("isConfirmed").setValue(true);
        doctorsReference.child("url").setValue(url).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    startActivity(new Intent(DoctorVerify.this, HomeActivity.class));
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(DoctorVerify.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.butonache) {
            Toast.makeText(this, "Access as a doctor requested. It might take a few minutes",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DoctorVerify.this, HomeActivity.class));
        }
    }


}
