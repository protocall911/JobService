package com.example.jobservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jobservice.Models.Chats;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

import io.paperdb.Paper;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private final Context context;
    private final List<Chats> listChats;

    DatabaseReference reference;

    public MessageAdapter(Context context, List<Chats> listChats){
        this.context = context;
        this.listChats = listChats;

        Paper.init(context);
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,
                    parent, false);
            return new ViewHolder(view);
        } else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,
                    parent, false);
            return new ViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position){
        Chats chats = listChats.get(position);
        holder.show_message.setText(chats.getMessage());
    }

    public int getItemCount(){
        return listChats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;

        public ViewHolder(View itemView){
            super(itemView);

            show_message = itemView.findViewById(R.id.tv_show_text);
        }
    }

    @Override
    public int getItemViewType(int position){
        String email_from = Paper.book().read(Prevalent.UserEmailKey);
        reference = FirebaseDatabase.getInstance().getReference();
        if (listChats.get(position).getEmailFrom().equals(decodeUserEmail(email_from))){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}
