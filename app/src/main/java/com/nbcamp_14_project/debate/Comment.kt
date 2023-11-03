package com.nbcamp_14_project.debate

data class Comment(
    val text: String,
    val user: String,
    val date: String,
    val commentType: String // Comment.TYPE_AGREE 또는 Comment.TYPE_OPPOSE 중 하나
) {
    companion object {
        const val TYPE_AGREE = 1
        const val TYPE_OPPOSE = 2
    }
}
