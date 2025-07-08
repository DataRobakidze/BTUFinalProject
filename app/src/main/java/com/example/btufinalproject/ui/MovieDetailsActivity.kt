package com.example.btufinalproject.ui

import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.btufinalproject.R
import com.example.btufinalproject.data.models.Movie
import com.example.btufinalproject.viewmodels.FilmsViewModel


class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: FilmsViewModel // ან თუ გინდა, ახალი ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val movie = intent.getParcelableExtra<Movie>("MOVIE")
        if (movie == null) {
            finish()  // თუ movie არ მივიღეთ, activity-ს დახურვა
            return
        }

        // ახლა შეგიძლია გამოიყენო movie
        findViewById<TextView>(R.id.textTitle).text = movie.title
        findViewById<TextView>(R.id.textRating).text = String.format("%.1f", movie.voteAverage)
        findViewById<TextView>(R.id.textYear).text = movie.releaseDate.take(4)
        findViewById<TextView>(R.id.textRuntime).text = "${movie.runtime ?: 0} min"
        findViewById<TextView>(R.id.textGenres).text = movie.genres?.joinToString(", ") { it.name }

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500/${movie.posterPath}")
            .into(findViewById(R.id.imagePoster))

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500/${movie.backdropPath}")
            .into(findViewById(R.id.imageBackdrop))

        val trailerKey = movie.trailerKey
        if (!trailerKey.isNullOrEmpty()) {
            val webView = WebView(this)
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("https://www.youtube.com/embed/$trailerKey")
            findViewById<FrameLayout>(R.id.trailerContainer).addView(webView)
        }
    }

}

class FavoriteManager {

}
