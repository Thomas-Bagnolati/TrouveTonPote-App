package com.yrmt.trouvetonpote.controller

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yrmt.trouvetonpote.R
import com.yrmt.trouvetonpote.model.UserBean
import com.yrmt.trouvetonpote.utils.WsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

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

    fun onClickSendUserInfo(view: View) {

        //set user info to send
        user.lat = 350.12
        user.lng = 120.1
        user.isShared = 1
        user.status_mess = ""

        CoroutineScope(IO).launch {
            try {
                WsUtils.sendUserInfo(user)
                withContext(Main) {
                    Toast.makeText(this@HomeActivity, "Infos envoyées", Toast.LENGTH_SHORT).show()
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                withContext(Main) {
                    Toast.makeText(this@HomeActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}