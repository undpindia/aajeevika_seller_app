package com.aajeevika.clf.view.survey.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseRecyclerViewAdapter
import com.aajeevika.clf.baseclasses.BaseViewHolder
import com.aajeevika.clf.databinding.ListItemActiveSurveyBinding
import com.aajeevika.clf.databinding.ListItemExpiredSurveyBinding
import com.aajeevika.clf.model.data_model.SurveyData
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.view.application.ActivityWebView
import java.util.*
import kotlin.collections.ArrayList

class SurveyRecyclerViewAdapter: BaseRecyclerViewAdapter() {
    private val dataList = ArrayList<SurveyData>()

    fun addData(data: ArrayList<SurveyData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BaseViewHolder? {
        return when(viewType) {
            1-> SurveyViewHolder(ListItemActiveSurveyBinding.inflate(inflater, parent, false))
            else-> ExpiredSurveyViewHolder(ListItemExpiredSurveyBinding.inflate(inflater, parent, false))
        }
    }

    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int): Int {
        val data = dataList[position]
        val isExpired = UtilityActions.parseInterestDate(data.end_date)?.before(Date()) ?: true
        return if(isExpired) 0 else 1
    }

    private inner class SurveyViewHolder(val bindingAdapter: ListItemActiveSurveyBinding): BaseViewHolder(bindingAdapter) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            bindingAdapter.run {
                message = data.message
                UtilityActions.parseInterestDate(data.start_date)?.run { createDate = UtilityActions.formatTicketDate(this) }
                UtilityActions.parseInterestDate(data.end_date)?.run { expireDate = UtilityActions.formatTicketDate(this) }
            }

            bindingAdapter.takeSurveyBtn.setOnClickListener {
                val isExpired = UtilityActions.parseInterestDate(data.end_date)?.before(Date()) ?: true

                if(!isExpired) {
                    val intent = Intent(context, ActivityWebView::class.java)
                    intent.putExtra(Constant.TITLE, data.message)
                    intent.putExtra(Constant.WEB_URL, data.google_url)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, context.getString(R.string.survey_expired), Toast.LENGTH_SHORT).show()
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }

    private inner class ExpiredSurveyViewHolder(val bindingAdapter: ListItemExpiredSurveyBinding): BaseViewHolder(bindingAdapter) {
        override fun bindData(context: Context) {
            val data = dataList[adapterPosition]

            bindingAdapter.run {
                message = data.message
                UtilityActions.parseInterestDate(data.start_date)?.run { createDate = UtilityActions.formatTicketDate(this) }
            }
        }
    }
}