package com.example.jobservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jobservice.Models.Posts;
import com.example.jobservice.databinding.ActivityNavMenuBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.paperdb.Paper;

public class NavMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavMenuBinding binding;
    private Intent intentSave;
    private RecyclerView recyclerView;

    String email;

    RecyclerView.LayoutManager layoutManager;
    DatabaseReference PostsRef, ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        View headerView = navigationView.getHeaderView(0);
        TextView tvUserName = headerView.findViewById(R.id.tvUserName);

        Paper.init(this);
        email = Paper.book().read(Prevalent.UserEmailKey);
        tvUserName.setText(decodeUserEmail(email));
        
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(PostsRef, Posts.class).build();

        FirebaseRecyclerAdapter<Posts, PostViewHolder> adapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Posts model) {
                holder.tv_post_pid.setText(model.getPid());
                ref = FirebaseDatabase.getInstance().getReference().child("Posts").child(holder.tv_post_pid.getText().toString());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            holder.tv_post_date.setText(dataSnapshot.child("datetime").getValue().toString());
                            holder.tv_post_price.setText(dataSnapshot.child("price").getValue().toString() + " RUB");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                holder.tv_post_name.setText(model.getPost_name());
                holder.tv_post_address.setText(model.getPost_address());

                holder.itemView.setOnClickListener(v -> {
                    Intent postIntent = new Intent(NavMenuActivity.this, PostViewActivity.class);
                    postIntent.putExtra("pid", holder.tv_post_pid.getText().toString());
                    startActivity(postIntent);
                });
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_items_layout, parent, false);
                PostViewHolder holder = new PostViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.app_bar_nav_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_create_post){
            Intent createIntent = new Intent(NavMenuActivity.this, CreatePostActivity.class);
            startActivity(createIntent);


        } else if(id == R.id.nav_messages){
            Intent intent = new Intent(NavMenuActivity.this, MyChatsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings){
            Intent settingsIntent = new Intent(NavMenuActivity.this, SettingsActivity.class);
            intentSave = getIntent();
            settingsIntent.putExtra("email", intentSave.getStringExtra("email"));
            startActivity(settingsIntent);

        } else if(id == R.id.nav_log_out){
            Intent loginIntent = new Intent(NavMenuActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}