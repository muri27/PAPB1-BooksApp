package com.example.booksapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private Context context;
    private ArrayList<ItemData> values;
    private LayoutInflater inflater;

    public ItemAdapter(Context context, ArrayList<ItemData> values){
        this.context=context;
        this.values=values;
        this.inflater=LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleText;
        TextView authorText;

        public ViewHolder(View view){
            super (view);
            titleText=view.findViewById(R.id.text1);
            authorText=view.findViewById(R.id.text2);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        ItemData data=values.get(position);
        holder.titleText.setText(data.itemTitle);;
        holder.authorText.setText(data.itemAuthor);;
        //agar setiap item bisa diklik dibuat onclicklistener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailActivity.class);
                //Proses Pengiriman data ke DetailActivity memakai putExtra
                //krn sudah di parcel
                intent.putExtra("DATA", data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
