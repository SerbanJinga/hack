package com.roh44x.medlinker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.roh44x.medlinker.R;
import com.roh44x.medlinker.TreatmentDetail;
import com.roh44x.medlinker.ViewHolder.TreatmentViewHolder;
import com.roh44x.medlinker.components.Treatment;

public class ProfileFragment extends Fragment {
    private DatabaseReference mDatabase;


    private FirebaseRecyclerAdapter<Treatment, TreatmentViewHolder> mAdapter;


    private RecyclerView mRecycler;


    private LinearLayoutManager mManager;

    public ProfileFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, null);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecycler = view.findViewById(R.id.profilePostsRecycler);
        mRecycler.setHasFixedSize(true);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Treatment>()
                .setQuery(postsQuery, Treatment.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Treatment, TreatmentViewHolder>(options) {


            @NonNull
            @Override
            public TreatmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
                return new TreatmentViewHolder(layoutInflater.inflate(R.layout.item_treatment, viewGroup, false));
            }


            @Override
            protected void onBindViewHolder(@NonNull TreatmentViewHolder holder, int position, @NonNull final Treatment model) {
                final DatabaseReference posRef = getRef(position);

                final String postKey = posRef.getKey();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), TreatmentDetail.class);
                        intent.putExtra(TreatmentDetail.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                if(model.usefuls.containsKey(getUid())){
                    holder.usefulView.setImageResource(R.drawable.ic_toggle_star_24);
                }else{
                    holder.usefulView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                holder.bindToTreatment(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference globalPostRef = mDatabase.child("posts").child(posRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(posRef.getKey());

                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
            }

        };

        mRecycler.setAdapter(mAdapter);

    }

    private void onStarClicked(DatabaseReference postRef)
    {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Treatment t = mutableData.getValue(Treatment.class);
                if(t == null)
                {
                    return Transaction.success(mutableData);
                }

                if(t.usefuls.containsKey(getUid())) {
                    t.usefulCount = t.usefulCount - 1;
                    t.usefuls.remove(getUid());
                }else{
                    t.usefulCount = t.usefulCount + 1;
                    t.usefuls.put(getUid(), true);
                }

                mutableData.setValue(t);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
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
        return databaseReference.child("user-posts")
                .child(getUid());
    }

    public String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
