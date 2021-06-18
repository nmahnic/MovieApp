package com.nicomahnic.movieapp.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int = -1,
    val adult: Boolean = false,
    val genre_ids: List<Int> = listOf(),
    val backdrop_path: String = "",
    val original_title: String = "",
    val original_language: String = "",
    val overview: String = "",
    val popularity: Double = -1.0,
    val poster_path: String = "",
    val release_date: String = "",
    @SerializedName("title")
    val short_title: String = "",
    // SerializedName permite usar una variable de fantasia en nuestro modelo
    // pero cuando vaya a buscar el dato al servidor use la que está en la API
    val video: Boolean = false,
    val vote_average: Double = -1.0,
    val vote_count: Int = -1
)

data class MovieList(val results: List<Movie> = listOf())