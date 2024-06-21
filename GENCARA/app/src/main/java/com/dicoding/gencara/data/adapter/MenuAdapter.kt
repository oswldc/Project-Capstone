package com.dicoding.gencara.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.gencara.databinding.ItemMenuBinding

class MenuAdapter(
    private val menu: Array<String>,
    private val icons: IntArray,
    private val onItemClick: (String, Int, Int) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menu[position], icons[position], position)
    }

    override fun getItemCount(): Int = menu.size

    inner class MenuViewHolder(private val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(menuItem: String, iconId: Int, position: Int) {
            binding.tvMenu.text = menuItem
            binding.ivIcon.setImageResource(iconId)
            itemView.setOnClickListener {
                onItemClick(menuItem, iconId, position)
            }
        }
    }
}
