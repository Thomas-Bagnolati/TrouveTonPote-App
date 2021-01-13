package com.yrmt.trouvetonpote.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yrmt.trouvetonpote.R
import com.yrmt.trouvetonpote.model.UserBean

lateinit var user: UserBean

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        session()

    }

    private fun session() {
        if (intent != null) {
            val idSession = intent.getLongExtra("id_session",-1)
            user = UserBean(idSession)
        }


    }
}