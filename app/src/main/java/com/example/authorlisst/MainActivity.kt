package com.example.authorlisst

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authorlisst.databinding.ActivityMainBinding
import com.example.authorlisst.model.ApiData
import com.example.authorlisst.network.ApiClient
import com.example.authorlisst.network.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // View Binding
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Inisialisasi API Client dan Adapter
    private val client = ApiClient.getInstance()
    private lateinit var adapter: ApiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set content view
        setContentView(binding.root)

        // Enable edge-to-edge UI (setelah setContentView)
        enableEdgeToEdge()

        // Apply padding for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up RecyclerView and Adapter
        binding.rvMorty.layoutManager = LinearLayoutManager(this) // Pastikan LayoutManager sudah benar
        adapter = ApiAdapter(listOf()) // Adapter dimulai dengan daftar kosong
        binding.rvMorty.adapter = adapter

        // Fetch data dari API
        fetchData()
    }

    private fun fetchData() {
        // Memanggil API untuk mengambil data
        val response = client.getApiData()
        response.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                // Memastikan response berhasil dan body tidak null
                val characterList = response.body()?.results
                if (characterList != null && characterList.isNotEmpty()) {
                    // Update data pada adapter
                    adapter.updateApiData(characterList)
                } else {
                    Toast.makeText(this@MainActivity, "Tidak ada data yang tersedia", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // Menampilkan error jika gagal fetch data
                Toast.makeText(this@MainActivity, "Koneksi Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
