package com.example.jobservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jobservice.Models.Chats;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import io.paperdb.Paper;

public class MessagesActivity extends AppCompatActivity {

    TextView tv_username;
    ImageButton ib_close_chat, btn_send_text;
    EditText et_typing_text;

    MessageAdapter messageAdapter;
    List<Chats> listChats;

    RecyclerView recyclerView;

    DatabaseReference reference;
    String email_to;
    String email_from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Bundle arguments = getIntent().getExtras();
        email_to = arguments.get("email").toString();

        Paper.init(this);
        email_from = Paper.book().read(Prevalent.UserEmailKey);

        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(email_to);

        ReadMessages(email_from, email_to);

        Toolbar toolbar = findViewById(R.id.toolbar_message_top);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_messages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ib_close_chat = findViewById(R.id.ib_close_chat);
        ib_close_chat.setOnClickListener(v -> {
            Intent intent = new Intent(MessagesActivity.this, MyChatsActivity.class);
            startActivity(intent);
        });

        btn_send_text = findViewById(R.id.btn_send_text);
        et_typing_text = findViewById(R.id.et_typing_text);

        btn_send_text.setOnClickListener(v -> SendMessage());

    }

    private void SendMessage(){
        String message = et_typing_text.getText().toString();

        if (!TextUtils.isEmpty(message)){

            ValidateMessage(message, email_from, email_to);

            et_typing_text.setText("");
        }
    }

    private void ValidateMessage(String message, String email_from, String email_to) {
        reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> chatDataMap = new HashMap<>();

        chatDataMap.put("message", message);
        chatDataMap.put("emailFrom", decodeUserEmail(email_from));
        chatDataMap.put("emailTo", decodeUserEmail(email_to));

        reference.child("Chats").push().setValue(chatDataMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(MessagesActivity.this,"Success",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MessagesActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ReadMessages(final String my_id, String userid){
        listChats = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listChats.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chats chats = snapshot1.getValue(Chats.class);
                    if (chats.getEmailTo().equals(decodeUserEmail(my_id)) && chats.getEmailFrom().equals(userid) ||
                    chats.getEmailTo().equals(userid) && chats.getEmailFrom().equals(decodeUserEmail(my_id))){
                        listChats.add(chats);
                    }

                    messageAdapter = new MessageAdapter(MessagesActivity.this, listChats);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}