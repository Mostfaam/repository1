package com.yosefmoq.odoo.handler.product

import android.content.Context
import android.content.Intent
import com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_ID
import com.yosefmoq.odoo.constant.BundleConstant.BUNDLE_KEY_PRODUCT_NAME
import com.yosefmoq.odoo.helper.OdooApplication
import com.yosefmoq.odoo.model.generic.ProductData

class AlternativeProductHandler(var context: Context) {

    fun clickOnProduct(productData: ProductData) {

        val intent = Intent(context, (context.getApplicationContext() as OdooApplication).productActivity)
        intent.putExtra(BUNDLE_KEY_PRODUCT_ID, productData.getTemplateId())
        intent.putExtra(BUNDLE_KEY_PRODUCT_NAME, productData.getName())
        context.startActivity(intent)

    }
}