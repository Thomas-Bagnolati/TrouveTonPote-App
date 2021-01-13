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
    constructor(id_session: Long) :
            this(null, null, null, null, id_session, null, null, null, null, null)

    constructor(mail: String, pwd: String) :
            this(null, null, pwd, mail, null, null, null, null, null, null)

    constructor(mail: String, name: String, pwd: String) :
            this(null, name, pwd, mail, null, null, null, null, null, null)
}

data class PasswordUpdateBean(
        val id_session: Long,
        val old_pwd: String,
        val new_pwd: String
)

data class ResponseCodeBean<T> (
        val code: Int,
        val message: String?,
        val data : T?
)