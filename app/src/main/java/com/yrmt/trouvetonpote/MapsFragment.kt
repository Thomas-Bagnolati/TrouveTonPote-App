package com.yrmt.trouvetonpote

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.thread

class MapsFragment : Fragment(), GoogleMap.InfoWindowAdapter {

    private lateinit var map: GoogleMap
    private lateinit var btnShowMe: MaterialButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val listUserBean = mutableListOf<UserBean>()
    private lateinit var timer: Timer
    private var isZoomed = false

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        //ask for permission
        if (ContextCompat.checkSelfPermission(requireContext().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)

        map.setInfoWindowAdapter(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        //show me btn callback
        btnShowMe.setOnClickListener {
            user.isShared = 1
            sendUserInfo()
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
    }

    //launch timer
    override fun onStart() {
        super.onStart()
        timer = Timer()
        timer.schedule(0, 15_000) {
            print("")
            getUsersInfo()
        }
    }

    //close timer
    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    //last permission callback
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        refreshMap()
    }

    private fun refreshMap() {
        activity?.runOnUiThread {
            map.clear()
            if (ContextCompat.checkSelfPermission(requireContext().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true

                //var to center camera on all users
                val latLngBounds = LatLngBounds.Builder()

                //if data, for each user, add a marker
                if (!listUserBean.isNullOrEmpty()) {
                    listUserBean.forEach {
                        val position = LatLng(it.lat ?: 0.0, it.lng ?: 0.0)
                        //add marker only if user has position
                        if (it.lat != 0.0 || it.lng != 0.0) {
                            map.addMarker(MarkerOptions().position(position).title(it.name ?: "Unknown").icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).tag = it
                            latLngBounds.include(position)
                        }
                    }
                }
                // check bonds had at least 2 item
                if (listUserBean.size > 1 && !isZoomed) {
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 250))
                    isZoomed = true
                }
            }
        }
    }

    private fun getUsersInfo() {
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

    fun sendUserInfo() {
        CoroutineScope(IO).launch {
            try {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.lastLocation.addOnSuccessListener {
                        user.lat = it.latitude
                        user.lng = it.longitude
                        thread {
                            WsUtils.sendUserInfo(user)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
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
        if (user.status_mess.isNullOrBlank()) {
            tvMessage.text = "Occupé"
        } else {
            tvMessage.text = user.status_mess
        }
        tvName.text = user.name ?: "Unknown"

        return view
    }
}