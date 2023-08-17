package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.MyViewHolder>{
    Context context;
    ArrayList<ExpenseModel> transList;

    public ExpensesAdapter(Context context, ArrayList<ExpenseModel> transList) {
        this.context = context;
        this.transList = transList;
    }

    public void updateData(List<ExpenseModel> newData) {
        transList.clear();
        transList.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ExpenseModel model=transList.get(position);
        String prioritate=model.getTip();
        if(prioritate.equals("Cheltuiala")){
            holder.prioritate.setBackgroundResource(R.drawable.red_priority);
        }else {
            holder.prioritate.setBackgroundResource(R.drawable.green_priority);
        }
        holder.suma.setText(model.getSuma());
        holder.data.setText(model.getData());
        holder.categorie.setText(model.getCategorie());
        holder.nota.setText(model.getNota());
    }

    @Override
    public int getItemCount() {
        return transList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView categorie, suma, data, nota;
        View prioritate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categorie=itemView.findViewById(R.id.categorie_tranzactie);
            suma=itemView.findViewById(R.id.suma_tranzactie);
            data=itemView.findViewById(R.id.data_tranzactie);
            nota=itemView.findViewById(R.id.nota_tranzactie);
            prioritate=itemView.findViewById(R.id.prioritate_green);
        }
    }

}
