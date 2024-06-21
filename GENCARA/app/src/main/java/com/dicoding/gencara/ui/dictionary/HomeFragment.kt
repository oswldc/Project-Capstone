package com.dicoding.gencara.ui.dictionary

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.dicoding.gencara.R
import com.dicoding.gencara.data.adapter.MenuAdapter
import com.dicoding.gencara.databinding.FragmentHomeBinding
import com.dicoding.gencara.ui.profile.UserProfileLiveData
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val userProfileLiveData = UserProfileLiveData()
        userProfileLiveData.observe(viewLifecycleOwner) { userProfile ->
            binding.tvUserName.text = userProfile.name
            if (userProfile.photoUrl != null) {
                Glide.with(requireContext())
                    .load(userProfile.photoUrl)
                    .into(binding.ivProfile)
            } else {
                binding.ivProfile.setImageResource(R.drawable.profile_placeholder)
            }
        }
        // Mendapatkan instance Firebase Authentication
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            // Pengguna sudah login
            val name = sharedPreferences.getString("user_name", currentUser.displayName)
            val photoUrl = sharedPreferences.getString("profile_photo_url", null)

            // Mengatur nama pengguna
            binding.tvUserName.text = name

            // Mengatur foto profil
            if (photoUrl != null) {
                Glide.with(requireContext())
                    .load(photoUrl)
                    .into(binding.ivProfile)
            } else {
                // Jika tidak ada foto profil, tampilkan gambar default
                binding.ivProfile.setImageResource(R.drawable.profile_placeholder)
            }
        } else {
            // Pengguna belum login
            // Lakukan tindakan yang sesuai, misalnya mengarahkan ke halaman login
        }

        val menu = resources.getStringArray(R.array.data_menu)
        val icon = resources.obtainTypedArray(R.array.data_icon).run {
            val iconIds = IntArray(length()) { getResourceId(it, -1) }
            recycle()
            iconIds
        }

        binding.rvMenu.layoutManager = GridLayoutManager(context, 3)
        adapter = MenuAdapter(menu, icon) { menuItem, iconId, position ->
            if (position == 0) {
                val intent = Intent(context, DictionaryActivity::class.java).apply {
                    putExtra("MENU_NAME", menuItem)
                    putExtra("MENU_ICON", iconId)
                }
                startActivity(intent)
            } else {
                showAlertDialog(menuItem)
            }
        }
        binding.rvMenu.adapter = adapter
    }

    private fun showAlertDialog(menuItem: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Oops!")
            .setMessage("Menu $menuItem belum bisa kamu buka saat ini!")
            .setPositiveButton("Kembali") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}