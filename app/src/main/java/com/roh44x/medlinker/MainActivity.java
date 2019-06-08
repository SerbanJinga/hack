package com.roh44x.medlinker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roh44x.medlinker.R;
import com.roh44x.medlinker.components.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public FirebaseAuth mAuth;
    public FirebaseDatabase mDatabase;
    public DatabaseReference userDatabase, doctorDatabase;

    public EditText etFirstName, etLastName, etEmail, etPassword, etRepeatPassword, etAge;
    public Button btnSignUp, btnHaveAccount;
    public CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();

        etFirstName = (EditText)findViewById(R.id.etFirstName);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etRepeatPassword = (EditText)findViewById(R.id.etRepeatPass);
        etAge = (EditText)findViewById(R.id.etAge);
        btnSignUp = (Button)findViewById(R.id.btnRegister);
        btnHaveAccount = (Button)findViewById(R.id.btnAlreadyAcc);
        check = (CheckBox)findViewById(R.id.checkBox);

        btnSignUp.setOnClickListener(this);
        btnHaveAccount.setOnClickListener(this);
    }

    public void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        userDatabase = mDatabase.getReference();
        doctorDatabase = mDatabase.getReference();
    }

    public void signUpWithEmailAndPassword(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email",Toast.LENGTH_SHORT).show();
            etEmail.startAnimation(shakeError());
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password",Toast.LENGTH_SHORT).show();
            etPassword.startAnimation(shakeError());
            return;
        }
        if(TextUtils.isEmpty(etFirstName.getText().toString())){
            Toast.makeText(this, "Please enter your First Name",Toast.LENGTH_SHORT).show();
            etFirstName.startAnimation(shakeError());
            return;
        }
        if(TextUtils.isEmpty(etLastName.getText().toString())){
            Toast.makeText(this, "Please enter your Second Name",Toast.LENGTH_SHORT).show();
            etLastName.startAnimation(shakeError());
            return;
        }
        if(TextUtils.isEmpty(etRepeatPassword.getText().toString())){
            Toast.makeText(this, "Please repeat your password",Toast.LENGTH_SHORT).show();
            etRepeatPassword.startAnimation(shakeError());
            return;
        }
        if(!etPassword.getText().toString().equals(etRepeatPassword.getText().toString())){
            Toast.makeText(this, "Passwords don't match",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if(check.isChecked()){
                                storeDoctorInDatabase();
                            }else{
                            storeUserInDatabase();
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void storeUserInDatabase() {
        final User user = new User(etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), "", Integer.parseInt(etAge.getText().toString()));
        final String id = mAuth.getCurrentUser().getUid();
        userDatabase.child("Users").child(id).setValue(user).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                       startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                 else {
                    String error = task.getException().getMessage();
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void storeDoctorInDatabase(){
            Doctor doctor = new Doctor(etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString(), Integer.parseInt(etAge.getText().toString()), false, "");
            doctorDatabase.child("Doctors").child(mAuth.getCurrentUser().getUid()).setValue(doctor).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(MainActivity.this, DoctorVerify.class));
                    }else{
                        String error = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    public TranslateAnimation shakeError(){
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.btnRegister) {
            signUpWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString());
        } else if(i == R.id.btnAlreadyAcc){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }


}
