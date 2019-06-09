package com.roh44x.medlinker;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.roh44x.medlinker.ViewHolder.MedicineViewHolder;
import com.roh44x.medlinker.ViewHolder.TreatmentViewHolder;
import com.roh44x.medlinker.components.Medicine;
import com.roh44x.medlinker.components.Treatment;

public class BreastCancer extends AppCompatActivity {
    private DatabaseReference mDatabase;


    private FirebaseRecyclerAdapter<Medicine, MedicineViewHolder> mAdapter;


    private RecyclerView mRecycler;


    private LinearLayoutManager mManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breast_cancer);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecycler = findViewById(R.id.mainRecycler);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Query medicineQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Medicine>()
                .setQuery(medicineQuery, Medicine.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Medicine, MedicineViewHolder>(options) {


            @NonNull
            @Override
            public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
                return new MedicineViewHolder(layoutInflater.inflate(R.layout.item_med, viewGroup, false));            }

            @Override
            protected void onBindViewHolder(@NonNull MedicineViewHolder holder, int position, @NonNull Medicine model) {

            }
        };

        mRecycler.setAdapter(mAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAdapter != null)
        {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAdapter != null){
            mAdapter.stopListening();
        }
    }


    public Query getQuery(DatabaseReference databaseReference)
    {
        return databaseReference.child("Users").child("disease")
                .limitToFirst(100);

    }
public String getUid(){
        return FirebaseAuth.getInstance().getUid();
}

}
