package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    Context context;
    ArrayList<BillModel> billList;

    public BillAdapter(Context context, ArrayList<BillModel> billList) {
        this.context = context;
        this.billList = billList;
    }

    public void updateData(List<BillModel> newData) {
        billList.clear();
       // notifyDataSetChanged();
        billList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_bill, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.MyViewHolder holder, int position) {
        BillModel model=billList.get(position);

        holder.categorie_factura.setText(model.getCategorie_factura());
        holder.suma_plata.setText(model.getSuma_plata());
        holder.termen_limita.setText(model.getTermen_limita());

        holder.delete_record.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance()
                        .collection("Facturi")
                        .document(model.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            billList.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> {
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView categorie_factura, suma_plata, termen_limita;
        View delete_record;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categorie_factura=itemView.findViewById(R.id.categorie_factura);
            suma_plata=itemView.findViewById(R.id.suma_plata_factura);
            termen_limita=itemView.findViewById(R.id.termen_limita);
            delete_record=itemView.findViewById(R.id.delete_record);
        }
    }
}
