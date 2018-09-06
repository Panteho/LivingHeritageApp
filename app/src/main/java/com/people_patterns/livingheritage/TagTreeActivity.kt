package com.people_patterns.livingheritage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog

class TagTreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_tree)
        initViews()
        showAddTreeDialog()
    }

    private fun initViews() {
        findViewById<FloatingActionButton>(R.id.fab_add)
                .setOnClickListener { showAddTreeDialog() }
    }

    private fun showAddTreeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setView(R.layout.layout_tag_tree)
        val dialog = builder.create()
        dialog.show()
    }
}
