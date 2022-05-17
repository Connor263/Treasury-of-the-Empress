package com.playrix.fishdomdd.gpl.utils

import com.playrix.fishdomdd.gpl.R
import com.playrix.fishdomdd.gpl.data.game.model.MemoryCard
import com.playrix.fishdomdd.gpl.utils.enums.CardBoardOptions

const val GAME_PREFERENCES = "GamePreferences"
const val LINK_PREFERENCES = "LinkPreferences"
const val BOARD_PREFERENCES = "BoardPreferences"

var MAX_MISTAKES_COUNT = 15
var MEMORY_CARDS_SIZE: Int = 0

fun getMemoryCards(boardOption: CardBoardOptions): List<MemoryCard> {
    val list = mutableListOf(
        MemoryCard(
            id = 1,
            drawableId = R.drawable.decal_1,
        ), MemoryCard(
            id = 2,
            drawableId = R.drawable.decal_1,
        ), MemoryCard(
            id = 3,
            drawableId = R.drawable.decal_2,
        ), MemoryCard(
            id = 4,
            drawableId = R.drawable.decal_2,
        ), MemoryCard(
            id = 5,
            drawableId = R.drawable.decal_3,
        ), MemoryCard(
            id = 6,
            drawableId = R.drawable.decal_3,
        )
    )

    val additionalList = listOf(
        MemoryCard(
            id = 7,
            drawableId = R.drawable.decal_4,
        ), MemoryCard(
            id = 8,
            drawableId = R.drawable.decal_4,
        ), MemoryCard(
            id = 9,
            drawableId = R.drawable.decal_5,
        ), MemoryCard(
            id = 10,
            drawableId = R.drawable.decal_5,
        ), MemoryCard(
            id = 11,
            drawableId = R.drawable.decal_6,
        ), MemoryCard(
            id = 12,
            drawableId = R.drawable.decal_6,
        ), MemoryCard(
            id = 13,
            drawableId = R.drawable.decal_7,
        ), MemoryCard(
            id = 14,
            drawableId = R.drawable.decal_7,
        ), MemoryCard(
            id = 15,
            drawableId = R.drawable.decal_8,
        ), MemoryCard(
            id = 16,
            drawableId = R.drawable.decal_8,
        ), MemoryCard(
            id = 17,
            drawableId = R.drawable.decal_9,
        ), MemoryCard(
            id = 18,
            drawableId = R.drawable.decal_9,
        ), MemoryCard(
            id = 19,
            drawableId = R.drawable.decal_10,
        ), MemoryCard(
            id = 20,
            drawableId = R.drawable.decal_10,
        )
    )

    when (boardOption) {
        CardBoardOptions.OPTION_4x5 -> {
            MAX_MISTAKES_COUNT = 15
            list.addAll(additionalList)
        }
        CardBoardOptions.OPTION_4x4 -> {
            MAX_MISTAKES_COUNT = 10
            repeat(10) {
                list.add(additionalList[it])
            }
        }
        CardBoardOptions.OPTION_3x3 -> {
            MAX_MISTAKES_COUNT = 5
            repeat(3) {
                list.add(additionalList[it])
            }
        }
        else -> {}
    }

    return list.toList().shuffled()
}