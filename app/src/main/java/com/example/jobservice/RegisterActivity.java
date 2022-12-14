package com.example.jobservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegNext;
    private EditText etPasswordReg,etSurname,etName,etEmailReg;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Paper.init(this);

        btnRegNext = findViewById(R.id.btnRegNext);
        etPasswordReg = findViewById(R.id.etPasswordReg);
        etSurname = findViewById(R.id.etSurname);
        etName = findViewById(R.id.etName);
        etEmailReg = findViewById(R.id.etEmailReg);

        btnRegNext.setOnClickListener(v -> {
            CreateAccount();
        });
    }

    private void CreateAccount() {
        String email = etEmailReg.getText().toString();
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String password = etPasswordReg.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
        } else if (!name.matches("[a-zA-Z]+")){
            Toast.makeText(RegisterActivity.this, "The Name was entered incorrectly", Toast.LENGTH_SHORT).show();
        } else if (!surname.matches("[a-zA-Z]+")){
            Toast.makeText(RegisterActivity.this, "The Last name was entered incorrectly", Toast.LENGTH_SHORT).show();
        } else if (!email.matches("[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+")){
            Toast.makeText(RegisterActivity.this, "Email is entered incorrectly", Toast.LENGTH_SHORT).show();
        } else if (password.length()<=7){
            Toast.makeText(RegisterActivity.this, "The Password must contain at least 7 characters", Toast.LENGTH_SHORT).show();
        } else {
            ValidateEmailUnique(email,name,surname,password);
        }
    }
    
    private void ValidateEmailUnique(String email, String name, String surname, String password) {
        RootRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> userDataMap = new HashMap<>();

        String encodeEmail = encodeUserEmail(email);

        userDataMap.put("email", encodeEmail);
        userDataMap.put("name", name);
        userDataMap.put("surname", surname);
        userDataMap.put("password", password);

        RootRef.child("Users").child(encodeEmail).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Paper.book().write(Prevalent.UserEmailKey, encodeEmail);
                    Toast.makeText(RegisterActivity.this,"Registration was successful!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, NavMenuActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Registration error!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}