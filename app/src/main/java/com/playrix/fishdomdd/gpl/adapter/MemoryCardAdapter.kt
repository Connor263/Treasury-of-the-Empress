package com.playrix.fishdomdd.gpl.adapter

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.playrix.fishdomdd.gpl.R
import com.playrix.fishdomdd.gpl.data.game.model.MemoryCard
import com.playrix.fishdomdd.gpl.databinding.ItemGameCardBinding

class MemoryCardAdapter(private val listener: TreGameCardAdapterInterface) :
    ListAdapter<MemoryCard, MemoryCardAdapter.ViewHolder>(
        AsyncDifferConfig.Builder(ItemCallback()).build()
    ) {
    private val listOfSelectedItems = mutableListOf<MemoryCard>()

    inner class ViewHolder(private val binding: ItemGameCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MemoryCard) {
            binding.root.background.alpha = 127
            binding.ivDrawable.apply {

                if (item.isRemoved) {
                    setImageResource(android.R.color.transparent)
                    binding.root.background = null
                } else {
                    setImageResource(
                        if (item.isSelected) {
                            item.drawableId
                        } else {
                            R.color.primaryLightColor
                        }
                    )

                    setOnClickListener {
                        processClickCard(item)
                    }
                }
            }
        }

        private fun processClickCard(item: MemoryCard) {
            if (listOfSelectedItems.size < 2 && !item.isSelected && !listOfSelectedItems.contains(
                    item
                )
            ) {
                listOfSelectedItems.add(item)
                listener.onClick(item)

                if (listOfSelectedItems.size == 2) {
                    object : CountDownTimer(2000, 1000) {
                        override fun onTick(p0: Long) {

                        }

                        override fun onFinish() {
                            val firstCard = listOfSelectedItems.first()
                            val secondCard = listOfSelectedItems.last()
                            listener.compareItems(firstCard, secondCard)
                            listOfSelectedItems.clear()
                        }
                    }.start()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemGameCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun submitList(list: List<MemoryCard>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    interface TreGameCardAdapterInterface {
        fun onClick(item: MemoryCard)
        fun compareItems(firstCard: MemoryCard, secondCard: MemoryCard)
    }


    class ItemCallback : DiffUtil.ItemCallback<MemoryCard>() {
        override fun areItemsTheSame(oldItem: MemoryCard, newItem: MemoryCard): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MemoryCard, newItem: MemoryCard): Boolean {
            return oldItem == newItem
        }

    }
}