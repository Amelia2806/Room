package com.example.authorlisst

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.example.authorlisst.model.ApiData
import com.example.authorlisst.R
import com.example.authorlisst.databinding.ItemApiBinding

class ApiAdapter(private var apiList: List<ApiData>) : RecyclerView.Adapter<ApiAdapter.ApiViewHolder>() {

    inner class ApiViewHolder(val binding: ItemApiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiViewHolder {
        val binding = ItemApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApiViewHolder, position: Int) {
        val apiData = apiList[position]
        holder.binding.tvCharName.text = apiData.name
        holder.binding.tvCharStatus.text = apiData.status
        holder.binding.tvCharSpecies.text = apiData.species
        holder.binding.tvCharGender.text = apiData.gender

        Picasso.get()
            .load("https://rickandmortyapi.com/api/character/avatar/${apiData.id}.jpeg")
            .into(holder.binding.imgChar)

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${apiData.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return apiList.size
    }

    // Fungsi untuk memperbarui data adapter
    fun updateApiData(newApiData: List<ApiData>) {
        apiList = newApiData
        notifyDataSetChanged()
    }
}
