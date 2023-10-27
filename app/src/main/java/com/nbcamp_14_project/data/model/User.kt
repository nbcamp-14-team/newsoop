package com.nbcamp_14_project.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
    var name: String? = null,
    var email: String? = null,
    var category: String? = null
)