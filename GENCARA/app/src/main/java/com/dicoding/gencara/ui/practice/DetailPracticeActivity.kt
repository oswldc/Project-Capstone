package com.dicoding.gencara.ui.practice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.gencara.R
import com.dicoding.gencara.data.adapter.QuestionAdapter
import com.dicoding.gencara.databinding.ActivityDetailPracticeBinding

class DetailPracticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPracticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuName = intent.getStringExtra("MENU_NAME")
        val menuIcon = intent.getIntExtra("MENU_ICON", -1)
        val questions = resources.getStringArray(R.array.data_question)

        binding.tvMenu.text = menuName
        if (menuIcon != -1) {
            binding.iconMenu.setImageResource(menuIcon)
        }

        // Inisialisasi RecyclerView dan adapter
        binding.rvQuestions.layoutManager = LinearLayoutManager(this)
        binding.rvQuestions.adapter = QuestionAdapter(questions, this) // Menyediakan Context

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
