package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemsClickListner;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    public ItemsClickListner itemsClickListner;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txtProductName=itemView.findViewById(R.id.cart_item_product_name);
        txtProductPrice=itemView.findViewById(R.id.cart_item_product_price);
        txtProductQuantity=itemView.findViewById(R.id.cart_item_product_quantity);

    }

    @Override
    public void onClick(View v)
    {
        itemsClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemsClickListner(ItemsClickListner itemsClickListner) {
        this.itemsClickListner = itemsClickListner;
    }
}
