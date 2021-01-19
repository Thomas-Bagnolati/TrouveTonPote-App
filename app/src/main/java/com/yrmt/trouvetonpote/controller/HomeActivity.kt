package com.yrmt.trouvetonpote.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yrmt.trouvetonpote.MapsFragment
import com.yrmt.trouvetonpote.R
import com.yrmt.trouvetonpote.model.UserBean


lateinit var user: UserBean
lateinit var profilFragment: ProfilFragment
lateinit var mapsFragment: MapsFragment
lateinit var chatFragment: ChatFragment


class HomeActivity : AppCompatActivity() {

    private lateinit var navbarBottom: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navbarBottom = findViewById(R.id.navbar_bottom)

        // Fragments
        profilFragment = ProfilFragment()
        mapsFragment = MapsFragment()
        chatFragment = ChatFragment()

        // NavBar Navigation
        navbarBottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_profil -> makeNavCurrentFragment(profilFragment)
                R.id.nav_map -> makeNavCurrentFragment(mapsFragment)
                R.id.nav_chat -> makeNavCurrentFragment(chatFragment)
            }
            true
        }

        // Start Fragment
        navbarBottom.selectedItemId = R.id.nav_map

        session()
    }

    private fun session() {
        if (intent != null) {
            val idSession = intent.getLongExtra("id_session", -1)
            user = UserBean(idSession)
        }
    }

    // Change current fragment
    private fun makeNavCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_homeactivity, fragment)
            commit()
        }
    }
}
