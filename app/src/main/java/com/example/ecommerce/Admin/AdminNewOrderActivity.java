package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference orderRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList=findViewById(R.id.new_order_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef, AdminOrders.class)
                .build();


        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model)
                    {
                        holder.userName.setText("Name: "+model.getName());
                        holder.userPhone.setText("Phone: "+model.getPhone());
                        holder.userTotalPrice.setText("Price: "+model.getTotalAmount());
                        holder.userDateTime.setText("Order at: "+model.getDate()+ " " + model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: "+model.getAddress()+ " "+ model.getCity());

                        holder.showOrderBttn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uid =getRef(position).getKey();

                                Intent pintent =new Intent(AdminNewOrderActivity.this, AdminUserProductsActivity.class);
                                pintent.putExtra("Phone",uid);
                                startActivity(pintent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                            CharSequence options[]=new CharSequence[]
                                    {
                                            "Yes",
                                            "No"

                                    };
                                AlertDialog.Builder builder =new AlertDialog.Builder(AdminNewOrderActivity.this);
                                builder.setTitle("Have You Shipped This Order");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i)
                                    {
                                        if(i==0)
                                        {
                                            String uid =getRef(position).getKey();
                                            RemoveOrder(uid);

                                        }
                                        if(i==1)
                                        {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return  new AdminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uid)
    {
        orderRef.child(uid).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName,userPhone,userTotalPrice,userDateTime,userShippingAddress;
        public Button showOrderBttn;
        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.orders_username);
            userPhone=itemView.findViewById(R.id.orders_phone_number);
            userTotalPrice=itemView.findViewById(R.id.order_product_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippingAddress=itemView.findViewById(R.id.order_address_city);
            showOrderBttn=itemView.findViewById(R.id.order_show_all_products);



        }
    }
}
