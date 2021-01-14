package com.yrmt.trouvetonpote.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
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

    // Data
    private val requestCodeForResult = 50

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
    fun onClickActivityRegister(view: View) {
        val intentRegister = Intent(this, RegisterActivity::class.java)
        startActivityForResult(intentRegister, requestCodeForResult)
    }


    fun onClickLogin(view: View) {

        if (verifyErrorTextInput(etEmailLogin, etPasswordLogin, tiEmailLogin, tiPasswordLogin)) {

            CoroutineScope(IO).launch {
                try {
                    val res = WsUtils.login(etEmailLogin.text.toString(), etPasswordLogin.text.toString())
                    val homeIntent = Intent(this@LoginActivity, HomeActivity::class.java)
                    homeIntent.putExtra("id_session", res.id_session)
                    startActivity(homeIntent)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                    updateUI(e.message ?: getString(R.string.error_auth))
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // OTHERs
    ///////////////////////////////////////////////////////////////////////////

    private suspend fun updateUI(errorMsg: String) {
        withContext(Main) {
            Snackbar.make(rootView, errorMsg, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeForResult && resultCode == Activity.RESULT_OK) {
            // Take mail and pwd
            val mail = data?.getStringExtra("mail") ?: ""
            val pwd = data?.getStringExtra("pwd") ?: ""
            // Fill on InputText
            etEmailLogin.append(mail)
            etPasswordLogin.append(pwd)

            Toast.makeText(this, getString(R.string.complete_registration), Toast.LENGTH_SHORT).show()

            etEmailLogin.clearFocus()
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // VERIFICATIONS
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