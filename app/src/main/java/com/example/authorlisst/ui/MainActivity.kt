package com.example.authorlist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authorlisst.databinding.ActivityMainBinding
import com.example.authorlisst.network.ApiClient
import com.example.authorlist.adapter.ApiAdapter
import com.example.authorlist.database.Favorite
import com.example.authorlist.database.FavoriteRoomDatabase
import com.example.authorlist.model.ApiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var apiAdapter: ApiAdapter
    private val favoriteDao by lazy { FavoriteRoomDatabase.getInstance(this).favoriteDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi adapter
        apiAdapter = ApiAdapter(mutableListOf(), favoriteDao) { apiData -> toggleFavorite(apiData) }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = apiAdapter

        // Muat data API
        getChartData()

        // Navigasi ke FavoritesActivity
        binding.favoriteIconButton.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    private fun getChartData() {
        val call = ApiClient.getInstance().getCharacters()
        call.enqueue(object : Callback<ApiData> {
            override fun onResponse(call: Call<ApiData>, response: Response<ApiData>) {
                if (response.isSuccessful) {
                    val apiCharacters = response.body()?.results ?: emptyList()

                    lifecycleScope.launch(Dispatchers.IO) {
                        val favoriteMap = favoriteDao.getFavoriteCharacters().associateBy { it.id }
                        val characters = apiCharacters.map { character ->
                            character.isFavorite = favoriteMap[character.id]?.isFavorite ?: false
                            character
                        }

                        withContext(Dispatchers.Main) {
                            apiAdapter.updateData(characters)
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiData>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toggleFavorite(apiData: ApiData) {
        apiData.isFavorite = !apiData.isFavorite

        lifecycleScope.launch(Dispatchers.IO) {
            if (apiData.isFavorite) {
                favoriteDao.insert(Favorite(apiData.id, apiData.name, apiData.status, apiData.species, apiData.gender, true))
            } else {
                favoriteDao.removeFromFavorites(apiData.id.toLong())
            }

            withContext(Dispatchers.Main) {
                apiAdapter.notifyDataSetChanged()
                Toast.makeText(this@MainActivity, if (apiData.isFavorite) "Added to favorites" else "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }
}