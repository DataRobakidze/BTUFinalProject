//package com.example.btufinalproject.ui
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.activity.viewModels
//import androidx.recyclerview.widget.GridLayoutManager
//import com.example.btufinalproject.adapter.MovieAdapter
//import com.example.btufinalproject.databinding.ActivityFilmsCollectionBinding
//import com.example.btufinalproject.viewmodels.FilmsViewModel
//
//
//class FilmsCollectionFragment : AppCompatActivity() {
//
//    private val viewModel: FilmsViewModel by viewModels()
//    private lateinit var adapter: MovieAdapter
//    private lateinit var binding: ActivityFilmsCollectionBinding  // კლასის დონეზე
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityFilmsCollectionBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        adapter = MovieAdapter { movie ->
//            val intent = Intent(this, MovieDetailsActivity::class.java)
//            intent.putExtra("MOVIE", movie)
//            startActivity(intent)
//        }
//
//        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
//        binding.recyclerView.adapter = adapter
//
//        viewModel.movies.observe(this, { movieList ->
//            adapter.submitList(movieList)
//        })
//
//        viewModel.fetchMovies()
//    }
//}

package com.example.btufinalproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.btufinalproject.adapter.MovieAdapter
import com.example.btufinalproject.databinding.ActivityFilmsCollectionBinding
import com.example.btufinalproject.ui.MovieDetailsActivity
import com.example.btufinalproject.viewmodels.FilmsViewModel

class FilmsCollectionFragment : Fragment() {

    private var _binding: ActivityFilmsCollectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FilmsViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityFilmsCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter { movie ->
            val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
            intent.putExtra("MOVIE", movie)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter

        viewModel.movies.observe(viewLifecycleOwner) { movieList ->
            adapter.submitList(movieList)
        }

        viewModel.fetchMovies()
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Movies"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
