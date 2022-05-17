package com.playrix.fishdomdd.gpl.data.source.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.playrix.fishdomdd.gpl.utils.LINK_PREFERENCES
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.linkPreferences by preferencesDataStore(name = LINK_PREFERENCES)

data class FilterLinkPreferences(
    val cachedLink: String
)

@Singleton
class LinkPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.linkPreferences
    val preferencesFlow = dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { preferences ->
            val cachedLink = preferences[LinkPreferencesKeys.CACHED_LINK] ?: ""
            FilterLinkPreferences(cachedLink)
        }

    suspend fun updateCachedLink(newLink: String) {
        dataStore.edit { preferences ->
            preferences[LinkPreferencesKeys.CACHED_LINK] = newLink
        }
    }

    object LinkPreferencesKeys {
        val CACHED_LINK = stringPreferencesKey("CACHED_LINK")
    }
}