package com.yrmt.trouvetonpote.controller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yrmt.trouvetonpote.R
import com.yrmt.trouvetonpote.utils.WsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfilFragment : Fragment() {

    lateinit var btnEditPwd: Button
    lateinit var tvName: TextView
    lateinit var imgProfile: ImageView
    lateinit var etStatus: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        // Graphics
        btnEditPwd = view.findViewById(R.id.btn_edit_pwd)
        tvName = view.findViewById(R.id.tv_name)
        imgProfile = view.findViewById(R.id.iv_profile)
        etStatus = view.findViewById(R.id.et_status_msg)

        // Onclick Btn EditPassword
        btnEditPwd.setOnClickListener {
            startActivity(Intent(context, EditPasswordActivity::class.java))
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        updateProfileInfo()
    }


    private suspend fun updateUI() {
        withContext(Main) {
            if (!user.name.isNullOrBlank()) tvName.text = user.name
            if (!user.status_mess.isNullOrBlank()) {
                etStatus.text.clear()
                etStatus.append(user.status_mess)
            }
        }
    }

    private fun updateProfileInfo() {
        CoroutineScope(IO).launch {
            try {
                val res = WsUtils.getUserProfile(user)
                user.name = res.name
                user.status_mess = res.status_mess
                updateUI()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}