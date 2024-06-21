package com.dicoding.gencara.ui.dictionary

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.gencara.R
import com.dicoding.gencara.data.adapter.AlphabetAdapter
import com.dicoding.gencara.databinding.ActivityDictionaryBinding

class DictionaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDictionaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val alphabetNames = resources.getStringArray(R.array.data_alphabet_name)

        val typedArray = resources.obtainTypedArray(R.array.data_alphabet)
        val alphabetImages = IntArray(alphabetNames.size)
        for (i in alphabetNames.indices) {
            alphabetImages[i] = typedArray.getResourceId(i, -1)
        }
        typedArray.recycle()

        val adapter = AlphabetAdapter(alphabetNames, alphabetImages) { name, image, position ->
            val intent = Intent(this, DetailDictionaryActivity::class.java)
            intent.putExtra("EXTRA_NAMES", alphabetNames)
            intent.putExtra("EXTRA_IMAGES", alphabetImages)
            intent.putExtra("EXTRA_POSITION", position)
            startActivity(intent)
        }

        binding.rvDictionary.layoutManager = GridLayoutManager(this, 2)
        binding.rvDictionary.adapter = adapter

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        val menuName = intent.getStringExtra("MENU_NAME")
        val menuIcon = intent.getIntExtra("MENU_ICON", -1)

        binding.tvMenu.text = menuName
        binding.iconMenu.setImageResource(menuIcon)
    }
}
