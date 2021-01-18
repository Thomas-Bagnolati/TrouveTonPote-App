package com.yrmt.trouvetonpote.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yrmt.trouvetonpote.R

class ChatFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        // Inflate the layout for this fragment
        val tvTitle = view.findViewById<TextView>(R.id.title_chat)
        return view
    }


}