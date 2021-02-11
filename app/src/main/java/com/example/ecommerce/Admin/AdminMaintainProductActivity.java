package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class AdminMaintainProductActivity extends AppCompatActivity
{
    private Button applyChanges,deleteBttn;
    private EditText name,price,description;
    private ImageView imageView;
    private String productID,Phone;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);

        productID =getIntent().getStringExtra("pid");
        Phone =getIntent().getStringExtra("Phone");
        productsRef= FirebaseDatabase.getInstance().getReference().child("Produts").child(productID);

        deleteBttn=(Button)findViewById(R.id.maintain_delete_bttn);
        applyChanges =(Button)findViewById(R.id.maintain_product_bttn);
        name=(EditText)findViewById(R.id.maintain_product_name);
        price=(EditText)findViewById(R.id.maintain_product_price_view);
        description=(EditText)findViewById(R.id.maintain_product_description_view);
        imageView=(ImageView)findViewById(R.id.maintain_product_image_view);

        DisplaySpecificInfo();

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            applyChanges();
            }
        });

        deleteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             deleteThisProduct();
            }
        });


    }

    private void deleteThisProduct()
    {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
            toast("Product is Deleted Successfully");
                Intent cpintent =new Intent(AdminMaintainProductActivity.this, AdminAddNewProductActivity.class);
                cpintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(cpintent);
                finish();
            }
        });
    }

    private void applyChanges()
    {
        String aName=name.getText().toString();
        String aPrice=price.getText().toString();
        String aDescription=description.getText().toString();

        if(aName.equals(""))
        {
            toast("Please Write Product Name");
        }
        else if(aPrice.equals(""))
        {
            toast("Please Provide Product Price");
        }
        else if(aDescription.equals(""))
        {
            toast("Please Provide Product Description");
        }
        else
        {
            HashMap<String, Object> productMap =new HashMap<>();
            productMap.put("pid",productID);
            productMap.put("Description",aDescription);
            productMap.put("Price",aPrice);
            productMap.put("Name",aName);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                if(task.isSuccessful())
                {
                    toast("Changes Applied Successfully");
                    Intent cintent =new Intent(AdminMaintainProductActivity.this,AdminCategoryActivity.class);
                    cintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(cintent);
                }
                }
            });
        }
    }

    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void DisplaySpecificInfo()
    {
        productsRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String pName= Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
                    String pPrice= Objects.requireNonNull(dataSnapshot.child("Price").getValue()).toString();
                    String pDescription= Objects.requireNonNull(dataSnapshot.child("Description").getValue()).toString();
                    String pImage= Objects.requireNonNull(dataSnapshot.child("Image").getValue()).toString();
                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
