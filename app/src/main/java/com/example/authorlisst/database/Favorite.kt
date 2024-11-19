package com.example.authorlist.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val isFavorite: Boolean
)
