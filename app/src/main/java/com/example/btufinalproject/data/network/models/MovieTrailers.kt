package com.example.btufinalproject.data.models

data class MovieTrailers(
    val results: List<Trailer>
) {
    data class Trailer(
        val key: String
    )
}
