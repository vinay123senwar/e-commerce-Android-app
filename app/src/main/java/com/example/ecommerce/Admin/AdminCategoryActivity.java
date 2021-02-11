package com.example.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ecommerce.buyers.HomeActivity;
import com.example.ecommerce.buyers.MainActivity;
import com.example.ecommerce.R;


public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tShirts,sportsTshirt,femaleDresses,sweaters;
    private ImageView glasses,hatCaps,walletsBags,shoes;
    private ImageView headPhones,laptops,watches,mobilePhones;
   private Button LogoutBttn,CheckOrderbttn,maintainProductsBttn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);



        tShirts =(ImageView)findViewById(R.id.t_shirts);
        sportsTshirt =(ImageView)findViewById(R.id.sports_t_shirts);
        femaleDresses =(ImageView)findViewById(R.id.female_dresses);
        sweaters =(ImageView)findViewById(R.id.sweaters);

        glasses =(ImageView)findViewById(R.id.glasses);
        hatCaps =(ImageView)findViewById(R.id.hats_caps);
        walletsBags =(ImageView)findViewById(R.id.purse_bags_wallets);
        shoes =(ImageView)findViewById(R.id.shoess);

        headPhones =(ImageView)findViewById(R.id.headphones_handfree);
        laptops =(ImageView)findViewById(R.id.laptops_pc);
        watches =(ImageView)findViewById(R.id.watches);
        mobilePhones =(ImageView)findViewById(R.id.mobile_phones);



        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","T Shirts");
                startActivity(intent);
            }
        });

        sportsTshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Sports TShirts");
                startActivity(intent);
            }
        });

        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Female Dresses");
                startActivity(intent);
            }
        });

        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Sweaters");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });

        hatCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Hats Caps");
                startActivity(intent);
            }
        });

        walletsBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Wallet Bags");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);
            }
        });

        headPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Head Pones");
                startActivity(intent);
            }
        });

      watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });

        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","Mobile Phones");
                startActivity(intent);
            }
        });
    }
}
