package com.nbcamp_14_project.api

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date
/**
 * API를 통해서 가져온 데이터값을 분류해주는 data Class 입니다.
 */
@Serializable
data class NewsDTO(
    @SerialName("lastBuildDate")
    val lastBuildDate: String? ="",
    @SerialName("total")
    val total: Int?,
    @SerialName("start")
    val start: Int?,
    @SerialName("display")
    val display: Int?,
    @SerialName("item")
    val items: List<Items>?,
)


data class Items(
    @SerialName("title")
    val title: String?,
    @SerialName("link")
    val link: String?,
    @SerialName("originallink")
    val originallink: String?,
    @SerialName("articleUrl")
    val articleUrl: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("pubDate")
    val pubDate: String?,

    )