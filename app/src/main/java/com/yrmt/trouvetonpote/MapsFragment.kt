package com.yrmt.trouvetonpote

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.button.MaterialButton
import com.yrmt.trouvetonpote.controller.user
import com.yrmt.trouvetonpote.model.UserBean
import com.yrmt.trouvetonpote.utils.WsUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class MapsFragment : Fragment(), GoogleMap.InfoWindowAdapter {

    lateinit var map:GoogleMap
    lateinit var btnShowMe: MaterialButton
    private val listUserBean = mutableListOf<UserBean>()

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        //ask for permission
        if (ContextCompat.checkSelfPermission(requireContext().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)

        map.setInfoWindowAdapter(this)

        //show me btn callback
        btnShowMe.setOnClickListener {
            refreshMap()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mapFragmentView = inflater.inflate(R.layout.fragment_maps, container, false)
        btnShowMe = mapFragmentView.findViewById(R.id.btn_show_me)
        return mapFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        refreshDataLoop()
    }

    //call data on map launch init every 30 secs
    fun refreshDataLoop() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                getUsersInfo()
                handler.postDelayed(this, 15_000)//1 sec delay
            }
        }, 0)
    }

    //last permission callback
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        refreshMap()
    }

    private fun refreshMap() {
        activity?.runOnUiThread {
            if (ContextCompat.checkSelfPermission(requireContext().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
                map.clear()

                //var to center camera on all users
                val latLngBounds = LatLngBounds.Builder()

                //if data, for each user, add a marker
                if(!listUserBean.isNullOrEmpty()) {
                    listUserBean.forEach {
                        val position = LatLng(it.lat?:0.0, it.lng?:0.0)
                        map.addMarker(MarkerOptions().position(position).title(it.name?:"Unknown").icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).tag = it
                        latLngBounds.include(position)
                    }
                }
                //check bonds had at least 2 item
//                if (listUserBean.size > 1) {
//                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 250))
//                }
            }
        }
    }

    fun getUsersInfo() {
        CoroutineScope(IO).launch {
            try {
                //clean list and call ws method to refill it
                listUserBean.clear()
                listUserBean.addAll(WsUtils.getUsersInfo(user))
                refreshMap()
            } catch (e: Exception) {
                e.printStackTrace()
                CoroutineScope(Main).launch {
                    Toast.makeText(requireContext(), e.message ?: "Une erreur est survenue", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

    override fun getInfoContents(p0: Marker): View {
        val view = LayoutInflater.from(this.requireContext()).inflate(R.layout.marker_user, null)
        val tvName = view.findViewById<TextView>(R.id.tv_marker_name)
        val tvMessage = view.findViewById<TextView>(R.id.tv_marker_message)
        val user = p0.tag as UserBean
        tvMessage.text = user.status_mess?: ""
        tvName.text = user.name?: "Unknown"

        return view
    }


}