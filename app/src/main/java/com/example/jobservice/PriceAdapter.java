package com.example.jobservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jobservice.Models.PriceList;
import com.example.jobservice.Listeners.PriceSelectListener;
import java.util.List;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyView> {
    private Context context;
    private List<PriceList> list;
    private PriceSelectListener listener;


    // View Holder class which
    // extends RecyclerView.ViewHolder
    public static class MyView extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardview);
            textView = view.findViewById(R.id.textview);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public CustomAdapter(List<PriceList> list, Context context, PriceSelectListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate item.xml using LayoutInflator
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(MyView holder, int position) {
        final PriceList priceList = list.get(position);
        // Set the text of each item of
        // Recycler view with the list items
        holder.textView.setText(priceList.getPrice());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(list.get(holder.getAdapterPosition()));
            }
        });
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}