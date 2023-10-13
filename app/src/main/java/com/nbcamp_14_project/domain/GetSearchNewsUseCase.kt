package com.nbcamp_14_project.domain

import com.nbcamp_14_project.Main.MainFragmentRepository
import retrofit2.http.Query

class GetSearchNewsUseCase(
    private val repository: MainFragmentRepository
){
    suspend operator fun invoke(
        query: String,
        display: Int?= null,
        start: Int? = null
    )= repository.getNews(
            query,
            display,
            start
        )
}