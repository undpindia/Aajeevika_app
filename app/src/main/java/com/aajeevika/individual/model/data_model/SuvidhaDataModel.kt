package com.aajeevika.individual.model.data_model

import java.io.Serializable

data class SuvidhaListData(
    val list: ArrayList<SuvidhaData>,
)

data class SuvidhaData(
    val id: Int,
    val title: String,
    val desc: String,
    val image1: String?,
    val image2: String?,
    val image3: String?,
    val image4: String?,
) : Serializable

data class SarvottamPrathayeListData(
    val list: ArrayList<SarvottamPrathayeData>
)

data class SarvottamPrathayeData(
    val id: Int,
    val name: String,
    val download_link: String,
)

data class SadhanListData(
    val sadhan_list: ArrayList<SadhanData>,
)

data class SadhanData(
    val id: Int,
    val catName: String,
    val get_sadhan: ArrayList<VideoData>,
)

data class VideoData(
    val id: Int,
    val youtube_url: String,
    val title_en: String,
    val title_hi: String,
    val created_at: String,
)