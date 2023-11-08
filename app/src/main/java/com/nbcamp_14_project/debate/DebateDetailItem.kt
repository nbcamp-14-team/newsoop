package com.nbcamp_14_project.debate

data class DebateDetailItem(
    val commentType: Int,
    var id: String = "",
    var text: String,
    var user: String,
    var date: String,
    var userUID: String
)
