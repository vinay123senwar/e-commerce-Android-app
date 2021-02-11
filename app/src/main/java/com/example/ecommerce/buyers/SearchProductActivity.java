package com.example.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SearchProductActivity extends AppCompatActivity
{
    private Button SearchBttn;
    private EditText inputText;
    private RecyclerView SearchList;
    private String Phone;
    private String searchInput;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        inputText=(EditText)findViewById(R.id.search_product_name);
        SearchBttn=findViewById(R.id.search_bttn);
        SearchList=findViewById(R.id.search_list);
        Phone = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("Phone")).toString();
        SearchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));

        SearchBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             searchInput = inputText.getText().toString();
             onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Produts");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>().setQuery(ref.orderByChild("Name").startAt(searchInput),Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
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
                            public void onClick(View v)
                            {
                                Intent Pintent =new Intent(SearchProductActivity.this, ProductActivity.class);
                                Pintent.putExtra("pid",""+model.getPid());
                                Pintent.putExtra("Phone",""+Phone);
                                startActivity(Pintent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent,false);
                        ProductViewHolder holder =new ProductViewHolder(view);
                        return holder;
                    }
                } ;
        SearchList.setAdapter(adapter);
        adapter.startListening();
    }
}
