package com.example.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.R;
import com.example.ecommerce.model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {

    private Button addToCartBttn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productName,productDescription,productPrice;
    private String productID,Phone,state="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        addToCartBttn=(Button)findViewById(R.id.pd_add_to_cart_bttn);
        productImage=(ImageView)findViewById(R.id.product_image_details);
        numberButton=(ElegantNumberButton)findViewById(R.id.number_btn);
        productName=(TextView)findViewById(R.id.product_name_details);
        productDescription=(TextView)findViewById(R.id.product_description_details);
        productPrice=(TextView)findViewById(R.id.product_price_details);

        productID =getIntent().getStringExtra("pid");
        Phone =getIntent().getStringExtra("Phone");
        getProductDetails(productID);

        addToCartBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

             if(state.equals("Order Placed") || state.equals("Order Shipped"))
             {
                 toast("You can purchase More Order Once It is Delivered");
             }
             else
             {
                 addingToCartList();
             }
            }
        });

    }
    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        CheckOverState();
        super.onStart();
    }

    private void addingToCartList()
    {
        String saveCurrentDate,saveCurrentTime;
        Calendar calendarForDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate =new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendarForDate.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calendarForDate.getTime());

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap =new HashMap<>();

        cartMap.put("pid",productID);
        cartMap.put("Name",productName.getText().toString());
        cartMap.put("Price",productPrice.getText().toString());
        cartMap.put("Date",saveCurrentDate);
        cartMap.put("Time",saveCurrentTime);
        cartMap.put("Quantity",numberButton.getNumber());
        cartMap.put("Discount","");

        cartListRef.child("User View").child(Phone).
                child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                     if(task.isSuccessful())
                     {
                         cartListRef.child("Admin View").child(Phone).
                                 child("Products").child(productID)
                                 .updateChildren(cartMap)
                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task)
                                     {
                                       if(task.isSuccessful())
                                       {
                                           toast("Addded Successfully");
                                           Intent intent =new Intent(ProductActivity.this, HomeActivity.class);
                                           intent.putExtra("Phone",Phone);
                                           intent.putExtra("type","");
                                           startActivity(intent);

                                       }
                                     }
                                 });
                     }
                    }
                });


    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Produts");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);
                    assert products != null;
                    productName.setText(products.getName());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());

                    Picasso.get().load(products.getImage()).into(productImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckOverState()
    {
        DatabaseReference ordersRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Phone);
        ordersRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingState =dataSnapshot.child("State").getValue().toString();
                    String userName =dataSnapshot.child("Name").getValue().toString();

                    if(shippingState.equals("Shipped"))
                    {
                      state="Order Shipped";
                    }
                    else if(shippingState.equals("Order Not Shipped"))
                    {
                        state="Order Placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
