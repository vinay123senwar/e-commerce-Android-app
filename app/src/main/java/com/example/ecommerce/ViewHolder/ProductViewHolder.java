package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemsClickListner;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProducName, txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemsClickListner listner;
    public ProductViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.product_image_view);
        txtProducName=(TextView)itemView.findViewById(R.id.product_name_view);
        txtProductDescription=(TextView)itemView.findViewById(R.id.product_description_view);
        txtProductPrice=(TextView)itemView.findViewById(R.id.product_price_view);
    }
    public void setItemClickListener(ItemsClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view,getAdapterPosition(),false);

    }
}
