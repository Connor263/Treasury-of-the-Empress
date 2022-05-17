package com.playrix.fishdomdd.gpl.data.source.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.playrix.fishdomdd.gpl.data.game.model.GameEndResult
import com.playrix.fishdomdd.gpl.utils.GAME_PREFERENCES
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.gamePreferences by preferencesDataStore(name = GAME_PREFERENCES)

data class FilterGameResultsPreferences(
    val results: List<GameEndResult>
)

@Singleton
class GameResultsPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.gamePreferences
    val preferencesFlow = dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { preferences ->
            val gameResults = preferences.asMap().values.map {
                GameEndResult.fromString(it as String)
            }

            FilterGameResultsPreferences(gameResults)
        }

    suspend fun updateGameResults(gameResult: GameEndResult) {
        dataStore.edit { preferences ->
            val allResults = preferences.asMap().values
            val lastIndex = if (allResults.isNotEmpty()) {
                GameEndResult.fromString(allResults.last() as String).id
            } else {
                0
            }

            gameResult.id = if (lastIndex >= 10) {
                preferences.clear()
                0
            } else {
                lastIndex + 1
            }

            val gameResultKey = stringPreferencesKey(gameResult.id.toString())
            preferences[gameResultKey] = gameResult.toString()
        }
    }
}