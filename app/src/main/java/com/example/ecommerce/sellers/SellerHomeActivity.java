package com.example.ecommerce.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminAddNewProductActivity;
import com.example.ecommerce.Admin.AdminCategoryActivity;
import com.example.ecommerce.Admin.AdminCheckApproveActivity;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ItemViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.buyers.MainActivity;
import com.example.ecommerce.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SellerHomeActivity extends AppCompatActivity {

    TextView homeBttn,addBttn,logoutBttn;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unVerifiedRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        recyclerView =findViewById(R.id.seller_products_unapprove_list);
        layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        unVerifiedRef = FirebaseDatabase.getInstance().getReference().child("Produts");



        homeBttn=(TextView)findViewById(R.id.seller_home_home_bttn);
        addBttn=(TextView)findViewById(R.id.seller_home_add_bttn);
        logoutBttn=(TextView)findViewById(R.id.seller_home_logout_bttn);

        logoutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final FirebaseAuth mAuth;
                mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent loIntent = new Intent(SellerHomeActivity.this, MainActivity.class);
                loIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loIntent);
                finish();
            }
        });

        addBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent addIntent = new Intent(SellerHomeActivity.this, AdminCategoryActivity.class);
                startActivity(addIntent);
            }

        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unVerifiedRef.orderByChild("Seller sid")
                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class).build();


        final FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter =new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull final Products model)
            {
                holder.txtProducName.setText(model.getName());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price =" + model.getPrice());
                holder.txtProductStatus.setText(model.getStatus());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        final String productId=model.getPid();
                        CharSequence opt[] = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };

                        AlertDialog.Builder builder= new AlertDialog.Builder(SellerHomeActivity.this);
                        builder.setTitle("Do You Want To Delete this Products");
                        builder.setItems(opt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int k)
                            {
                                if(k==0)
                                {
                                   deleteProduct(productId);
                                }
                                if(k==1)
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
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent,false);
                ItemViewHolder holder =new ItemViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteProduct(String productId)
    {
        unVerifiedRef.child(productId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        toast("Item Deleted Successfully");
                    }
                });
    }

    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
