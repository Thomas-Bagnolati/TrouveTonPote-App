package com.yrmt.trouvetonpote.controller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.yrmt.trouvetonpote.R
import com.yrmt.trouvetonpote.model.UserBean
import com.yrmt.trouvetonpote.utils.AuthService
import com.yrmt.trouvetonpote.utils.WsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfilFragment : Fragment() {

    private lateinit var btnEditPwd: Button
    private lateinit var btnEditName: Button
    private lateinit var btnConfirmStatus: Button

    lateinit var tvName: TextView
    lateinit var imgProfile: ImageView
    lateinit var etStatus: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        // Graphics
        btnEditPwd = view.findViewById(R.id.btn_edit_pwd)
        btnEditName = view.findViewById(R.id.btn_edit_name)
        btnConfirmStatus = view.findViewById(R.id.btn_confirm_status)
        tvName = view.findViewById(R.id.tv_name)
        imgProfile = view.findViewById(R.id.iv_profile)
        etStatus = view.findViewById(R.id.et_status_msg)

        // Onclick Btn EditPassword
        btnEditPwd.setOnClickListener {
            startActivity(Intent(context, EditPasswordActivity::class.java))
        }
        // Onclick Btn EditName
        btnEditName.setOnClickListener {
            showAlertDialogEditName()
        }
        // Onclick Btn ConfirmStatus
        btnConfirmStatus.setOnClickListener {

            //Oblige a entrer un nom
            if (user.name == null) {
                Toast.makeText(context, "Il te faut un nom", Toast.LENGTH_SHORT).show()
            } else {
                updateProfile()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        getProfileDB()
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

    private fun getProfileDB() {
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

    private fun updateProfile() {

        if (!isUpdatableProfile()) Toast.makeText(context, "Tes informations sont déjà à jours", Toast.LENGTH_SHORT).show()
        else {
            CoroutineScope(IO).launch {
                try {
                    val tmpUser = UserBean(user.id_session!!, user.name!!, user.status_mess)
                    WsUtils.setUserProfile(tmpUser)
                    CoroutineScope(Main).launch { Toast.makeText(context, "Tes informations ont été mise à jours", Toast.LENGTH_SHORT).show() }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // Verify if values are different & Update our variable user with infos on graphic
    private fun isUpdatableProfile(): Boolean {
        val name = tvName.text.toString()
        val statusMsg = etStatus.text.toString()

        if (name != user.name || statusMsg != user.status_mess) {
            user.name = name
            user.status_mess = statusMsg
            return true
        }
        return false
    }


    private fun showAlertDialogEditName() {

        val layout = layoutInflater.inflate(R.layout.dialog_edit_name, null)
        val etName = layout.findViewById<TextInputEditText>(R.id.et_dialog_name)

        MaterialAlertDialogBuilder(requireContext())
                .setTitle("Change ton nom")
                .setView(layout)
                .setNegativeButton("Annuler") { dialog, which ->

                }
                .setPositiveButton("Valider") { dialog, which ->
                    if (AuthService.isNameValide(etName)) {
                        tvName.text = etName.text.toString()
                    } else {
                        Toast.makeText(context, "Ton nom n'es pas valide", Toast.LENGTH_SHORT).show()
                    }
                }
                .show()
    }


}