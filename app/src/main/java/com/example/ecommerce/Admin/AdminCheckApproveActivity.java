package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.interfaces.ItemsClickListner;
import com.example.ecommerce.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckApproveActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unVerifiedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_approve);

        recyclerView =findViewById(R.id.admin_products_approve_list);
        layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        unVerifiedRef = FirebaseDatabase.getInstance().getReference().child("Produts");

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unVerifiedRef.orderByChild("Status")
                                .equalTo("Not Approved"),Products.class).build();


        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
            {
                holder.txtProducName.setText(model.getName());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price =" + model.getPrice());
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

                       AlertDialog.Builder builder= new AlertDialog.Builder(AdminCheckApproveActivity.this);
                       builder.setTitle("Do You Want To Approve this Products");
                       builder.setItems(opt, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int k)
                           {
                               if(k==0)
                               {
                                   ChangeProductState(productId);
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
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false);
                ProductViewHolder holder =new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void ChangeProductState(String productId)
    {
        unVerifiedRef.child(productId).child("Status").setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        toast("Order Approved Successfully");
                    }
                });
    }

    private void toast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
