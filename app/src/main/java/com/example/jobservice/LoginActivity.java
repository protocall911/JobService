package com.example.jobservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.jobservice.Models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);

        Button btnLogIn = findViewById(R.id.btnLogIn);
        Button btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogIn.setOnClickListener(v -> {
            LoginAccount();
        });
    }

    private void LoginAccount() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
        } else {
            ValidateEmail(email, password);
        }
    }

    private void ValidateEmail(String email, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        String encodeEmail = encodeUserEmail(email);

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(encodeEmail).exists()) {
                    String _email = snapshot.child("Users").child(encodeEmail).child("email").getValue(String.class);
                    String _password = snapshot.child("Users").child(encodeEmail).child("password").getValue(String.class);

                    Users user = new Users();
                    user.setEmail(_email);
                    user.setPassword(_password);

                    if (user.getEmail().equals(encodeEmail)) {
                        if (user.getPassword().equals(password)) {
                            Paper.book().write(Prevalent.UserEmailKey, encodeEmail);
                            Toast.makeText(LoginActivity.this, "Successful login!", Toast.LENGTH_SHORT).show();

                            Intent homeIntent = new Intent(LoginActivity.this, NavMenuActivity.class);
                            startActivity(homeIntent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account" + email + " not registered", Toast.LENGTH_SHORT).show();
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}