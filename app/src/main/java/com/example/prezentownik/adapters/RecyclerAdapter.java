package com.example.prezentownik.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prezentownik.GiftsActivity;
import com.example.prezentownik.MainActivity;
import com.example.prezentownik.R;
import com.example.prezentownik.models.Gift;
import com.example.prezentownik.models.Person;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    RecyclerAdapter.OnRecyclerItemClicked onRecyclerItemClicked;

    private static final String TAG = "RecyclerAdapter";

    private List<Person> mPersons;
    private String listName;
    private Context mContext;

    public RecyclerAdapter(Context mContext, RecyclerAdapter.OnRecyclerItemClicked onRecyclerItemClicked) {
        this.mContext = mContext;
        this.onRecyclerItemClicked = onRecyclerItemClicked;
    }

    public void setPersonModels(List<Person> mPersons){
        this.mPersons = mPersons;
    }

    public void setListName (String listName) {this.listName = listName;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        // Set the name of the Person
        holder.name.setText(mPersons.get(position).getName());

        //Setting onclicklistener for button
        holder.deletePersonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerItemClicked.deletePerson(mPersons.get(position).getName());
            }
        });

        // Set the text of gift quantity
        int giftsBought = mPersons.get(position).getGiftsBought();
        String word = "";
        if (giftsBought == 1){word = " prezent z ";}
        else if(giftsBought == 2 || giftsBought ==3 || giftsBought == 4 ) {word = " prezenty z ";}
        else {word = " prezentów z ";}
        holder.giftQuantity.setText("Zakupiono "+ giftsBought + word + mPersons.get(position).getGiftQuantity());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on something");
                String value = mPersons.get(position).getName();
                Log.d(TAG, "onClick: clicked on something " + mPersons.get(position).getName());
                Intent myIntent = new Intent(mContext, GiftsActivity.class);
                myIntent.putExtra("key", value);
                myIntent.putExtra("key2", listName);
                myIntent.putExtra("key3", mPersons.get(position).getBudget());
                Log.d(TAG, "onClick: KURWA" + mPersons.get(position).getBudget());//Optional parameters

                mContext.startActivity(myIntent);
            }
        });
    }

    public interface OnRecyclerItemClicked{
        void shutDownActivity();
        void deletePerson(String personName);
    }

    @Override
    public int getItemCount() {
        if(mPersons == null){
            return 0;
        } else {
            return mPersons.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

         private TextView name;
         private TextView giftQuantity;
         private ImageButton deletePersonBtn;
         RelativeLayout parentLayout;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);

             name = itemView.findViewById(R.id.person_name);
             giftQuantity = itemView.findViewById(R.id.gift_quantity);
             deletePersonBtn = itemView.findViewById(R.id.delete_person_button);

             parentLayout = itemView.findViewById(R.id.parent_layout);

         }
     }

}
