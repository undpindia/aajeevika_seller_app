package com.aajeevika.clf.view.sales

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityAddNewSaleBinding
import com.aajeevika.clf.model.data_model.Interest
import com.aajeevika.clf.model.data_model.SalesSellerProducts
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.RecyclerViewDecoration
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.UtilityActions.showMessage
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.interest.ActivityMyInterests
import com.aajeevika.clf.view.order.ActivityOrderDetailsCompleted
import com.aajeevika.clf.view.sales.adapter.SaleProductRecyclerViewAdapter
import com.aajeevika.clf.view.sales.viewmodel.AddNewSaleViewModel
import java.util.*
import kotlin.collections.ArrayList

class ActivityAddNewSale : BaseActivityVM<ActivityAddNewSaleBinding, AddNewSaleViewModel>(
    R.layout.activity_add_new_sale,
    AddNewSaleViewModel::class
) {
    private val saleProductRecyclerViewAdapter = SaleProductRecyclerViewAdapter()
    private val productsList = ArrayList<SalesSellerProducts>()
    private lateinit var interest: Interest

    private val interestIdResultCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.getIntExtra(Constant.INTEREST_ID, -1)?.let { id -> viewModel.getInterestById(id) }
        }
    }

    private val addProductResultCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            (it.data?.getSerializableExtra(Constant.PRODUCT_DATA) as? ArrayList<SalesSellerProducts>)?.let { list ->
                productsList.clear()
                productsList.addAll(list)
                saleProductRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.dateOfSaleValue.text = UtilityActions.formatDate(Calendar.getInstance().time)

        viewDataBinding.recyclerView.run {
            adapter = saleProductRecyclerViewAdapter
            addItemDecoration(RecyclerViewDecoration(8F, 8F, 8F, 8F))
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.interestsDetailLiveData.observe(this, { data ->
            interest = data

            viewDataBinding.run {
                interestId = data.interest_Id
                buyerName = data.buyer.name
                message = data.message

                productsList.clear()
                data.items.forEach {
                    productsList.add(
                        SalesSellerProducts(
                            it.product.id,
                            it.quantity,
                            it.product.name,
                            it.product.price.toString(),
                            it.product.price_unit,
                            it.product.image_1,
                            it.quantity
                        )
                    )
                }

                saleProductRecyclerViewAdapter.addData(productsList)
            }
        })

        viewModel.saleStatusLiveData.observe(this, {
            AlertDialog(
                context = this@ActivityAddNewSale,
                cancelOnOutsideClick = false,
                message = it.message ?: "",
                positive = getString(R.string.ok),
                positiveClick = {
                    val intent = Intent(this@ActivityAddNewSale, ActivityOrderDetailsCompleted::class.java)
                    intent.putExtra(Constant.ORDER_ID, it.data?.id ?: -1)
                    startActivity(intent)
                    finish()
                    overridePendingTransition(0,0)
                }
            ).show()
        })

        viewModel.collectionCenterLiveData.observe(this, { list ->
            viewDataBinding.collectionCenterList = list.collectionCenter.map { it.name }.toCollection(ArrayList())
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            toolbar.backBtn.setOnClickListener {
                onBackPressed()
            }

            addProductsBtn.setOnClickListener {
                if (::interest.isInitialized) {
                    val intent = Intent(this@ActivityAddNewSale, ActivityAddSaleProducts::class.java)
                    intent.putExtra(Constant.PRODUCT_DATA, productsList)

                    addProductResultCallback.launch(intent)
                }
            }

            inputInterestId.setOnClickListener {
                val intent = Intent(this@ActivityAddNewSale, ActivityMyInterests::class.java)
                intent.putExtra(Constant.IS_TO_ADD_SALE, true)

                interestIdResultCallback.launch(intent)
            }

            deliveryModeGroup.setOnCheckedChangeListener { _, checkedId ->
                if(checkedId == R.id.mode_collection_center) {
                    viewModel.getCollectionCenterList()
                }
            }

            submitBtn.setOnClickListener {
                val collectionCenter = inputCollectionCenter.text.toString().trim()
                val review = inputReview.text.toString().trim()
                val rating = ratingBar.rating
                val modeOfDelivery = if(modeCollectionCenter.isChecked) 1 else 0

                val collectionCenterId = viewModel.collectionCenterLiveData.value?.collectionCenter?.firstOrNull { it.name == collectionCenter }?.id ?: -1

                validateFormData(rating, review, modeOfDelivery, collectionCenterId)?.let { error ->
                    root.showMessage(error)
                } ?: run {
                    val saleDate = dateOfSaleValue.text.toString()
                    viewModel.addNewSale(interest.id, interest.buyer.id, modeOfDelivery, collectionCenterId, rating, review, saleDate, productsList)
                }
            }

            dateOfSaleContainer.setOnClickListener {
                UtilityActions.parseDate(dateOfSaleValue.text.toString())?.let { date ->
                    val calendar = Calendar.getInstance()
                    calendar.time = date

                    val datePickerDialog = DatePickerDialog(
                        this@ActivityAddNewSale,
                        { _, year, month, dayOfMonth ->
                            calendar.set(Calendar.YEAR, year)
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                            viewDataBinding.dateOfSaleValue.text = UtilityActions.formatDate(calendar.time)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.maxDate = Calendar.getInstance().timeInMillis
                    }

                    datePickerDialog.show()
                }
            }
        }
    }

    private fun validateFormData(rating: Float, review: String, modeOfDelivery: Int, collectionCenterId: Int): String? {
        return when {
            !::interest.isInitialized -> getString(R.string.please_select_a_interest)
            modeOfDelivery == 1 && collectionCenterId == -1 -> getString(R.string.select_a_collection_center)
            productsList.isEmpty() -> getString(R.string.please_enter_at_least_one_product)
            review.isEmpty() -> getString(R.string.please_add_a_review_for_this_sale)
            rating == 0F -> getString(R.string.please_rate_this_sale)
            else -> null
        }
    }
}