package com.example.btufinalproject.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.btufinalproject.adapter.MovieAdapter
import com.example.btufinalproject.databinding.FragmentSearchBinding
import com.example.btufinalproject.viewmodels.FilmsViewModel
import androidx.recyclerview.widget.LinearLayoutManager

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MovieAdapter
    private val viewModel: FilmsViewModel by viewModels()

    private var filterOption = FilterOption.NAME

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter { movie ->
            val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
            intent.putExtra("MOVIE", movie)
            startActivity(intent)
        }

        binding.recyclerViewMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMovies.adapter = adapter

        binding.filterButton.setOnClickListener {
            showFilterMenu()
        }

        binding.searchEditText.addTextChangedListener { editable ->
            filterMovies(editable.toString())
        }

        viewModel.movies.observe(viewLifecycleOwner) {
            filterMovies(binding.searchEditText.text.toString())
        }

        viewModel.fetchMovies()
    }

    private fun filterMovies(searchText: String) {
        val allMovies = viewModel.movies.value ?: return

        val filtered = when(filterOption) {
            FilterOption.NAME -> allMovies.filter {
                it.title.contains(searchText, ignoreCase = true)
            }
            FilterOption.GENRE -> allMovies.filter { movie ->
                movie.genres?.any { it.name.contains(searchText, ignoreCase = true) } == true
            }
            FilterOption.YEAR -> allMovies.filter {
                it.releaseDate.startsWith(searchText)
            }
        }

        adapter.submitList(filtered)

        binding.textViewEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showFilterMenu() {
        val popup = PopupMenu(requireContext(), binding.filterButton)
        popup.menu.add("Name")
        popup.menu.add("Genre")
        popup.menu.add("Year")
        popup.setOnMenuItemClickListener { menuItem ->
            filterOption = when(menuItem.title) {
                "Name" -> FilterOption.NAME
                "Genre" -> FilterOption.GENRE
                "Year" -> FilterOption.YEAR
                else -> FilterOption.NAME
            }
            filterMovies(binding.searchEditText.text.toString())
            true
        }
        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

enum class FilterOption {
    NAME, GENRE, YEAR
}
