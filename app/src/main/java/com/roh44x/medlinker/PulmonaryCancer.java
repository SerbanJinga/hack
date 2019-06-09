package com.roh44x.medlinker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PulmonaryCancer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulmonary_cancer);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
