package com.yosefmoq.odoo.listeners;

import com.yosefmoq.odoo.model.generic.CategoryData;

public interface OnCategoryClickListener {
    void onCategoryClick(int position, CategoryData categoryData);
}
