package com.nbcamp_14_project.debate

import com.google.firebase.firestore.DocumentId

data class DebateDetailItem(
    var id: String = "", // 수정: id를 String으로 변경
    var text: String,
    var user: String,
    var date: String,
    var userUID: String
)
