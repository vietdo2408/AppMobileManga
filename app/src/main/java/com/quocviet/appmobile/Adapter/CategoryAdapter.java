package com.quocviet.appmobile.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quocviet.appmobile.Model.ModelCategory;
import com.quocviet.appmobile.databinding.RowCategoryBinding;
import com.quocviet.appmobile.filters.FilterCategory;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.HolderCategory> implements Filterable {

    private Context context;
    public ArrayList<ModelCategory> categoryArrayList, filterList;
    private RowCategoryBinding binding;

    private FilterCategory filter;

    //create constructor
    public CategoryAdapter(Context context, ArrayList<ModelCategory> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
        this.filterList = categoryArrayList;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.HolderCategory holder, int position) {
        ModelCategory model = categoryArrayList.get(position);
        String id = model.getCategoryId();
        String category = model.getCategory();
        String uid = model.getUid();
        long timestamp = model.getTimestamp();

        holder.categoryTv.setText(category);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this category?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteCategory(model, holder);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return categoryArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterCategory(filterList, this);
        }
        return filter;
    }

    private void deleteCategory(ModelCategory model, HolderCategory holder) {
        //get id of category to delete
        String id = model.getCategoryId();
        //Firebase DB > Categories > categoryId
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Success delete....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class HolderCategory extends RecyclerView.ViewHolder {

        TextView categoryTv;
        ImageButton deleteBtn;

        public HolderCategory(@NonNull View itemView) {
            super(itemView);
            //init ui views
            categoryTv = binding.categoriesTv;
            deleteBtn = binding.deleteBtn;
        }
    }
}
