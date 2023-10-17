package com.nbcamp_14_project.detail

import java.io.Serializable

data class DetailInfo (
    val title : String?,
    val description : String?,
    val thumbnail: String? = null,
    val originalLink: String?,
    val pubDate: String?
):Serializable
