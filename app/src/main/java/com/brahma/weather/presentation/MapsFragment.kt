package com.brahma.weather.presentation

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.brahma.weather.R
import com.brahma.weather.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    LocationListener {

    private lateinit var lastLocation: Location
    private lateinit var latestLatLang: LatLng
    private lateinit var gMap: GoogleMap
    private lateinit var currentLocationUserMarker: Marker
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentMapsBinding
    private lateinit var sp: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapsBinding.bind(view)
        binding.root

        sp = this.requireActivity()
            .getSharedPreferences("pref", Context.MODE_PRIVATE)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.saveLocation.setOnClickListener {
            val spEdit = sp.edit()
            spEdit.putString("latitude", latestLatLang.latitude.toString())
            spEdit.putString("longitude", latestLatLang.longitude.toString())
            spEdit.apply()

            view?.let {
                Navigation.findNavController(it)
                    .navigate(R.id.action_map_frag_to_weather_home_fragment)
            }
        }


    }

    private fun getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        gMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {
                lastLocation = location
                latestLatLang = LatLng(location.latitude, location.longitude)
                val currentLatLng = LatLng(location.latitude, location.longitude)
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                placeMarkerOnTheMap(currentLatLng)
            }

        }
    }

    private fun placeMarkerOnTheMap(currentLatLng: LatLng) {
        gMap.clear()
        latestLatLang = LatLng(currentLatLng.latitude, currentLatLng.longitude)
        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.anchor(0.5f, 0.5f)
        markerOptions.title("$currentLatLng")
        markerOptions.snippet("Drag me to change Location")
        currentLocationUserMarker = gMap.addMarker(markerOptions)!!
        currentLocationUserMarker.isDraggable = true
        currentLocationUserMarker.showInfoWindow()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Please grant Location permissions", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onMarkerClick(p0: Marker) = false

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        getCurrentLocation()

        gMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {

            override fun onMarkerDragEnd(marker: Marker) {
                val latLng: LatLng = marker.position
                placeMarkerOnTheMap(latLng)
            }

            override fun onMarkerDragStart(marker: Marker) {

            }

            override fun onMarkerDrag(marker: Marker) {

            }

        })

        gMap.setOnMyLocationButtonClickListener {
            val l: Location = gMap.myLocation
            placeMarkerOnTheMap(LatLng(l.latitude, l.longitude))
            false
        }

        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.uiSettings.isMyLocationButtonEnabled = true
        gMap.uiSettings.isZoomGesturesEnabled = true
        gMap.uiSettings.isRotateGesturesEnabled = true
        gMap.uiSettings.isCompassEnabled = true
    }

    override fun onLocationChanged(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        placeMarkerOnTheMap(latLng)
        currentLocationUserMarker.remove()

    }

}