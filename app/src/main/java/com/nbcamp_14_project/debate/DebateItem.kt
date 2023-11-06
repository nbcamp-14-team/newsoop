package com.nbcamp_14_project.debate

data class DebateItem(
    var id: String = "",
    var title: String,
    var originalcontext: String,
    var agreecontext: String,
    var oppositecontext: String,
    var name: String,
    var userUID: String
)
