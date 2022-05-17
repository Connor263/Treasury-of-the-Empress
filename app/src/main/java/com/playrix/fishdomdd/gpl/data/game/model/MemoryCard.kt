package com.playrix.fishdomdd.gpl.data.game.model

import androidx.annotation.DrawableRes

data class MemoryCard(
    val id: Int,
    @DrawableRes val drawableId: Int,
    var isSelected: Boolean = false,
    var isRemoved: Boolean = false
)
