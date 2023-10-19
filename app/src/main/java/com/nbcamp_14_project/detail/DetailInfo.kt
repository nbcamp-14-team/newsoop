package com.nbcamp_14_project.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
data class DetailInfo (
    val title : String?,
    val description : String?,
    val thumbnail: String? = null,
    val author: String? = null,
    val originalLink: String?,
    val pubDate: String?,
):Parcelable