package com.testyourself.teknomerkez.testyourself.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.testyourself.teknomerkez.testyourself.Interface.ItemClickListener;
import com.testyourself.teknomerkez.testyourself.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView category_name;
    public ImageView category_image;
    private ItemClickListener itemclickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        category_image = itemView.findViewById(R.id.category_image);
        category_name = itemView.findViewById(R.id.category_title);
        itemView.setOnClickListener(this);
    }

    public void setItemclickListener(ItemClickListener itemclickListener) {
        this.itemclickListener = itemclickListener;
    }

    @Override
    public void onClick(View v) {
        itemclickListener.onClick(v,getAdapterPosition(),false);
    }
}
