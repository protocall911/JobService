package com.example.jobservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.jobservice.Models.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;

public class MyChatsActivity extends AppCompatActivity {

    DatabaseReference reference;

    private RecyclerView recyclerView;
    private List<MessagesList> messagesLists = new ArrayList<>();
    private ImageView tv_close_chats;

    private String lastMessage = "";
    String email_from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chats);

        Paper.init(this);
        email_from = Paper.book().read(Prevalent.UserEmailKey);

        tv_close_chats = findViewById(R.id.tv_close_chats);
        tv_close_chats.setOnClickListener(v -> {
            Intent intent = new Intent(MyChatsActivity.this, NavMenuActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.chats_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://jobservice-ccf27-default-rtdb.firebaseio.com/");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.child("Users").getChildren()){
                    final String getEmailUser = dataSnapshot.getKey();

                    if (!getEmailUser.equals(email_from)){
                        final String getName = dataSnapshot.child("email").getValue(String.class);

                        MessagesList messagesList = new MessagesList(getName, lastMessage);
                        messagesLists.add(messagesList);
                    }
                }
                recyclerView.setAdapter(new ChatAdapter(messagesLists, getApplicationContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}