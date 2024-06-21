package com.dicoding.gencara.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.gencara.databinding.ItemDictionaryBinding

class AlphabetAdapter(
    private val alphabetNames: Array<String>,
    private val alphabetImages: IntArray,
    private val onItemClick: (String, Int, Int) -> Unit
) : RecyclerView.Adapter<AlphabetAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDictionaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, imageResId: Int, position: Int) {
            binding.tvMenu.text = name
            binding.iconDictionary.setImageResource(imageResId)
            binding.root.setOnClickListener {
                onItemClick(name, imageResId, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDictionaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(alphabetNames[position], alphabetImages[position], position)
    }

    override fun getItemCount(): Int {
        return alphabetNames.size
    }
}
