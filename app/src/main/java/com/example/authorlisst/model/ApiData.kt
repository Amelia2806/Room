package com.example.authorlist.model

import java.io.Serializable

data class ApiData(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    var isFavorite: Boolean = false
) : Serializable
