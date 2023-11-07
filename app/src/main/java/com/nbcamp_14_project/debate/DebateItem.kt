package com.nbcamp_14_project.debate

import java.util.Date

data class DebateItem(
    var id: String = "",
    var title: String,
    var originalcontext: String,
    var agreecontext: String,
    var oppositecontext: String,
    var name: String,
    var userUID: String,
    var timestamp: Date // Timestamp 필드 추가
)
