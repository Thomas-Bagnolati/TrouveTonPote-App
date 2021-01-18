package com.yrmt.trouvetonpote.controller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.yrmt.trouvetonpote.R

class ProfilFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        val btnEditPwd = view.findViewById<Button>(R.id.btn_edit_pwd)

        // Onclick Btn EditPassword
        btnEditPwd.setOnClickListener {
            startActivity(Intent(context, EditPasswordActivity::class.java))
        }

        return view
    }

}