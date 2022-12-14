package com.example.jobservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostViewActivity extends AppCompatActivity {
    TextView pid_post, post_name, post_price, post_description,
            tv_customer_name, tv_btn_respond,tv_address_post;
    ImageButton btn_close_post;
    String pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        Bundle arguments = getIntent().getExtras();
        pid = arguments.get("pid").toString();

        pid_post = findViewById(R.id.pid_post);
        post_name = findViewById(R.id.post_name);
        post_price = findViewById(R.id.post_price);
        post_description = findViewById(R.id.post_description);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_btn_respond = findViewById(R.id.tv_btn_respond);
        btn_close_post = findViewById(R.id.btn_close_post);
        tv_address_post = findViewById(R.id.tv_address_post);

        PostInfo();

        btn_close_post.setOnClickListener(v -> {
            Intent intent = new Intent(PostViewActivity.this, NavMenuActivity.class);
            startActivity(intent);
        });

        tv_btn_respond.setOnClickListener(v -> {
            Intent intent = new Intent(PostViewActivity.this, MessagesActivity.class);
            intent.putExtra("email", tv_customer_name.getText().toString());
            startActivity(intent);
        });
    }

    private void PostInfo() {
        DatabaseReference PostRef = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(pid);
        PostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    pid_post.setText("Задание № " + pid);
                    post_name.setText(snapshot.child("post_name").getValue().toString());
                    post_price.setText(snapshot.child("price").getValue().toString());
                    tv_address_post.setText(snapshot.child("post_address").getValue().toString());
                    post_description.setText(snapshot.child("description").getValue().toString());
                    tv_customer_name.setText(decodeUserEmail(snapshot.child("email").getValue().toString()));
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