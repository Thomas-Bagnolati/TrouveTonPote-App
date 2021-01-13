package com.yrmt.trouvetonpote.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yrmt.trouvetonpote.R
import com.yrmt.trouvetonpote.model.ResponseCodeBean
import com.yrmt.trouvetonpote.model.UserBean
import com.yrmt.trouvetonpote.utils.AuthService
import com.yrmt.trouvetonpote.utils.WsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    // Graphics
    private lateinit var etEmailLogin: TextInputEditText
    private lateinit var tiEmailLogin: TextInputLayout
    private lateinit var etPasswordLogin: TextInputEditText
    private lateinit var tiPasswordLogin: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Graphics
        etEmailLogin = findViewById<TextInputEditText>(R.id.et_email_login)
        tiEmailLogin = findViewById<TextInputLayout>(R.id.ti_email_login)
        etPasswordLogin = findViewById<TextInputEditText>(R.id.et_password_login)
        tiPasswordLogin = findViewById<TextInputLayout>(R.id.ti_password_login)
    }

    ///////////////////////////////////////////////////////////////////////////
    // ONCLICK
    ///////////////////////////////////////////////////////////////////////////
    fun onClickRegister(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun onClickLogin(view: View) {

        if (verifyErrorTextInput(etEmailLogin, etPasswordLogin, tiEmailLogin, tiPasswordLogin)) {

            CoroutineScope(IO).launch {
                val res = WsUtils.login(etEmailLogin.text.toString(), etPasswordLogin.text.toString())
                if (res.code == 200) {
                    updateUI("Vous êtes bien connecté")
                } else {
                    updateUI(res.message ?: "biscuit" )
                }
            }

        }

    }

    suspend fun updateUI(msg: String) {
        withContext(Main) {
            Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // REQUESTS
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // OTHERS
    ///////////////////////////////////////////////////////////////////////////

    // Verify email & Password
    private fun verifyErrorTextInput(etEmail: EditText, etPassword: EditText, tiEmail: TextInputLayout, tiPassword: TextInputLayout): Boolean {

        //Clean error messages
        tiEmail.error = null
        tiPassword.error = null

        //Case wrong email
        if (!AuthService.isEmailValide(etEmail)) {
            tiEmail.error = getString(R.string.error_mail)
            return false
        }

        //Case Wrong password
        else if (!AuthService.isPasswordValide(etPassword)) {
            tiPassword.error = getString(R.string.error_password)
            return false

        } else {
            return true
        }
    }

}