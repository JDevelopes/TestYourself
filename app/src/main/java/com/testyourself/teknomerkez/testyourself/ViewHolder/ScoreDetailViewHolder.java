package com.testyourself.teknomerkez.testyourself.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.testyourself.teknomerkez.testyourself.Interface.ItemClickListener;
import com.testyourself.teknomerkez.testyourself.R;

public class ScoreDetailViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_category_name, txt_category_score;

    public ScoreDetailViewHolder(View itemView) {
        super(itemView);
        txt_category_name = itemView.findViewById(R.id.txt_category_name);
        txt_category_score = itemView.findViewById(R.id.txt_category_score);

    }


}
