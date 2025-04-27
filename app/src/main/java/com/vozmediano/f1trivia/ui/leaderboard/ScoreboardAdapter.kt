package com.vozmediano.f1trivia.ui.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vozmediano.f1trivia.databinding.ItemScoreBinding
import com.vozmediano.f1trivia.domain.model.quiz.ScoreboardEntry

class ScoreboardAdapter(private val scores: List<ScoreboardEntry>) :
    RecyclerView.Adapter<ScoreboardAdapter.ScoreViewHolder>() {

    inner class ScoreViewHolder(private val binding: ItemScoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: ScoreboardEntry, position: Int) {
            binding.tvRank.text = "${position + 1}."
            binding.tvUsername.text = entry.username
            binding.tvScore.text = entry.score.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val binding = ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bind(scores[position], position)
    }

    override fun getItemCount(): Int = scores.size
}