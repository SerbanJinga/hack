package com.roh44x.medlinker.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.roh44x.medlinker.R;
import com.roh44x.medlinker.components.Medicine;

public class MedicineViewHolder extends RecyclerView.ViewHolder {
    public TextView medName;
    public TextView warnings;

    public MedicineViewHolder(View itemView){
        super(itemView);

        medName = itemView.findViewById(R.id.medicine);
        warnings = itemView.findViewById(R.id.warnings);
    }

    public void bindToMedicine(Medicine medicine)
    {
        medName.setText(medicine.medName);
        warnings.setText(medicine.warnings);
    }

}
