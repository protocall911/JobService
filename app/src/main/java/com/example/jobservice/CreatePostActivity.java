package com.example.jobservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jobservice.Models.PriceList;
import com.example.jobservice.Listeners.PriceSelectListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CreatePostActivity extends AppCompatActivity implements PriceSelectListener {
    private EditText et_datetime;
    private Calendar calendar;
    private DatabaseReference RootRef;

    List<PriceList> priceLists;
    LinearLayoutManager linearLayoutManager;
    TextView tv_price;

    Button btn_publish;
    EditText et_post_name,et_post_address,et_description;

    @Override
    public void onItemClicked(PriceList priceList) {
        tv_price.setVisibility(View.VISIBLE);
        tv_price.setText(priceList.getPrice());
    }

    public static class MyView extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);
            cardView = view.findViewById(R.id.cardview);
            textView = view.findViewById(R.id.textview);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Paper.init(this);

        calendar = Calendar.getInstance();

        et_datetime = findViewById(R.id.et_datetime);
        et_post_address = findViewById(R.id.et_post_address);
        et_post_name = findViewById(R.id.et_post_name);
        tv_price = findViewById(R.id.tv_price);
        et_description = findViewById(R.id.et_description);

        TextView tv_cancel_creating = findViewById(R.id.tv_cancel_creating);
        btn_publish = findViewById(R.id.btn_publish);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        linearLayoutManager = new LinearLayoutManager(CreatePostActivity.this,
                LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        AddItemsToArrayList();

        CustomAdapter adapter = new CustomAdapter(priceLists, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);

        DatePickerDialog.OnDateSetListener datetime = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                
                updateLabel();
            }
        };

        et_datetime.setOnClickListener(v -> {
            new DatePickerDialog(CreatePostActivity.this, datetime, calendar
            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        tv_cancel_creating.setOnClickListener(v -> {
            Intent intent = new Intent(this, NavMenuActivity.class);
            startActivity(intent);
        });

        btn_publish.setOnClickListener(v -> {
            PublishNewPost();
        });
    }

    private void PublishNewPost() {
        String post_name = et_post_name.getText().toString();
        String post_address = et_post_address.getText().toString();
        String datetime = et_datetime.getText().toString();
        String price = tv_price.getText().toString();
        String description = et_description.getText().toString();

        if (TextUtils.isEmpty(post_name) || TextUtils.isEmpty(post_address) || TextUtils.isEmpty(datetime)
                || TextUtils.isEmpty(price) || TextUtils.isEmpty(description)){
            Toast.makeText(CreatePostActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        } else {
            ValidateNewPost(post_name,post_address,datetime,price,description);
        }
    }

    private void ValidateNewPost(String post_name, String post_address, String datetime, String price, String description) {
        String email = Paper.book().read(Prevalent.UserEmailKey);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        String productRandomKey = saveCurrentDate + saveCurrentTime;

        RootRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> postDataMap = new HashMap<>();

        postDataMap.put("post_name", post_name);
        postDataMap.put("post_address", post_address);
        postDataMap.put("datetime", datetime);
        postDataMap.put("price", price);
        postDataMap.put("description",description);
        postDataMap.put("email",email);
        postDataMap.put("pid",productRandomKey);

        RootRef.child("Posts").child(productRandomKey).updateChildren(postDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CreatePostActivity.this,"Post published!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreatePostActivity.this, NavMenuActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CreatePostActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void AddItemsToArrayList() {
        priceLists = new ArrayList<>();
        priceLists.add(new PriceList("< 2000"));
        priceLists.add(new PriceList("2000-4000"));
        priceLists.add(new PriceList("4000-8000"));
        priceLists.add(new PriceList("8000-16000"));
        priceLists.add(new PriceList("> 16000"));
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_datetime.setText(sdf.format(calendar.getTime()));
    }
}