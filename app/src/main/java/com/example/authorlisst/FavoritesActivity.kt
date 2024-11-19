package com.example.authorlisst

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authorlisst.databinding.ActivityFavoritesBinding
import com.example.authorlist.database.FavoriteRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = FavoriteAdapter(mutableListOf())
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter


        loadFavorites()
    }


    private fun loadFavorites() {
        val favoriteDao = FavoriteRoomDatabase.getInstance(this).favoriteDao()


        lifecycleScope.launch(Dispatchers.IO) {
            val favorites = favoriteDao.getAllFavorites()
            withContext(Dispatchers.Main) {
                Log.d("tag", "favorites: $favorites")
                adapter.updateData(favorites)
            }
        }
    }
}
