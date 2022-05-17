package com.playrix.fishdomdd.gpl.ui.game.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.playrix.fishdomdd.gpl.data.source.local.preferences.GameResultsPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    gameResultsPreferences: GameResultsPreferences
) : ViewModel() {
    private val gamePreferencesFlow = gameResultsPreferences.preferencesFlow

    val gameResults get() = combine(gamePreferencesFlow) { it.first().results }.asLiveData()
}