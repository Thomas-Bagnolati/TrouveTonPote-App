package com.yrmt.trouvetonpote.model

data class UserBean(
        val id: Long?,
        val name: String?,
        val pwd: String?,
        val mail: String?,
        val id_session: Long?,
        val last_con: Long?,
        val status_mess: String?,
        val isShared: Boolean?,
        val lat: Double?,
        val lng: Double?
) {
    constructor(mail: String) : this(null, null, null, mail, null, null, null, null, null, null)
}

data class PasswordUpdateBean(
        val id_session: Long,
        val old_pwd: String,
        val new_pwd: String
)

data class ResponseCodeBean(
        val code: Int,
        val message: String
)