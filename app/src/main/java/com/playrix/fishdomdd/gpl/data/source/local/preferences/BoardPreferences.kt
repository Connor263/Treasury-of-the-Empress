package com.playrix.fishdomdd.gpl.data.source.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.playrix.fishdomdd.gpl.utils.BOARD_PREFERENCES
import com.playrix.fishdomdd.gpl.utils.enums.CardBoardOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.boardPreferences by preferencesDataStore(name = BOARD_PREFERENCES)

data class FilterBoardPreferences(
    val boardOption: CardBoardOptions
)

@Singleton
class BoardPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.boardPreferences
    val preferencesFlow = dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { preferences ->
            val boardOption = CardBoardOptions.valueOf(
                preferences[BoardPreferencesKeys.BOARD_OPTION] ?: CardBoardOptions.OPTION_4x5.name
            )
            FilterBoardPreferences(boardOption)
        }

    suspend fun updateBoardOption(options: CardBoardOptions) {
        dataStore.edit { preferences ->
            preferences[BoardPreferencesKeys.BOARD_OPTION] = options.name
        }
    }


    object BoardPreferencesKeys {
        val BOARD_OPTION = stringPreferencesKey("BOARD_OPTION")
    }
}