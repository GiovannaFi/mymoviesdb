package com.example.mymoviesdb.network.dto

data class CreditsData(
    val cast: List<Cast>,
    val crew: List<Crew?>?,
    val id: Int?
)