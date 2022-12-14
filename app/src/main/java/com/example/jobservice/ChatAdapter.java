package com.example.jobservice;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jobservice.Models.MessagesList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{

    private final List<MessagesList> messagesLists;
    private final Context context;

    public ChatAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_user_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        MessagesList chat = messagesLists.get(position);

        holder.tv_username.setText(decodeUserEmail(chat.getUsername()));
        holder.tv_last_message.setText(chat.getLastMessage());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessagesActivity.class);
            intent.putExtra("email", holder.tv_username.getText());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile_image;
        private TextView tv_username, tv_last_message;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_last_message = itemView.findViewById(R.id.tv_last_message);
        }
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}
