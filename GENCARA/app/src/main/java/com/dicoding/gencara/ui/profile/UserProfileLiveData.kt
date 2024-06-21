package com.dicoding.gencara.ui.profile

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth

class UserProfileLiveData : LiveData<UserProfile>() {
    private val auth = FirebaseAuth.getInstance()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userProfile = UserProfile(
                currentUser.displayName,
                currentUser.photoUrl?.toString()
            )
            postValue(userProfile)
        }
    }

    fun updateUserProfile(name: String, photoUrl: String?) {
        val userProfile = UserProfile(name, photoUrl)
        postValue(userProfile)
    }
}

data class UserProfile(val name: String?, val photoUrl: String?)