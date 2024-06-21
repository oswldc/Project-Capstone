package com.dicoding.gencara.ui.dictionary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.dicoding.gencara.R
import com.dicoding.gencara.databinding.ActivityDetailDictionaryBinding

class DetailDictionaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDictionaryBinding
    private lateinit var alphabetNames: Array<String>
    private lateinit var alphabetImages: IntArray
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        alphabetNames = intent.getStringArrayExtra("EXTRA_NAMES") ?: emptyArray()
        alphabetImages = intent.getIntArrayExtra("EXTRA_IMAGES") ?: IntArray(0)
        currentPosition = intent.getIntExtra("EXTRA_POSITION", 0)

        updateContent()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnPrev.setOnClickListener {
            if (currentPosition > 0) {
                currentPosition--
                updateContent()
            }
        }

        binding.btnNext.setOnClickListener {
            if (currentPosition < alphabetNames.size - 1) {
                currentPosition++
                updateContent()
            }
        }
    }

    private fun updateContent() {
        val name = alphabetNames[currentPosition]
        val image = alphabetImages[currentPosition]

        binding.tvDictionary.text = name
        binding.ivDictionary.setImageResource(image)

        binding.btnPrev.visibility = if (currentPosition == 0) android.view.View.GONE else android.view.View.VISIBLE
        binding.btnNext.visibility = if (currentPosition == alphabetNames.size - 1) android.view.View.GONE else android.view.View.VISIBLE
    }
}
