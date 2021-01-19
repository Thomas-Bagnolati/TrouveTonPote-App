package com.yrmt.trouvetonpote.utils

class TTPConst {

    companion object {

        // URL ROOT
        private const val URL_ROOT_API = "http://172.22.64.1:8080"

        // Auth
        const val URL_API_LOGIN = "$URL_ROOT_API/login"
        const val URL_API_REGISTER = "$URL_ROOT_API/register"
        //User
        const val URL_API_GET_USERS_INFO = "$URL_ROOT_API/getUsersInfo"
        const val URL_API_SEND_USER_INFO = "$URL_ROOT_API/sendUserInfo"
        // Profile
        const val URL_API_GET_USER_PROFILE = "$URL_ROOT_API/getUserProfile"
        const val URL_API_SET_USER_PROFILE = "$URL_ROOT_API/setUserProfil"
    }

}