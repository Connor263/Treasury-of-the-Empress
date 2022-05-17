package com.playrix.fishdomdd.gpl.ui.game.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playrix.fishdomdd.gpl.data.game.model.GameEndResult
import com.playrix.fishdomdd.gpl.data.source.local.preferences.GameResultsPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val gameResultsPreferences: GameResultsPreferences
) : ViewModel() {
    fun saveResult(gameResult: GameEndResult) = viewModelScope.launch {
        gameResultsPreferences.updateGameResults(gameResult)
    }
}