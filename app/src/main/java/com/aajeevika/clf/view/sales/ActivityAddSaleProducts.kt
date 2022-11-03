package com.aajeevika.clf.view.sales

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityAddSaleProductsBinding
import com.aajeevika.clf.model.data_model.SalesSellerProducts
import com.aajeevika.clf.model.data_model.SalesSellerProductsDataModel
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.view.sales.adapter.SaleAddProductRecyclerViewAdapter
import com.aajeevika.clf.view.sales.viewmodel.AddSaleProductsViewModel

class ActivityAddSaleProducts : BaseActivityVM<ActivityAddSaleProductsBinding, AddSaleProductsViewModel>(
    R.layout.activity_add_sale_products,
    AddSaleProductsViewModel::class
) {
    private val addSaleProductRecyclerViewAdapter = SaleAddProductRecyclerViewAdapter()
    private val productData: ArrayList<SalesSellerProducts>? by lazy {
        intent.getSerializableExtra(Constant.PRODUCT_DATA) as? ArrayList<SalesSellerProducts>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.recyclerView.run {
            adapter = addSaleProductRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
        }

        viewModel.getInterestById(preferencesHelper.uid)
    }

    override fun observeData() {
        super.observeData()

        viewModel.productsLiveData.observe(this, { list ->
            list.seller_data.forEach { product ->
                productData?.firstOrNull { it.id == product.id }?.quantity?.let { product.quantity = it }
            }

            stopSwipeToRefreshRefresh()
            addSaleProductRecyclerViewAdapter.addData(list.seller_data)
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getInterestById(preferencesHelper.uid)
            }

            doneBtn.setOnClickListener {
                if(addSaleProductRecyclerViewAdapter.getDataList().isEmpty()) {
                    root.showMessage(getString(R.string.please_enter_at_least_one_product))
                } else {
                    val intent = Intent()
                    intent.putExtra(Constant.PRODUCT_DATA, addSaleProductRecyclerViewAdapter.getDataList())
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}