package com.example.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
    private String Phone="";
    private String totalAmount=null;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");
        Button confirmOrderBttn = (Button) findViewById(R.id.confirm_final_order_bttn);
        nameEditText =(EditText)findViewById(R.id.shipment_name);
        phoneEditText =(EditText)findViewById(R.id.shipment_phone_number);
        cityEditText =(EditText)findViewById(R.id.shipment_city);
        addressEditText =(EditText)findViewById(R.id.shipment_address);
        Phone = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("Phone")).toString();
        confirmOrderBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
              Check();
            }
        });

    }

    private void Check()
    {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            toast("Please Write Your Name");
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
                toast("Please Write Your Phone");
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            toast("Please Write Your Address");
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            toast("Please Write Your city");
        }
        else
        {
            ConfirmOrder();
        }

    }

    private void ConfirmOrder()
    {
        String saveCurrentDate,saveCurrentTime;
        Calendar calendar =Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate =new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Phone);

        HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("Name",nameEditText.getText().toString());
        cartMap.put("Phone",phoneEditText.getText().toString());
        cartMap.put("Address",addressEditText.getText().toString());
        cartMap.put("City",cityEditText.getText().toString());
        cartMap.put("Date",saveCurrentDate);
        cartMap.put("Time",saveCurrentTime);
        cartMap.put("TotalAmount",totalAmount);
        cartMap.put("State","Order Not Shipped");

        ordersRef.updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
             if(task.isSuccessful())
             {
                 FirebaseDatabase.getInstance().getReference().child("Cart List")
                         .child("User View")
                         .child(Phone)
                         .removeValue()
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task)
                             {
                             if(task.isSuccessful())
                             {
                                 toast("Your Final Order is Placed Successfully");
                                 Intent Newintent =new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                 Newintent.putExtra("Phone",Phone);
                                 Newintent.putExtra("type","");
                                 Newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 startActivity(Newintent);
                             }
                             }
                         });
             }
            }
        });
    }

    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
