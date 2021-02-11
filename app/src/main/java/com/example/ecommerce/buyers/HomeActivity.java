package com.example.ecommerce.buyers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminMaintainProductActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.model.Products;
import com.example.ecommerce.model.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String Phone;
    private  TextView UserNameTextView;
    private DatabaseReference ProdctsRef;
    private RecyclerView recyclerView;
    private String type="";
    private ActionBarDrawerToggle actionBarDrawerToggle;
    RecyclerView.LayoutManager layoutManager;
    CircleImageView profileImageView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

      type= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("type")).toString();

      /*  Intent inte=getIntent();
        Bundle bundle=inte.getExtras();
        if(bundle!=null)
        {
            type= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("Admin")).toString();
        }*/


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if(!type.equals("Admin"))
                {
                    Intent cartintent = new Intent(HomeActivity.this, CartActivity.class);
                    cartintent.putExtra("Phone", Phone);
                    startActivity(cartintent);
                }
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
       NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cart, R.id.nav_categories, R.id.nav_search,R.id.nav_settings,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if(!type.equals("Admin"))
        {Phone = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("Phone")).toString();}

        actionBarDrawerToggle =new ActionBarDrawerToggle(HomeActivity.this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                UserMenuSelectorItem(item);
                return false;
            }
        });

        View headerView =navigationView.getHeaderView(0);
        UserNameTextView =headerView.findViewById(R.id.user_profile_name);
        ProdctsRef =FirebaseDatabase.getInstance().getReference().child("Produts");
        recyclerView =findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        profileImageView =headerView.findViewById(R.id.user_profile_image);

        if(!type.equals("Admin"))
        {
            SetUserName();

            SetProfileImage();
        }



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetProfileImage()
    {
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users userdata = dataSnapshot.child("Users").child(Phone).getValue(Users.class);
                assert userdata != null;
                String image = userdata.getImage();
                Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetUserName()
    {
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users userdata = dataSnapshot.child("Users").child(Phone).getValue(Users.class);
                assert userdata != null;
                String name = userdata.getName();
                UserNameTextView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UserMenuSelectorItem(MenuItem item) {

        switch (item.getItemId())
        {


            case R.id.nav_cart:

                if(!type.equals("Admin")) {
                    Intent cartintent = new Intent(HomeActivity.this, CartActivity.class);
                    cartintent.putExtra("Phone", Phone);
                    startActivity(cartintent);
                }
                Toast.makeText(this,"Cart", Toast.LENGTH_SHORT).show();
                break;


            case R.id.nav_categories:

                Toast.makeText(this,"Categories", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                if(!type.equals("Admin"))
                {

                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                    Intent Settingsintent = new Intent(HomeActivity.this, SettingsActivity.class);
                    Settingsintent.putExtra("Phone", "" + Phone);
                    startActivity(Settingsintent);
                }
                break;

            case R.id.nav_search:
                if(!type.equals("Admin")) {
                    Intent sintent = new Intent(HomeActivity.this, SearchProductActivity.class);
                    sintent.putExtra("Phone", Phone);
                    startActivity(sintent);
                }
                Toast.makeText(this,"Search", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                Toast.makeText(this,"Log Out", Toast.LENGTH_SHORT).show();
                if(!type.equals("Admin")) {
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;

            case R.id.nav_dev:
                    Intent intent = new Intent(HomeActivity.this, Developer.class);
                    startActivity(intent);
                 break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProdctsRef.orderByChild("Status").equalTo("Approved"),Products.class).build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
            {
                holder.txtProducName.setText(model.getName());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price =" + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (type.equals("Admin"))
                        {
                            Intent Pintent = new Intent(HomeActivity.this, AdminMaintainProductActivity.class);
                            Pintent.putExtra("pid", "" + model.getPid());
                            Pintent.putExtra("Phone", "" + Phone);
                            startActivity(Pintent);

                        }
                        else
                        {
                            Intent Pxintent = new Intent(HomeActivity.this, ProductActivity.class);
                            Pxintent.putExtra("pid", "" + model.getPid());
                            Pxintent.putExtra("Phone", "" + Phone);
                            startActivity(Pxintent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false);
                ProductViewHolder holder =new ProductViewHolder(view);
                        return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
