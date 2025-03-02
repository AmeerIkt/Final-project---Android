package com.example.finalproject0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter  extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context ;
    Activity activity ;
    private int position ;
    private ArrayList item_id ,item_name , item_price ;

    CustomAdapter (Activity activity ,Context context , ArrayList item_id ,ArrayList item_name ,ArrayList item_price){
        this.context = context ;
        this.item_id = item_id ;
        this.item_name = item_name ;
        this.item_price = item_price;
        this.activity = activity ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.item_display_row , parent , false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        this.position = position ;
        holder.item_name.setText(String.valueOf(item_name.get(position)));
        holder.item_price.setText(String.valueOf(item_price.get(position)));
        holder.item_id.setText(String.valueOf(item_id.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , updateitem.class);
                intent.putExtra("id", String.valueOf(item_id.get(position)));
                intent.putExtra("name", String.valueOf(item_name.get(position)));
                intent.putExtra("price", String.valueOf(item_price.get(position)));
                activity.startActivityForResult(intent ,1);

            }
        });


    }

    @Override
    public int getItemCount() {
        return item_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item_price , item_name ,item_id ;
        LinearLayout mainLayout ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_price = itemView.findViewById(R.id.item_price_txt);
            item_name = itemView.findViewById(R.id.item_name_txt);
            item_id = itemView.findViewById(R.id.item_id_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);


        }
    }

}
