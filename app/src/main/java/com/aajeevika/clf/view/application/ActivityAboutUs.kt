package com.aajeevika.clf.view.application

import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivity
import com.aajeevika.clf.databinding.ActivityAboutUsBinding

class ActivityAboutUs : BaseActivity<ActivityAboutUsBinding>(R.layout.activity_about_us) {
    override fun initListener() {
        viewDataBinding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}