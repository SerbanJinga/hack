package com.roh44x.medlinker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DoctorVerify extends AppCompatActivity implements View.OnClickListener {

    public EditText diplomaURL;
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        diplomaURL = (EditText)findViewById(R.id.diploma);
        button = (Button)findViewById(R.id.butonache);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.butonache) {
            Toast.makeText(this, "Access as a doctor requested. It might take a few minutes",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DoctorVerify.this, HomeActivity.class));
        }
    }


}
