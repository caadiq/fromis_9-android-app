package com.beemer.unofficial.fromis_9.model.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreModule(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "MyPrefs")

    private val notiMemberTime = booleanPreferencesKey("notiMemberTime")

    suspend fun setNotiMemberTime(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[notiMemberTime] = value
        }
    }

    fun getNotiMemberTime(): Flow<Boolean?> {
        return context.dataStore.data.map { prefs ->
            prefs[notiMemberTime]
        }
    }
}