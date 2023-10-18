package com.nbcamp_14_project.search


class GetSearchUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        query: String,
        display: Int? = null,
        start: Int? = null
    ) = repository.getNews(
        query,
        display,
        start
    )
}
