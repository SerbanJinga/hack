package com.roh44x.medlinker.ViewHolder;

import android.app.PendingIntent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roh44x.medlinker.R;
import com.roh44x.medlinker.components.Treatment;

public class TreatmentViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView authorView;
    public ImageView usefulView;
    public TextView numUsefulView;
    public TextView bodyView;
    public TextView ratingView;
    public TextView medicationView;

    public TreatmentViewHolder(View itemView)
    {
        super(itemView);


        titleView = itemView.findViewById(R.id.postTitle);
        authorView = itemView.findViewById(R.id.postAuthor);
        usefulView = itemView.findViewById(R.id.useful);
        numUsefulView = itemView.findViewById(R.id.postNumUseful);
        bodyView = itemView.findViewById(R.id.postBody);
        ratingView = itemView.findViewById(R.id.rating);
        medicationView = itemView.findViewById(R.id.medication);
    }

    public void bindToTreatment(Treatment treatment, View.OnClickListener usefulClickListener)
    {
        titleView.setText(treatment.title);
        authorView.setText(treatment.author);
        numUsefulView.setText(String.valueOf(treatment.usefulCount));
        bodyView.setText(treatment.body);
        ratingView.setText(treatment.rating);
        medicationView.setText(treatment.medication);

        usefulView.setOnClickListener(usefulClickListener);
    }
}
