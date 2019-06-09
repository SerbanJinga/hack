package com.roh44x.medlinker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CancerActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancer);

        findViewById(R.id.bone_cancer).setOnClickListener(this);
        findViewById(R.id.breast_cancer).setOnClickListener(this);
        findViewById(R.id.pulmonary_cancer).setOnClickListener(this);
        findViewById(R.id.skin_cancer).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if(i == R.id.bone_cancer)
        {
            startActivity(new Intent(CancerActivity.this, BoneCancer.class));
        }else if(i == R.id.breast_cancer){
            startActivity(new Intent(CancerActivity.this, BreastCancer.class));
        }else if(i == R.id.pulmonary_cancer){
            startActivity(new Intent(CancerActivity.this, PulmonaryCancer.class));
        }else if(i == R.id.skin_cancer){
            startActivity(new Intent(CancerActivity.this, SkinCancer.class));
        }
    }
}
