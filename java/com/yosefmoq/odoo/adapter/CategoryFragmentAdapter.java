package com.yosefmoq.odoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yosefmoq.odoo.databinding.ItemCategoryBinding;
import com.yosefmoq.odoo.listeners.OnCategoryClickListener;
import com.yosefmoq.odoo.model.generic.CategoryData;

import java.util.List;

public class CategoryFragmentAdapter extends RecyclerView.Adapter<CategoryFragmentAdapter.ViewHolder> {

    Context context;
    List<CategoryData> categoryData;
    OnCategoryClickListener onCategoryClickListener;

    public CategoryFragmentAdapter(Context context, List<CategoryData> categoryData, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.categoryData = categoryData;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemCategoryBinding.setData(categoryData.get(position));
        holder.itemCategoryBinding.getRoot().setOnClickListener(v -> {
            onCategoryClickListener.onCategoryClick(position,categoryData.get(position));

        });
        holder.itemCategoryBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding itemCategoryBinding;
        public ViewHolder(ItemCategoryBinding itemCategoryBinding) {
            super(itemCategoryBinding.getRoot());
            this.itemCategoryBinding = itemCategoryBinding;

        }
    }


}
