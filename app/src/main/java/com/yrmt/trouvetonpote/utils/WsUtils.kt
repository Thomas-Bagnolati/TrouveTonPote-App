package com.yrmt.trouvetonpote.utils


import android.content.ClipData
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yrmt.trouvetonpote.model.ResponseCodeBean
import com.yrmt.trouvetonpote.model.UserBean


class WsUtils {

    companion object {

        private const val TAG = "WSUtils"
        private val gson = Gson()
        var json = ""

        ///////////////////////////////////////////////////////////////////////////
        // REQUEST AUTHENTIFICATION
        ///////////////////////////////////////////////////////////////////////////

        fun login(mail: String, pwd: String): ResponseCodeBean<UserBean> {

            val user = UserBean(mail, pwd)
            val json = gson.toJson(user)

            val res = sendPostOkHttpRequest(TTPConst.URL_API_LOGIN, json)

            val itemType = object : TypeToken<ResponseCodeBean<UserBean>>() {}.type
            val responseCode = gson.fromJson<ResponseCodeBean<UserBean>>(res, itemType)

            Log.d(TAG, "login -> responseCode = $responseCode")

            return responseCode
        }

    }


}