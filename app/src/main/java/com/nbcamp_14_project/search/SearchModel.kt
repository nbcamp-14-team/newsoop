package com.nbcamp_14_project.search

data class SearchModel(
    val title: String,
    val data: String,
    var isLike: Boolean = false
)