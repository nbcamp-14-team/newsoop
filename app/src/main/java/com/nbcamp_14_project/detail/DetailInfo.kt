package com.nbcamp_14_project.detail

import android.os.Parcelable
import android.text.BoringLayout
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.sql.Date

@Parcelize
data class DetailInfo (
    val title : String?,
    val description : String?,
    val thumbnail: String? = null,
    val author: String? = null,
    val originalLink: String?,
    val pubDate: String?,
    var isLike:Boolean?
):Parcelable
