package com.example.mymoviesdb.network.dto

data class SimilarData(
    val page: Int?,
    val results: List<Result>?,
    val total_pages: Int?,
    val total_results: Int?
)