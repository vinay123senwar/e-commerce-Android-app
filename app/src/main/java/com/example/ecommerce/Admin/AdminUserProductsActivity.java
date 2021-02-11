package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.CartViewHolder;
import com.example.ecommerce.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView produxt_list;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
    private String Phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        Phone= getIntent().getStringExtra("Phone");
        produxt_list=findViewById(R.id.admin_product_list);
        produxt_list.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        produxt_list.setLayoutManager(layoutManager);
        cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(Phone).child("Products");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef,Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {

                holder.txtProductQuantity.setText("Quantity =" + model.getQuantity());
                holder.txtProductPrice.setText("Price =" + model.getPrice());
                holder.txtProductName.setText(model.getName());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        produxt_list.setAdapter(adapter);
        adapter.startListening();
    }
}
