package com.dicoding.gencara.data.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.gencara.databinding.ItemQuestionBinding
import com.dicoding.gencara.ui.practice.SignPracticeActivity

class QuestionAdapter(
    private val questions: Array<String>,
    private val context: Context
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: String) {
            binding.btnQuestion.text = question
            binding.btnQuestion.setOnClickListener {
                val intent = Intent(context, SignPracticeActivity::class.java).apply {
                    putExtra("QUESTION", question) // Mengirim data pertanyaan jika diperlukan
                }
                context.startActivity(intent)
            }
        }
    }
}
