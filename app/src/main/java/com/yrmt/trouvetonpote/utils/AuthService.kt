package com.yrmt.trouvetonpote.utils

import android.util.Patterns
import android.widget.EditText
import java.util.regex.Pattern

class AuthService {

    companion object {

        //Function to verify valid username
        fun isUsernameValide(etUsername: EditText?): Boolean {

            val strUsername = etUsername?.text.toString().trim()

            return (strUsername.length in 3..25)
        }

        //Function to verify valid username
        fun isEmailValide(etEmail: EditText?): Boolean {

            val strEmail = etEmail?.text.toString().trim()

            return (strEmail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
        }

        //Function to verify valid password
        fun isPasswordValide(etPassword: EditText?): Boolean {

            val strPassword = etPassword?.text.toString()

            return isValidPasswordFormat(strPassword)
        }

        // Fun regex Password
        private fun isValidPasswordFormat(password: String): Boolean {
            val passwordREGEX = Pattern.compile(
                    "^" +
                            "(?=.*[0-9])" +         //at least 1 digit
                            "(?=.*[a-z])" +         //at least 1 lower case letter
                            "(?=.*[A-Z])" +         //at least 1 upper case letter
                            "(?=.*[a-zA-Z])" +      //any letter
                            "(?=.*[@#$%^&+=!?])" +    //at least 1 special character
                            "(?=\\S+$)" +           //no white spaces
                            ".{8,}" +               //at least 8 characters
                            "$"
            )
            return passwordREGEX.matcher(password).matches()
        }

    }

}