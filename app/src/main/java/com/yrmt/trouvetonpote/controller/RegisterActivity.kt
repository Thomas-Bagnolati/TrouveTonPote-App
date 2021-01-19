package com.yrmt.trouvetonpote.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yrmt.trouvetonpote.R
import com.yrmt.trouvetonpote.model.UserBean
import com.yrmt.trouvetonpote.utils.AuthService
import com.yrmt.trouvetonpote.utils.WsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    // Graphics
    private lateinit var etEmailRegister: TextInputEditText
    private lateinit var tiEmailRegister: TextInputLayout
    private lateinit var etNameRegister: TextInputEditText
    private lateinit var tiNameRegister: TextInputLayout
    private lateinit var etPasswordRegister: TextInputEditText
    private lateinit var tiPasswordRegister: TextInputLayout
    private lateinit var etPasswordConfirmRegister: TextInputEditText
    private lateinit var tiPasswordConfirmRegister: TextInputLayout
    private lateinit var rootView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Graphics
        etEmailRegister = findViewById(R.id.et_email_register)
        tiEmailRegister = findViewById(R.id.ti_email_register)
        etNameRegister = findViewById(R.id.et_name_register)
        tiNameRegister = findViewById(R.id.ti_name_register)
        etPasswordRegister = findViewById(R.id.et_password_register)
        tiPasswordRegister = findViewById(R.id.ti_password_register)
        etPasswordConfirmRegister = findViewById(R.id.et_password_confirm_register)
        tiPasswordConfirmRegister = findViewById(R.id.ti_password_confirm_register)
        rootView = findViewById(R.id.root_register)

    }

    ///////////////////////////////////////////////////////////////////////////
    // ONCLICK
    ///////////////////////////////////////////////////////////////////////////
    fun onClickRegister(view: View) {

        val imm: InputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        // Verify inputTexts
        val isValidForm = verifyErrorTextInput(
                etEmailRegister, etNameRegister, etPasswordRegister, etPasswordConfirmRegister,
                tiEmailRegister, tiNameRegister, tiPasswordRegister, tiPasswordConfirmRegister)

        if (isValidForm) {
            CoroutineScope(IO).launch {
                try {
                    val user = UserBean(
                            etEmailRegister.text.toString(),
                            etNameRegister.text.toString(),
                            etPasswordRegister.text.toString())

                    // Register to DB
                    WsUtils.register(user)

                    val resultIntent = Intent()
                    resultIntent.putExtra("mail", etEmailRegister.text.toString())
                    resultIntent.putExtra("pwd", etPasswordRegister.text.toString())

                    setResult(Activity.RESULT_OK, resultIntent)
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
        withContext(Dispatchers.Main) {
            Snackbar.make(rootView, errorMsg, Snackbar.LENGTH_LONG).show()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // VERIFICATIONS
    ///////////////////////////////////////////////////////////////////////////

    // Verify textInputs
    private fun verifyErrorTextInput(
            etEmail: EditText, etName: EditText, etPassword: EditText, etConfirmPassword: EditText,
            tiEmail: TextInputLayout, tiName: TextInputLayout, tiPassword: TextInputLayout, tiConfirmPassword: TextInputLayout)
            : Boolean {

        //Clean error messages
        tiEmail.error = null
        tiName.error = null
        tiPassword.error = null
        tiConfirmPassword.error = null

        //Case wrong email
        if (!AuthService.isEmailValide(etEmail)) {
            tiEmail.error = getString(R.string.error_mail)
            return false
        }

        //Case wrong name
        else if (!AuthService.isNameValide(etName)) {
            tiName.error = getString(R.string.error_name)
            return false
        }

        //Case Wrong password
        else if (!AuthService.isPasswordValide(etPassword)) {
            tiPassword.error = getString(R.string.error_password)
            return false

        }
        // Case not same passwords
        else if (!AuthService.isSamePassword(etPassword, etConfirmPassword)) {
            tiConfirmPassword.error = getString(R.string.error_same_password)
            return false
        }

        // All right
        else return true
    }

}

