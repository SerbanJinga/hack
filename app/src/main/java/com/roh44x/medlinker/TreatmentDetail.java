package com.roh44x.medlinker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.roh44x.medlinker.components.Comment;
import com.roh44x.medlinker.components.Doctor;
import com.roh44x.medlinker.components.Treatment;
import com.roh44x.medlinker.components.User;

import java.util.ArrayList;
import java.util.List;

public class TreatmentDetail extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "TreatmentDetail";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private CommentAdapter mAdapter;

    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    private EditText mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;

    private DatabaseReference userOrDoctorReference, altDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_detail);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        userOrDoctorReference = FirebaseDatabase.getInstance().getReference();



        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if(mPostKey == null)
        {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("posts").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference().child("post-comments").child(mPostKey);

        mAuthorView = findViewById(R.id.postAuthor);
        mTitleView = findViewById(R.id.postTitle);
        mBodyView = findViewById(R.id.postBody);
        mCommentField = findViewById(R.id.fieldCommentText);
        mCommentButton = findViewById(R.id.buttonPostComment);
        mCommentsRecycler = findViewById(R.id.recyclerPostComments);

        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));



    }


    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Treatment treatment = dataSnapshot.getValue(Treatment.class);
                mAuthorView.setText(treatment.author);
                mTitleView.setText(treatment.title);
                mBodyView.setText(treatment.body);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(TreatmentDetail.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mDatabaseReference.addValueEventListener(postListener);

        mPostListener = postListener;

        mAdapter = new CommentAdapter(this, mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mPostListener != null)
        {
            mDatabaseReference.removeEventListener(mPostListener);
        }

        mAdapter.cleanupLisener();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if(i == R.id.buttonPostComment){
            postComment();
        }


    }

    private void postComment(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Doctors").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Doctor doctor = dataSnapshot.getValue(Doctor.class);
                        String author = doctor.firstName + " " + doctor.lastName;

                        String commentText = mCommentField.getText().toString();
                        Comment comment = new Comment(uid, author, commentText);

                        mCommentsReference.push().setValue(comment);

                        mCommentField.setText(null);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder{
        public TextView authorView;
        public TextView bodyView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            authorView = itemView.findViewById(R.id.commentAuthor);
            bodyView = itemView.findViewById(R.id.commentBody);
        }
    }


    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{
        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentsIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference ref){
            mContext = context;
            mDatabaseReference = ref;

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    Comment comment =  dataSnapshot.getValue(Comment.class);

                    mCommentsIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentsIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        mComments.set(commentIndex, newComment);

                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentsIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Remove data from the list
                        mCommentsIds.remove(commentIndex);
                        mComments.remove(commentIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };

            ref.addChildEventListener(childEventListener);


            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position)
        {
            Comment comment = mComments.get(position);
            holder.authorView.setText(comment.author);
            holder.bodyView.setText(comment.text);
        }

        @Override
        public int getItemCount(){
            return mComments.size();
        }

        public void cleanupLisener(){
            if(mChildEventListener != null)
            {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }
    }

}


