package com.yrmt.trouvetonpote.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yrmt.trouvetonpote.R
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
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Graphics
        etEmailLogin = findViewById(R.id.et_email_login)
        tiEmailLogin = findViewById(R.id.ti_email_login)
        etPasswordLogin = findViewById(R.id.et_password_login)
        tiPasswordLogin = findViewById(R.id.ti_password_login)
        rootView = findViewById(R.id.root_login)
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
                try {
                    val res = WsUtils.login(etEmailLogin.text.toString(), etPasswordLogin.text.toString())
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    updateUI()
                }
            }
        }
    }

    private suspend fun updateUI() {
        withContext(Main) {
            Snackbar.make(rootView, getString(R.string.error_auth), Snackbar.LENGTH_SHORT).show()
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