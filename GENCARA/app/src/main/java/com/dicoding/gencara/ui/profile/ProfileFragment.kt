package com.dicoding.gencara.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.gencara.R
import com.dicoding.gencara.databinding.FragmentProfileBinding
import com.dicoding.gencara.ui.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@Suppress("DEPRECATION")
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        Log.d("ProfileFragment", "onCreateView called")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ProfileFragment", "onViewCreated called")

        sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
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

        binding.tvEditProfile.setOnClickListener {
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnLanguage.setOnClickListener {
            Log.d("ProfileFragment", "Language button clicked")
            showLanguageDialog()
        }

        binding.btnContact.setOnClickListener {
            sendEmail()
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun showLanguageDialog() {
        Log.d("ProfileFragment", "Opening language dialog")
        val languages = arrayOf("English (USA)", "Bahasa Indonesia")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Language")
        builder.setSingleChoiceItems(languages, -1) { dialog, which ->
            Log.d("ProfileFragment", "Language selected: $which")
            when (which) {
                0 -> setLocale("en")
                1 -> setLocale("id")
            }
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
        Log.d("ProfileFragment", "Language dialog should be shown")
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.locale = locale
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
        editor.putString("Language", lang)
        editor.apply()

        requireActivity().recreate()
    }

    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "message/rfc822"
        emailIntent.setPackage("com.google.android.gm")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("gencaraapp@gmail.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Message")

        try {
            startActivity(emailIntent)
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Gmail app is not installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(requireContext(), "Successfully logged out.", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), WelcomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}