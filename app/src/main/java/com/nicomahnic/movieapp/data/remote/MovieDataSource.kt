package com.nicomahnic.movieapp.data.remote

import com.nicomahnic.movieapp.data.model.MovieList
import com.nicomahnic.movieapp.repository.WebService
import com.nicomahnic.movieapp.utils.AppConstants

class MovieDataSource(private val webService: WebService) {

    suspend fun getUpcomingMovies(): MovieList =
        webService.getUpcomingMovies(AppConstants.API_KEY)

    suspend fun getTopRatedMovies(): MovieList =
        webService.getTopRatedMovies(AppConstants.API_KEY)

    suspend fun getPopularMovies(): MovieList =
        webService.getPopularMovies(AppConstants.API_KEY)

}