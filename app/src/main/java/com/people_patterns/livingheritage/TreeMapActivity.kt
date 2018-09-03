package com.people_patterns.livingheritage

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.people_patterns.livingheritage.base.BaseActivity

class TreeMapActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        addMarkerToMap(sydney, "Sydney")
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    fun addMarkerToMap(latlog: LatLng, title: String) {
        val marker = mMap.addMarker(MarkerOptions().position(latlog).title(title))
        marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_treetag_01));
    }

    fun gotoSignUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }



}
