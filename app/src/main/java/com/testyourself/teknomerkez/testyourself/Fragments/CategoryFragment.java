package com.testyourself.teknomerkez.testyourself.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.testyourself.teknomerkez.testyourself.Common.Common;
import com.testyourself.teknomerkez.testyourself.Interface.ItemClickListener;
import com.testyourself.teknomerkez.testyourself.Model.Category;
import com.testyourself.teknomerkez.testyourself.R;
import com.testyourself.teknomerkez.testyourself.Start;
import com.testyourself.teknomerkez.testyourself.ViewHolder.CategoryViewHolder;

public class CategoryFragment extends Fragment {

    View myFragment;
    RecyclerView recycler_list_category;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    Context context;


    public static CategoryFragment newInstance() {
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(Common.Category);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_category, container, false);
        recycler_list_category = myFragment.findViewById(R.id.listCategory);
        recycler_list_category.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recycler_list_category.setLayoutManager(layoutManager);

        loadCategories();
        return myFragment;
    }

    private void loadCategories() {
        Query query = FirebaseDatabase.getInstance().getReference().child(Common.Category);
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.row_category_layout, parent, false);
                return new CategoryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final Category model) {
                holder.category_name.setText(model.getName());
                Picasso picasso = Picasso.get();
                picasso.load(model.getImage()).into(holder.category_image);

                final Category clickItem = model;
                holder.setItemclickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLong) {
                        Toast.makeText(getActivity(), clickItem.getName()
                                , Toast.LENGTH_SHORT).show();
                        Intent startGame = new Intent(getActivity(),Start.class);
                        Common.categoryID = adapter.getRef(position).getKey();
                        Common.categoryName = model.getName();
                        startActivity(startGame);
                        getActivity().finish();
                    }
                });
            }


        };
        adapter.notifyDataSetChanged();
        recycler_list_category.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }
}
