package com.example.colormakerapp

import android.app.Application
import android.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Extension property to create a single instance of DataStore per application
val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "color_maker_prefs")

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    // DataStore keys for persistence
    private val redValueKey = floatPreferencesKey("red_value")
    private val greenValueKey = floatPreferencesKey("green_value")
    private val blueValueKey = floatPreferencesKey("blue_value")
    private val redSwitchKey = booleanPreferencesKey("red_switch")
    private val greenSwitchKey = booleanPreferencesKey("green_switch")
    private val blueSwitchKey = booleanPreferencesKey("blue_switch")

    // LiveData for color and switch states
    private val _color = MutableLiveData<Int>(Color.BLACK)
    val color: LiveData<Int> = _color

    // Flow and LiveData for persisted values
    val redValue: Flow<Float> = getApplication<Application>().dataStore.data
        .map { preferences -> preferences[redValueKey] ?: 0f }

    val greenValue: Flow<Float> = getApplication<Application>().dataStore.data
        .map { preferences -> preferences[greenValueKey] ?: 0f }

    val blueValue: Flow<Float> = getApplication<Application>().dataStore.data
        .map { preferences -> preferences[blueValueKey] ?: 0f }

    val redSwitchState: Flow<Boolean> = getApplication<Application>().dataStore.data
        .map { preferences -> preferences[redSwitchKey] ?: true }

    val greenSwitchState: Flow<Boolean> = getApplication<Application>().dataStore.data
        .map { preferences -> preferences[greenSwitchKey] ?: true }

    val blueSwitchState: Flow<Boolean> = getApplication<Application>().dataStore.data
        .map { preferences -> preferences[blueSwitchKey] ?: true }

    // Function to update color and save to DataStore
    fun updateColor(red: Float, green: Float, blue: Float,
                    redChecked: Boolean, greenChecked: Boolean, blueChecked: Boolean) {
        // Update color in ViewModel
        val newColor = Color.rgb(
            (if (redChecked) red * 255 else 0).toInt(),
            (if (greenChecked) green * 255 else 0).toInt(),
            (if (blueChecked) blue * 255 else 0).toInt()
        )
        _color.value = newColor

        // Save values to DataStore
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { preferences ->
                preferences[redValueKey] = red
                preferences[greenValueKey] = green
                preferences[blueValueKey] = blue
                preferences[redSwitchKey] = redChecked
                preferences[greenSwitchKey] = greenChecked
                preferences[blueSwitchKey] = blueChecked
            }
        }
    }
}