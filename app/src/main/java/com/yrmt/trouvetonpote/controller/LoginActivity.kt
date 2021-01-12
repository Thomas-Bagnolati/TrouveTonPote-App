package com.yrmt.trouvetonpote.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yrmt.trouvetonpote.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onClickRegister(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}