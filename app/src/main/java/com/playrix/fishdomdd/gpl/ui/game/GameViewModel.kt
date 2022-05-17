package com.playrix.fishdomdd.gpl.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playrix.fishdomdd.gpl.data.game.model.MemoryCard
import com.playrix.fishdomdd.gpl.data.source.local.preferences.BoardPreferences
import com.playrix.fishdomdd.gpl.utils.MAX_MISTAKES_COUNT
import com.playrix.fishdomdd.gpl.utils.MEMORY_CARDS_SIZE
import com.playrix.fishdomdd.gpl.utils.enums.CardBoardOptions
import com.playrix.fishdomdd.gpl.utils.enums.GameEndType
import com.playrix.fishdomdd.gpl.utils.getMemoryCards
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val boardPreferences: BoardPreferences
) : ViewModel() {
    private val _uiState = MutableStateFlow<GameEvent?>(null)
    val uiState = _uiState

    val cards = MutableLiveData<List<MemoryCard>>(listOf())

    private val _matched = MutableLiveData(0)
    val matched: LiveData<Int> = _matched

    private val _mistakes = MutableLiveData(0)
    val mistakes: LiveData<Int> = _mistakes

    private val _boardOption = MutableLiveData<CardBoardOptions>()
    val boardOption: LiveData<CardBoardOptions> = _boardOption

    fun setMemoryCards() = viewModelScope.launch(Dispatchers.Main.immediate) {
        val boardPreferencesFlow = boardPreferences.preferencesFlow
        _boardOption.value = boardPreferencesFlow.first().boardOption

        val memoryCards = getMemoryCards(boardOption.value!!)
        cards.value = memoryCards

        MEMORY_CARDS_SIZE = memoryCards.size
    }

    fun setItemSelected(item: MemoryCard) = viewModelScope.launch {
        cards.value?.find { it.id == item.id }?.isSelected = true
        updateMemoryCards()
    }

    fun processMemoryCards(
        firstCard: MemoryCard,
        secondCard: MemoryCard
    ) {
        val isDrawableMatches = firstCard.drawableId == secondCard.drawableId
        if (isDrawableMatches.not()) increaseMistakes()

        updateCardFields(firstCard, isDrawableMatches)
        updateCardFields(secondCard, isDrawableMatches)

        updateMemoryCards()
    }

    private fun updateCardFields(card: MemoryCard, isDrawableMatches: Boolean) {
        cards.value?.find {
            it.id == card.id
        }?.apply {
            if (isDrawableMatches) {
                isRemoved = true
                matchCard()
            }
            isSelected = false
        }
    }

    private fun matchCard() {
        matched.value?.let {
            _matched.value = it.plus(1)
        }
        if (matched.value == MEMORY_CARDS_SIZE) {
            sendGameEndEvent(GameEndType.WIN)
        }
    }

    private fun increaseMistakes() {
        mistakes.value?.let {
            _mistakes.value = it.plus(1)
        }

        if (mistakes.value == MAX_MISTAKES_COUNT) {
            sendGameEndEvent(GameEndType.LOSE)
        }
    }

    private fun updateMemoryCards() {
        cards.value = cards.value
    }


    fun sendGameEndEvent(gameEndType: GameEndType?, show: Boolean? = true) {
        _uiState.value = if (show == true) GameEvent.GameEnd(gameEndType!!) else null
    }

    sealed class GameEvent {
        data class GameEnd(val gameEndType: GameEndType) : GameEvent()
    }
}