package com.example.prezentownik.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prezentownik.R;
import com.example.prezentownik.models.GiftList;

import java.util.List;

public class TestowyAdapter extends RecyclerView.Adapter<TestowyAdapter.TestowyViewHolder> {

    private List<GiftList> listModels;

    public void setListModels(List<GiftList> listModels) {
        this.listModels = listModels;
    }

    @NonNull
    @Override
    public TestowyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new TestowyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestowyViewHolder holder, int position) {
        holder.listName.setText(listModels.get(position).getListName());
        holder.listBudget.setText(String.valueOf(listModels.get(position).getListBudget()));
    }

    @Override
    public int getItemCount() {
        if (listModels == null){
            return 0;
        } else {return listModels.size();}
    }

    public class TestowyViewHolder extends RecyclerView.ViewHolder{
        private TextView listName;
        private TextView listBudget;

        public TestowyViewHolder(@NonNull View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.person_name);
            listBudget = itemView.findViewById(R.id.gift_quantity);

        }
    }
}
