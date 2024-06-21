package com.dicoding.gencara.ui.practice

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.gencara.R
import com.dicoding.gencara.data.adapter.MenuAdapter
import com.dicoding.gencara.databinding.FragmentPracticeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PracticeFragment : Fragment() {

    private var _binding: FragmentPracticeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPracticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menu = resources.getStringArray(R.array.data_menu)
        val icon = resources.obtainTypedArray(R.array.data_icon).run {
            val iconIds = IntArray(length()) { getResourceId(it, -1) }
            recycle()
            iconIds
        }

        binding.rvMenu.layoutManager = GridLayoutManager(context, 3)
        adapter = MenuAdapter(menu, icon) { menuItem, iconId, position ->
            if (position == 0) {
                val intent = Intent(context, DetailPracticeActivity::class.java).apply {
                    putExtra("MENU_NAME", menuItem)
                    putExtra("MENU_ICON", iconId)
                }
                startActivity(intent)
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Oops!")
                    .setMessage("Kamu belum bisa berlatih bahasa isyarat $menuItem sekarang. Latihan Alfabet dulu, yuk!")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
        binding.rvMenu.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
