package com.example.authorlisst

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.authorlisst.databinding.ItemFavoriteBinding
import com.example.authorlist.database.Favorite

class FavoriteAdapter(private val favorites: MutableList<Favorite>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = favorites[position]
        holder.binding.apply {
            nameTextView.text = favorite.name
            speciesTextView.text = favorite.species
            genderTextView.text = favorite.gender
            statusTextView.text = favorite.status
        }
    }

    override fun getItemCount(): Int = favorites.size

    fun updateData(newFavorites: List<Favorite>) {
        favorites.clear()
        favorites.addAll(newFavorites)
        notifyDataSetChanged()
    }
}
