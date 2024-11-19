package com.example.authorlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.authorlisst.databinding.ItemApiBinding
import com.example.authorlist.database.Favorite
import com.example.authorlist.database.FavoriteDao
import com.example.authorlist.model.ApiData
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiAdapter(
    private val apiList: MutableList<ApiData>,
    private val favoriteDao: FavoriteDao
) : RecyclerView.Adapter<ApiAdapter.ApiViewHolder>() {

    inner class ApiViewHolder(val binding: ItemApiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiViewHolder {
        val binding = ItemApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApiViewHolder, position: Int) {
        val apiData = apiList[position]
        holder.binding.apply {
            tvCharName.text = apiData.name
            tvCharStatus.text = apiData.status
            tvCharSpecies.text = apiData.species
            tvCharGender.text = apiData.gender

            Picasso.get()
                .load("https://rickandmortyapi.com/api/character/avatar/${apiData.id}.jpeg")
                .into(imgChar)

            btnFavorites.text =
                if (apiData.isFavorite) "Remove from Favorites" else "Add to Favorites"

            btnFavorites.setOnClickListener {
                apiData.isFavorite = !apiData.isFavorite
                btnFavorites.text =
                    if (apiData.isFavorite) "Remove from Favorites" else "Add to Favorites"

                CoroutineScope(Dispatchers.IO).launch {
                    if (apiData.isFavorite) {
                        favoriteDao.insertFavorite(
                            Favorite(
                                id = apiData.id.toLong(),
                                name = apiData.name,
                                status = apiData.status,
                                species = apiData.species,
                                gender = apiData.gender,
                                isFavorite = true
                            )
                        )
                    } else {
                        favoriteDao.deleteById(apiData.id.toLong())
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = apiList.size

    fun updateApiData(newApiData: List<Any>?) {
        apiList.clear()

        newApiData?.let {
            for (item in it) {
                when (item) {
                    is ApiData -> {
                        apiList.add(item)
                    }
                    is Favorite -> {
                        apiList.add(
                            ApiData(
                                id = item.id.toInt(),
                                name = item.name,
                                status = item.status,
                                species = item.species,
                                gender = item.gender,
                                isFavorite = true // Mark as favorite
                            )
                        )
                    }
                }
            }
        }

        notifyDataSetChanged()
    }
}
