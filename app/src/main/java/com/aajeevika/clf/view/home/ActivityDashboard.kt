package com.aajeevika.clf.view.home

import BaseUrls
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.aajeevika.clf.BuildConfig
import com.aajeevika.clf.R
import com.aajeevika.clf.baseclasses.BaseActivityVM
import com.aajeevika.clf.databinding.ActivityDashboardBinding
import com.aajeevika.clf.utility.Constant
import com.aajeevika.clf.utility.UtilityActions
import com.aajeevika.clf.utility.app_enum.LanguageType
import com.aajeevika.clf.view.application.ActivityAboutUs
import com.aajeevika.clf.view.application.ActivityNotifications
import com.aajeevika.clf.view.auth.ActivityLogin
import com.aajeevika.clf.view.buy.ActivityBuyManager
import com.aajeevika.clf.view.dialog.AlertDialog
import com.aajeevika.clf.view.grievance.ActivityGrievance
import com.aajeevika.clf.view.home.adapter.ProductCategoryRecyclerViewAdapter
import com.aajeevika.clf.view.home.viewmodel.DashboardViewModel
import com.aajeevika.clf.view.interest.ActivityMyInterests
import com.aajeevika.clf.view.product.ActivityAddProductStepOne
import com.aajeevika.clf.view.profile.ActivityProfile
import com.aajeevika.clf.view.sales.ActivitySales
import com.aajeevika.clf.view.shgindividual.ActivityShgInd
import com.aajeevika.clf.view.survey.ActivitySurvey

class ActivityDashboard : BaseActivityVM<ActivityDashboardBinding, DashboardViewModel>(
    R.layout.activity_dashboard,
    DashboardViewModel::class
) {
    private val productCategoryRecyclerViewAdapter = ProductCategoryRecyclerViewAdapter { page ->
        viewModel.getHomeData(page)
    }

    private val callRequestCallback = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it == true) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${BaseUrls.contactUsNumber}"))
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.run {
            isSHGEnterprise = preferencesHelper.roleId == Constant.SHG_ENTERPRISE_ROLE_ID

            userEmail = preferencesHelper.email
            userName = preferencesHelper.name
            version = BuildConfig.VERSION_NAME

            if(preferencesHelper.profileImage.isNotEmpty())
                userProfileImage = BaseUrls.baseUrl + preferencesHelper.profileImage

            if(preferencesHelper.appLanguage == LanguageType.ENGLISH.code) languageSwitch.isChecked = false
            else if(preferencesHelper.appLanguage == LanguageType.HINDI.code) languageSwitch.isChecked = true
        }

        viewDataBinding.recyclerView.adapter = productCategoryRecyclerViewAdapter
        viewModel.getHomeData()
    }

    override fun onResume() {
        super.onResume()

        viewDataBinding.run {
            userEmail = preferencesHelper.email
            userName = preferencesHelper.name

            if(preferencesHelper.profileImage.isNotEmpty())
                userProfileImage = BaseUrls.baseUrl + preferencesHelper.profileImage
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.homeLiveData.observe(this, {
            stopSwipeToRefreshRefresh()
            productCategoryRecyclerViewAdapter.addData(
                it.products,
                it.categories.current_page,
                it.categories.last_page,
            )
        })
    }

    override fun initListener() {
        viewDataBinding.run {
            addFabBtn.setOnClickListener {
                val intent = Intent(this@ActivityDashboard, ActivityAddProductStepOne::class.java)
                startActivity(intent)
            }

            menuBtn.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }

            searchBtn.setOnClickListener {
                val intent = Intent(this@ActivityDashboard, ActivitySearchProduct::class.java)
                startActivity(intent)
            }

            notificationBtn.setOnClickListener {
                val intent = Intent(this@ActivityDashboard, ActivityNotifications::class.java)
                startActivity(intent)
            }

            profileContainer.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityProfile::class.java)
                startActivity(intent)
            }

            languageSwitch.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked && preferencesHelper.appLanguage != LanguageType.HINDI.code) {
                    preferencesHelper.appLanguage = LanguageType.HINDI.code

                    startActivity(intent)
                    finish()
                    overridePendingTransition(0, 0)
                } else if(!isChecked && preferencesHelper.appLanguage != LanguageType.ENGLISH.code) {
                    preferencesHelper.appLanguage = LanguageType.ENGLISH.code

                    startActivity(intent)
                    finish()
                    overridePendingTransition(0, 0)
                }
            }

            /*draftBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityDraft::class.java)
                startActivity(intent)
            }*/

            myInternetBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityMyInterests::class.java)
                startActivity(intent)
            }

            /*myOrderBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityMyOrder::class.java)
                startActivity(intent)
            }*/

            buyBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityBuyManager::class.java)
                startActivity(intent)
            }

            shgIndividualsBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityShgInd::class.java)
                startActivity(intent)
            }

            salesBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivitySales::class.java)
                startActivity(intent)
            }

            takeSurveyBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivitySurvey::class.java)
                startActivity(intent)
            }

            grievanveBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityGrievance::class.java)
                startActivity(intent)
            }

            /*faqBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityFaqs::class.java)
                startActivity(intent)
            }*/

            /*termsAndConditionsBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityWebView::class.java)
                intent.putExtra(Constant.TITLE, getString(R.string.terms_and_conditions))
                intent.putExtra(Constant.WEB_URL, BaseUrls.termsAndConditions)
                startActivity(intent)
            }*/

            /*privacyPolicyBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityWebView::class.java)
                intent.putExtra(Constant.TITLE, getString(R.string.privacy_policy))
                intent.putExtra(Constant.WEB_URL, BaseUrls.privacyPolicy)
                startActivity(intent)
            }*/

            supportBtn.setOnClickListener {
                callRequestCallback.launch(Manifest.permission.CALL_PHONE)
            }

            aboutUsBtn.setOnClickListener {
                viewDataBinding.drawerLayout.closeDrawer(GravityCompat.START)
                val intent = Intent(this@ActivityDashboard, ActivityAboutUs::class.java)
                startActivity(intent)
            }

            logoutBtn.setOnClickListener {
                AlertDialog(
                    context = this@ActivityDashboard,
                    message = getString(R.string.logout_confirmation_message),
                    positive = getString(R.string.cancel),
                    negative = getString(R.string.yes_logout),
                    negativeClick = {
                        preferencesHelper.clear()
                        UtilityActions.updateFCM(preferencesHelper)

                        val intent = Intent(this@ActivityDashboard, ActivityLogin::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                ).show()
            }

            drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
                override fun onDrawerStateChanged(newState: Int) {}
                override fun onDrawerClosed(drawerView: View) {}

                override fun onDrawerOpened(drawerView: View) {
                    UtilityActions.closeKeyboard(this@ActivityDashboard, viewDataBinding.root)
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getHomeData()
            }
        }
    }
}