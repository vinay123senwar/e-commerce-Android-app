package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemsClickListner;

public class ItemViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProducName, txtProductDescription,txtProductPrice,txtProductStatus;
    public ImageView imageView;
    public ItemsClickListner listner;
    public ItemViewHolder (@NonNull View itemView)
    {
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.seller_product_image_view);
        txtProducName=(TextView)itemView.findViewById(R.id.seller_product_name_view);
        txtProductDescription=(TextView)itemView.findViewById(R.id.seller_product_description_view);
        txtProductPrice=(TextView)itemView.findViewById(R.id.seller_product_price_view);
        txtProductStatus=(TextView)itemView.findViewById(R.id.seller_product_status_view);
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

