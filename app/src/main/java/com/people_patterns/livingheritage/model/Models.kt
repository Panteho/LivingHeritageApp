package com.people_patterns.livingheritage.model

data class User(val name: String,
                val email: String,
                val phone: String,
                val id: String)

data class Tree(val name: String,
                val imagePath: String,
                val description: String,
                val taggerId: String,
                val guardianId: String,
                val lat: Long,
                val long: Long)