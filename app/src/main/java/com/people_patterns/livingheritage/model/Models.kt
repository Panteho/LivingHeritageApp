package com.people_patterns.livingheritage.model

data class User(val name: String,
                val email: String,
                val phone: String,
                val id: String)

data class Tree(val name: String,
                var imagePath: String,
                val description: String,
                val taggerId: String,
                val guardianId: String,
                val lat: Double,
                val long: Double) {
    constructor() : this("", "", "", "", "", 0.0, 0.0)
}