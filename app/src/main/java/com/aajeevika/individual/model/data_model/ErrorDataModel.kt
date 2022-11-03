package com.aajeevika.individual.model.data_model

import com.aajeevika.individual.utility.app_enum.ErrorType

data class ErrorDataModel(
    val message: String,
    val errorType: ErrorType,
)