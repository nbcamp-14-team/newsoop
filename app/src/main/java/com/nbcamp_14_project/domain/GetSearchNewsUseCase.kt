package com.nbcamp_14_project.domain

import com.nbcamp_14_project.home.MainFragmentRepository

class GetSearchNewsUseCase(
    private val repository: MainFragmentRepository
){
    suspend operator fun invoke(
        query: String,
        display: Int?= null,
        start: Int? = null,
        sort:String? = null,
    )= repository.getNews(
            query,
            display,
            start,
            sort
        )
}