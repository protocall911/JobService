package com.example.jobservice;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jobservice.Listeners.PostSelectListener;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView tv_post_name;
    TextView tv_post_date;
    TextView tv_post_address;
    TextView tv_post_price;
    public TextView tv_post_pid;
    PostSelectListener listener;

    public PostViewHolder(View itemView){
        super(itemView);

        tv_post_name = itemView.findViewById(R.id.tv_post_name);
        tv_post_date = itemView.findViewById(R.id.tv_post_date);
        tv_post_address = itemView.findViewById(R.id.tv_post_address);
        tv_post_price = itemView.findViewById(R.id.tv_post_price);

        tv_post_pid = itemView.findViewById(R.id.tv_post_pid);
    }

    public void setPostSelectListener(PostSelectListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
