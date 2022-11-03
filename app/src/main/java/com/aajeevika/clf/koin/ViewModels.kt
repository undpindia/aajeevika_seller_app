package com.aajeevika.clf.koin

import com.aajeevika.clf.view.application.viewmodel.*
import com.aajeevika.clf.view.auth.viewmodel.*
import com.aajeevika.clf.view.buy.viewmodel.BuyDetailViewModel
import com.aajeevika.clf.view.buy.viewmodel.BuyManagerViewModel
import com.aajeevika.clf.view.documents.viewmodel.DocumentsViewModel
import com.aajeevika.clf.view.grievance.viewmodel.CreateGrievanceViewModel
import com.aajeevika.clf.view.grievance.viewmodel.GrievanceChatViewModel
import com.aajeevika.clf.view.grievance.viewmodel.GrievanceViewModel
import com.aajeevika.clf.view.home.viewmodel.DashboardViewModel
import com.aajeevika.clf.view.home.viewmodel.SearchViewModel
import com.aajeevika.clf.view.interest.viewmodel.InterestsViewModel
import com.aajeevika.clf.view.interest.viewmodel.MyInterestsViewModel
import com.aajeevika.clf.view.order.viewmodel.MyOrderViewModel
import com.aajeevika.clf.view.order.viewmodel.OrderDetailsViewModel
import com.aajeevika.clf.view.product.viewmodel.*
import com.aajeevika.clf.view.profile.viewmodel.ProfileViewModel
import com.aajeevika.clf.view.sales.viewmodel.AddNewSaleViewModel
import com.aajeevika.clf.view.sales.viewmodel.AddSaleProductsViewModel
import com.aajeevika.clf.view.sales.viewmodel.SalesViewModel
import com.aajeevika.clf.view.shgindividual.viewmodel.ShgIndViewModel
import com.aajeevika.clf.view.survey.viewmodel.SurveyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myViewModel = module {
    viewModel { SplashScreenViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { OtpVerificationViewModel() }
    viewModel { LoginViewModel() }
    viewModel { ForgotPasswordViewModel() }
    viewModel { ResetPasswordViewModel() }
    viewModel { DocumentsViewModel() }
    viewModel { DashboardViewModel() }
    viewModel { NotificationsViewModel() }
    viewModel { FaqViewModel() }
    viewModel { RatingAndReviewsViewModel() }
    viewModel { GrievanceViewModel() }
    viewModel { CreateGrievanceViewModel() }
    viewModel { MyInterestsViewModel() }
    viewModel { InterestsViewModel() }
    viewModel { DraftViewModel() }
    viewModel { SalesViewModel() }
    viewModel { AddNewSaleViewModel() }
    viewModel { SubCategoryViewModel() }
    viewModel { ProductDetailViewModel() }
    viewModel { ProductAddViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { AddSaleProductsViewModel() }
    viewModel { MyOrderViewModel() }
    viewModel { OrderDetailsViewModel() }
    viewModel { SearchViewModel() }
    viewModel { BuyDetailViewModel() }
    viewModel { BuyManagerViewModel() }
    viewModel { ShgIndViewModel() }
    viewModel { GrievanceChatViewModel() }
    viewModel { SurveyViewModel() }
}