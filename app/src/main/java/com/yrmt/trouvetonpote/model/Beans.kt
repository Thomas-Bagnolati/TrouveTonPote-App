package com.yrmt.trouvetonpote.model

data class UserBean(
        val id: Long?,
        var name: String?,
        var pwd: String?,
        val mail: String?,
        val id_session: Long?,
        val last_con: Long?,
        var status_mess: String?,
        var isShared: Int?,
        var lat: Double?,
        var lng: Double?
) {
    constructor(id_session: Long) :
            this(null, null, null, null, id_session, null, null, null, null, null)

    constructor(mail: String, pwd: String) :
            this(null, null, pwd, mail, null, null, null, null, null, null)

    constructor(mail: String, name: String, pwd: String) :
            this(null, name, pwd, mail, null, null, null, null, null, null)

    constructor(id_session: Long, name: String, status_mess: String?) :
            this(null, name, null, null, id_session, null, status_mess, null, null, null)
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