package com.aajeevika.individual.koin

import com.aajeevika.individual.view.auth.viewmodel.*
import com.aajeevika.individual.view.chat.viewmodel.ChatViewModel
import com.aajeevika.individual.view.documents.viewmodel.DocumentsViewModel
import com.aajeevika.individual.view.grievance.viewmodel.CreateGrievanceViewModel
import com.aajeevika.individual.view.grievance.viewmodel.GrievanceChatViewModel
import com.aajeevika.individual.view.grievance.viewmodel.GrievanceViewModel
import com.aajeevika.individual.view.home.viewmodel.DashboardViewModel
import com.aajeevika.individual.view.notification.viewmodel.NotificationsViewModel
import com.aajeevika.individual.view.other.viewmodel.FaqViewModel
import com.aajeevika.individual.view.profile.viewmodel.IndividualProfileViewModel
import com.aajeevika.individual.view.profile.viewmodel.ProfileViewModel
import com.aajeevika.individual.view.profile.viewmodel.RatingAndReviewsViewModel
import com.aajeevika.individual.view.profile.viewmodel.SelectInterestViewModel
import com.aajeevika.individual.view.sale.viewmodel.AddNewSaleViewModel
import com.aajeevika.individual.view.sale.viewmodel.AddProductViewModel
import com.aajeevika.individual.view.sale.viewmodel.SaleDetailViewModel
import com.aajeevika.individual.view.sale.viewmodel.SalesViewModel
import com.aajeevika.individual.view.survey.viewmodel.SurveyViewModel
import com.aajeevika.individual.view.suvidha.viewmodel.SadhanViewModel
import com.aajeevika.individual.view.suvidha.viewmodel.SarvottamPrathayeViewModel
import com.aajeevika.individual.view.suvidha.viewmodel.SuvidhaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myViewModel = module {
    viewModel { ForgotPasswordViewModel() }
    viewModel { LoginViewModel() }
    viewModel { OtpVerificationViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { ResetPasswordViewModel() }
    viewModel { SplashScreenViewModel() }
    viewModel { DocumentsViewModel() }
    viewModel { CreateGrievanceViewModel() }
    viewModel { GrievanceViewModel() }
    viewModel { DashboardViewModel() }
    viewModel { NotificationsViewModel() }
    viewModel { FaqViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { RatingAndReviewsViewModel() }
    viewModel { SelectInterestViewModel() }
    viewModel { SalesViewModel() }
    viewModel { AddNewSaleViewModel() }
    viewModel { AddProductViewModel() }
    viewModel { IndividualProfileViewModel() }
    viewModel { SaleDetailViewModel() }
    viewModel { SurveyViewModel() }
    viewModel { GrievanceChatViewModel() }
    viewModel { ChatViewModel() }
    viewModel { SuvidhaViewModel() }
    viewModel { SarvottamPrathayeViewModel() }
    viewModel { SadhanViewModel() }
}