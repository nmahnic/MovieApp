package com.nicomahnic.movieapp.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.nicomahnic.movieapp.R
import com.nicomahnic.movieapp.core.Resource
import com.nicomahnic.movieapp.data.model.Movie
import com.nicomahnic.movieapp.data.remote.MovieDataSource
import com.nicomahnic.movieapp.databinding.FragmentMovieBinding
import com.nicomahnic.movieapp.presentation.MovieViewModel
import com.nicomahnic.movieapp.presentation.MovieViewModelFactory
import com.nicomahnic.movieapp.repository.MovieRepositoryImpl
import com.nicomahnic.movieapp.repository.RetrofitClient
import com.nicomahnic.movieapp.ui.movie.adapters.MovieAdapter
import com.nicomahnic.movieapp.ui.movie.adapters.concat.PopularConcatAdapter
import com.nicomahnic.movieapp.ui.movie.adapters.concat.TopRatedConcatAdapter
import com.nicomahnic.movieapp.ui.movie.adapters.concat.UpcomingConcatAdapter

class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMovieBinding
    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactory(
            MovieRepositoryImpl(
                MovieDataSource(RetrofitClient.webService)
            )
        )
    }
    private lateinit var concatAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)

        concatAdapter = ConcatAdapter()

        viewModel.fetchMainScreenMovies().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("LiveData", "Loading...")
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(0,UpcomingConcatAdapter(MovieAdapter(result.data.first.results, this@MovieFragment)))
                        addAdapter(1,TopRatedConcatAdapter(MovieAdapter(result.data.second.results, this@MovieFragment)))
                        addAdapter(2,PopularConcatAdapter(MovieAdapter(result.data.third.results, this@MovieFragment)))
                    }

                    binding.rvMovies.adapter = concatAdapter
//                    Log.d("LiveData", "Upcoming ${result.data.first}")
//                    Log.d("LiveData", "TopRated ${result.data.second}")
//                    Log.d("LiveData", "Popular ${result.data.third}")
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("LiveData", "${result.exception}")
                }
            }
        })

    }

    override fun onMovieClick(movie: Movie) {
        Log.d("Movie", "onMovieClick: $movie")
        val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
            posterImageUrl = movie.poster_path,
            backgroundImageUrl = movie.backdrop_path,
            voteAverage = movie.vote_average.toFloat(),
            voteCount = movie.vote_count,
            overview = movie.overview,
            title = movie.short_title,
            language = movie.original_language,
            releaseDate = movie.release_date
        )
        findNavController().navigate(action)
    }
}