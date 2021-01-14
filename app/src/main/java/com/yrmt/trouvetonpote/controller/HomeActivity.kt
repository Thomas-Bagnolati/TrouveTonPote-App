package com.yrmt.trouvetonpote.controller

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.yrmt.trouvetonpote.R
import com.yrmt.trouvetonpote.model.UserBean
import com.yrmt.trouvetonpote.utils.WsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

lateinit var user: UserBean

class HomeActivity : AppCompatActivity() {

    private lateinit var tv_home_data: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tv_home_data = findViewById(R.id.tv_home_data)

        session()
    }

    private fun session() {
        if (intent != null) {
            val idSession = intent.getLongExtra("id_session", -1)
            user = UserBean(idSession)
        }
    }

    fun onClickGetUsersInfo(view: View) {
        CoroutineScope(IO).launch {
            try {
                val listUserBean = WsUtils.getUsersInfo(user)
                CoroutineScope(Main).launch {
                    tv_home_data.append(listUserBean.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                CoroutineScope(Main).launch {
                    Toast.makeText(this@HomeActivity, e.message ?: "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}