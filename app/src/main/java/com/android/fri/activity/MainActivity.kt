package com.android.fri.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.fri.R
import com.android.fri.fragments.FavoriteFragment
import com.android.fri.fragments.HomeFragment
import com.android.fri.fragments.AboutFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val homeFragment = HomeFragment()
    val favoriteFragment = FavoriteFragment()
    val settingsFragment = AboutFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottom_navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        makeCurrentFragment(homeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_favorite -> makeCurrentFragment(favoriteFragment)
                R.id.ic_person -> makeCurrentFragment(settingsFragment)
            }
            true
        }

    }



  private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment).addToBackStack(null)
            commit() }

}

