package com.people_patterns.livingheritage

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.people_patterns.livingheritage.base.BaseActivity
import com.people_patterns.livingheritage.model.Tree
import com.squareup.picasso.Picasso


class TreeMapActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var ivTree: ImageView
    private lateinit var txtTitle: TextView
    private lateinit var txtDesc: TextView
    private lateinit var txtTagger: TextView
    private lateinit var txtGuardian: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener{
            val intent = Intent(this, TagTreeActivity::class.java)
            startActivity(intent)
        }
        iniViews();
        mapFragment.getMapAsync(this)
    }

    private fun iniViews() {
        txtTitle = findViewById(R.id.txt_title)
        txtDesc = findViewById(R.id.txt_tree_desc)
        txtTagger = findViewById(R.id.txt_tager)
        txtGuardian = findViewById(R.id.txt_guardian)
        ivTree = findViewById(R.id.iv_tree_photo)
        constraintLayout = findViewById(R.id.container)
        BottomSheetBehavior.from(constraintLayout).state = BottomSheetBehavior.STATE_HIDDEN
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
        val sydney = LatLng(15.508665, 73.834478)
        mMap.setOnMarkerClickListener {
            markerClicked(it.tag!!)
        }
        mMap.setOnMapClickListener {
            addTreeAt(it)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f))
        if(getbaseApp().location != null) {
            val latlng = LatLng(getbaseApp().getLocation().latitude, getbaseApp().getLocation().longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
        }
        showAllTrees()
    }

    private fun addTreeAt(latlng: LatLng) {
        val intent = Intent(this, TagTreeActivity::class.java)
        intent.putExtra("latitude", latlng.latitude)
        intent.putExtra("longitude", latlng.longitude)
        startActivity(intent)
    }

    private fun markerClicked(tag: Any): Boolean {
        if (tag == null) {
            return true;
        }
        if(BottomSheetBehavior.from(constraintLayout).state == BottomSheetBehavior.STATE_HIDDEN) {
            BottomSheetBehavior.from(constraintLayout).state = BottomSheetBehavior.STATE_EXPANDED
        }
        var tree = tag as Tree;
        txtTitle.text = tree.name
        txtDesc.text = tree.description
        txtTagger.text = tree.taggerId
        txtGuardian.text = tree.guardianId
        getImageUrl(tree.imagePath, {
            if (it.exception == null) {
                ivTree.visibility = View.VISIBLE
                Picasso.get().load(it.result).placeholder(R.drawable.ic_loading).into(ivTree)
            }
            else {
                ivTree.visibility = View.GONE
            }
        })
        return true;
    }

    private fun showAllTrees() {
        mMap.clear()
        getAllTrees {
            it.forEach { addMarkerToMap(LatLng(it.lat, it.long), it.name, it) }
        }
    }


    fun addMarkerToMap(latlog: LatLng, title: String, tree: Tree) {
        val marker = mMap.addMarker(MarkerOptions().position(latlog).title(title))
        marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_treetag_01));
        marker.tag = tree;
    }

    fun gotoSignUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

}
