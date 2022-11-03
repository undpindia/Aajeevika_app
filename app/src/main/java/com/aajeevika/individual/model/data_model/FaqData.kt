package com.aajeevika.individual.model.data_model

import android.os.Parcelable
import java.io.Serializable

data class FaqModel(
    val list: ArrayList<FaqData>?= arrayListOf()
)

data class FaqData(
    val id: String,
    val title: String,
    val get_question: ArrayList<FaqQuestions>,
    var expanded: Boolean = false
)


data class FaqQuestions(
    val id: String,
    val faq_topic_id: String,
    val question: String,
    val desc: String,
    var expanded: Boolean = false
) : Serializable