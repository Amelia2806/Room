package com.example.authorlisst

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authorlisst.databinding.ActivityMainBinding
import com.example.authorlist.adapter.ApiAdapter
import com.example.authorlist.database.FavoriteDao
import com.example.authorlist.database.FavoriteRoomDatabase
import com.example.authorlist.network.ApiClient
import com.example.authorlist.network.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ApiAdapter
    private lateinit var favoriteDao: FavoriteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize favoriteDao to access the Room database
        favoriteDao = FavoriteRoomDatabase.getInstance(this).favoriteDao()

        // Initialize adapter for RecyclerView
        adapter = ApiAdapter(mutableListOf(), favoriteDao)

        // Setup RecyclerView
        binding.rvMorty.layoutManager = LinearLayoutManager(this)
        binding.rvMorty.adapter = adapter

        // Fetch data from API and database (favorites)
        fetchData()
        fetchFavorites()
    }

    // Fetch character data from API
    private fun fetchData() {
        val apiClient = ApiClient.apiService
        val call = apiClient.getCharacters()

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val characterList = response.body()?.results
                    if (!characterList.isNullOrEmpty()) {
                        adapter.updateApiData(characterList)
                    } else {
                        Toast.makeText(this@MainActivity, "No data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch data: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Connection error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fetch favorite data from Room and update RecyclerView
    private fun fetchFavorites() {
        // Run Room query in background thread
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteList = favoriteDao.getAllFavorites()

            // Update UI on main thread after fetching data from database
            withContext(Dispatchers.Main) {
                if (favoriteList.isNotEmpty()) {
                    adapter.updateApiData(favoriteList)
                } else {
                    Toast.makeText(this@MainActivity, "No favorites found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.buttom_navigation_menu, menu) // Make sure menu file name is correct
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnFavorites -> {
                val intent = Intent(this, FavoritesActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
