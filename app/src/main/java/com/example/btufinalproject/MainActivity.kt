package com.example.btufinalproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.btufinalproject.R
import com.example.btufinalproject.ui.FavouritesFragment
import com.example.btufinalproject.ui.FilmsCollectionFragment
import com.example.btufinalproject.ui.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val filmsFragment = FilmsCollectionFragment()
    private val searchFragment = SearchFragment()
    private val favouritesFragment = FavouritesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // საწყისად ფილმების ფრაგმენტი
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, filmsFragment)
            .commit()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_films -> switchFragment(filmsFragment)
                R.id.nav_search -> switchFragment(searchFragment)
                R.id.nav_favourites -> switchFragment(favouritesFragment)
            }
            true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}