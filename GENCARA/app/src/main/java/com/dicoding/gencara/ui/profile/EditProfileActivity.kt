package com.dicoding.gencara.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.gencara.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var imageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            Glide.with(this).load(imageUri).into(binding.ivProfile)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        binding.editName.setText(user.displayName)
        binding.editEmail.setText(user.email)
        Glide.with(this).load(user.photoUrl).into(binding.ivProfile)

        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.tvSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = binding.editName.text.toString()
        if (name.isNotEmpty()) {
            val profileUpdates = userProfileChangeRequest {
                displayName = name
                photoUri = imageUri
            }

            user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageUri?.let { uri ->
                        val storageRef = FirebaseStorage.getInstance().reference
                        val profileRef = storageRef.child("profileImages/${user.uid}.jpg")
                        profileRef.putFile(uri).addOnCompleteListener { uploadTask ->
                            if (uploadTask.isSuccessful) {
                                profileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                    user.updateProfile(
                                        userProfileChangeRequest { photoUri = downloadUri }
                                    ).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val userProfileLiveData = UserProfileLiveData()
                                            userProfileLiveData.updateUserProfile(
                                                name,
                                                downloadUri.toString()
                                            )

                                            Toast.makeText(
                                                this,
                                                "Profile updated",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            setResult(Activity.RESULT_OK)
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Profile update failed",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Profile image upload failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } ?: run {
                        val userProfileLiveData = UserProfileLiveData()
                        userProfileLiveData.updateUserProfile(name, null)

                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Profile update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}