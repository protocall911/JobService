package com.example.jobservice;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView settings_account_image;
    private EditText et_settings_surname, et_settings_name, et_settings_email;
    private TextView tv_save_settings;
    private ImageButton btn_close_settings;

    String encodeEmail;

    DatabaseReference reference;

    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Paper.init(this);

        settings_account_image = findViewById(R.id.settings_account_image);
        et_settings_surname = findViewById(R.id.et_settings_surname);
        et_settings_name = findViewById(R.id.et_settings_name);
        et_settings_email = findViewById(R.id.et_settings_email);
        tv_save_settings = findViewById(R.id.tv_save_settings);
        btn_close_settings = findViewById(R.id.btn_close_settings);

        encodeEmail = Paper.book().read(Prevalent.UserEmailKey);
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(encodeEmail);


        userInfo(et_settings_surname, et_settings_name, et_settings_email);

        btn_close_settings.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, NavMenuActivity.class);
            startActivity(intent);
        });

        tv_save_settings.setOnClickListener(v -> {
            userInfoSaved();
//            if (checker.equals("clicked")) {
//                userInfoSaved();
//
//            }
        });
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(et_settings_name.getText().toString()) ||
                TextUtils.isEmpty(et_settings_surname.getText().toString())
                || TextUtils.isEmpty(et_settings_email.getText().toString())) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        } else {
            et_settings_name.setText(et_settings_name.getText().toString());
            HashMap<String, Object> userDataMap = new HashMap<>();

            userDataMap.put("name", et_settings_name.getText().toString());
            userDataMap.put("surname", et_settings_surname.getText().toString());

            reference.updateChildren(userDataMap);
        }
    }

    private void userInfo(EditText et_settings_surname, EditText et_settings_name, EditText et_settings_email) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String surname = dataSnapshot.child("surname").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    et_settings_name.setText(name);
                    et_settings_surname.setText(surname);
                    et_settings_email.setText(decodeUserEmail(email));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

}