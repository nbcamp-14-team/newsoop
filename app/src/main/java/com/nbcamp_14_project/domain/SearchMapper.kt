package com.nbcamp_14_project.domain

import com.nbcamp_14_project.api.NewsDTO

fun NewsDTO.toSearchEntity() = SearchEntity(

    lastBuildDate = lastBuildDate,
    total = total,
    start = start,
    display = display,
    items = items,

)