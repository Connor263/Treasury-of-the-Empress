package com.playrix.fishdomdd.gpl.data.game.model

import android.os.Parcelable
import com.playrix.fishdomdd.gpl.utils.enums.CardBoardOptions
import com.playrix.fishdomdd.gpl.utils.enums.GameEndType
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameEndResult(
    var id: Int = 0,
    val result: GameEndType,
    val matched: Int,
    val mistakes: Int,
    val boardOption: CardBoardOptions
) : Parcelable {
    override fun toString(): String {
        return "${this.id},${this.result.name},${this.matched},${this.mistakes},${this.boardOption.name}"
    }

    companion object {
        fun fromString(value: String): GameEndResult {
            val splitValue = value.split(",").toList()
            return GameEndResult(
                id = splitValue[0].toInt(),
                result = GameEndType.valueOf(splitValue[1]),
                matched = splitValue[2].toInt(),
                mistakes = splitValue[3].toInt(),
                boardOption = CardBoardOptions.valueOf(splitValue[4])
            )
        }
    }
}
