package com.playrix.fishdomdd.gpl.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playrix.fishdomdd.gpl.R
import com.playrix.fishdomdd.gpl.data.game.model.GameEndResult
import com.playrix.fishdomdd.gpl.databinding.ItemResultBinding
import com.playrix.fishdomdd.gpl.utils.MEMORY_CARDS_SIZE
import com.playrix.fishdomdd.gpl.utils.enums.GameEndType

class ResultsAdapter : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {
    private var listOfResults: List<GameEndResult>? = null

    override fun getItemCount() = listOfResults?.size ?: 0

    inner class ViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GameEndResult) = with(binding.root.context) {

            binding.tvResult.text =
                when (item.result) {
                    GameEndType.WIN -> {
                        resources.getString(
                            R.string.result_win,
                            item.boardOption.name,
                            if (item.matched == MEMORY_CARDS_SIZE) {
                                resources.getString(R.string.matched_cards_all)
                            } else {
                                resources.getString(
                                    R.string.matched_cards, item.matched,
                                    MEMORY_CARDS_SIZE
                                )
                            },
                            item.mistakes
                        )
                    }
                    GameEndType.LOSE -> {
                        resources.getString(
                            R.string.result_lose,
                            item.boardOption.name,
                            item.matched
                        )
                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfResults?.get(position)
        item?.let { holder.bind(it) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<GameEndResult>) {
        listOfResults = list
        notifyDataSetChanged()
    }
}