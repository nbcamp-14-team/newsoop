package com.nbcamp_14_project.debate

data class DebateItem(
    var id: String = "", // 필드 타입은 실제로 사용되는 타입에 맞게 수정해야 합니다
    var title: String,
    var name: String, // 새로운 'name' 필드 추가
    var userUID: String
)
