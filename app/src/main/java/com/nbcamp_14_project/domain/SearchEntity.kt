package com.nbcamp_14_project.domain

import com.nbcamp_14_project.api.Items
import kotlinx.serialization.SerialName
import java.util.Date

data class SearchEntity(
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