package com.yrmt.trouvetonpote.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yrmt.trouvetonpote.R
import com.yrmt.trouvetonpote.model.UserBean
import com.yrmt.trouvetonpote.utils.WsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

lateinit var user: UserBean
lateinit var profilFragment: ProfilFragment
lateinit var mapFragment: MapFragment
lateinit var chatFragment: ChatFragment


class HomeActivity : AppCompatActivity() {

//    private lateinit var tv_home_data: TextView
    private lateinit var navbarBottom: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        tv_home_data = findViewById(R.id.tv_home_data)
        navbarBottom = findViewById(R.id.navbar_bottom)

        // Fragments
        profilFragment = ProfilFragment()
        mapFragment = MapFragment()
        chatFragment = ChatFragment()

        // NavBar Navigation
        navbarBottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_profil -> makeNavCurrentFragment(profilFragment)
                R.id.nav_map -> makeNavCurrentFragment(mapFragment)
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



//    fun onClickGetUsersInfo(view: View) {
//        CoroutineScope(IO).launch {
//            try {
//                val listUserBean = WsUtils.getUsersInfo(user)
//                CoroutineScope(Main).launch {
//                    tv_home_data.append(listUserBean.toString())
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                CoroutineScope(Main).launch {
//                    Toast.makeText(this@HomeActivity, e.message ?: "Une erreur est survenue", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    fun onClickSendUserInfo(view: View) {
//
//        //set user info to send
//        user.lat = 350.12
//        user.lng = 120.1
//        user.isShared = 1
//        user.status_mess = ""
//
//        CoroutineScope(IO).launch {
//            try {
//                WsUtils.sendUserInfo(user)
//                withContext(Main) {
//                    Toast.makeText(this@HomeActivity, "Infos envoyées", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                withContext(Main) {
//                    Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    // Change current fragment
    private fun makeNavCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_homeactivity, fragment)
            commit()
        }
    }
}
