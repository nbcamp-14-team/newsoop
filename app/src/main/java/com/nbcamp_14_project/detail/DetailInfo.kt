package com.nbcamp_14_project.detail

import android.os.Parcelable
import android.text.BoringLayout
import com.nbcamp_14_project.home.HomeModel
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

fun DetailInfo.toHomeModel(): HomeModel {
    return HomeModel(
        title = title,
        description = description,
        thumbnail = thumbnail,
        author = author,
        link = originalLink,
        isLike = isLike
    )


}
