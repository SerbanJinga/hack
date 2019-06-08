package com.roh44x.medlinker;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roh44x.medlinker.components.Treatment;
import com.roh44x.medlinker.components.User;

import java.util.HashMap;
import java.util.Map;

public class AddTreatment extends AppCompatActivity {


    private static final String TAG = "AddTreatment";
    private static final String REQUIRED = "required";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText titleField, contentField, medicationField;
    private Button btnAddTreatment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treatment);

        initFirebase();

        titleField = (EditText) findViewById(R.id.titleTreatment);
        contentField = (EditText) findViewById(R.id.contentTreatment);
        medicationField = (EditText) findViewById(R.id.medicationTreatment);
        btnAddTreatment = (Button) findViewById(R.id.btnAddTreatment);

        btnAddTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTreatment();
            }
        });

    }



    private void initFirebase(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    private void submitTreatment(){
        final String title = titleField.getText().toString();
        final String content = contentField.getText().toString();
        final String medication = medicationField.getText().toString();
        if (TextUtils.isEmpty(title)) {
            titleField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(content)) {
            contentField.setError(REQUIRED);
            return;
        }


        if (TextUtils.isEmpty(medication)) {
            medicationField.setError(REQUIRED);
            return;
        }

        final String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    // User is null, error out
                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                    Toast.makeText(AddTreatment.this,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                }else {
                    writeNewTreatment(userId, user.firstName + " " + user.lastName, title, content, medication);
                }

                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void writeNewTreatment(String userId, String username, String title, String content, String medication){
        String key = mDatabase.child("treatments").push().getKey();
        Treatment treatment = new Treatment(userId, title, username, content, medication);
        Map<String, Object> treatmentValues = treatment.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, treatmentValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, treatmentValues);

        mDatabase.updateChildren(childUpdates);
    }
}
