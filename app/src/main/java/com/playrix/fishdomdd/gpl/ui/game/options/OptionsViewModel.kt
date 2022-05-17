package com.playrix.fishdomdd.gpl.ui.game.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playrix.fishdomdd.gpl.data.source.local.preferences.BoardPreferences
import com.playrix.fishdomdd.gpl.di.module.IODispatcher
import com.playrix.fishdomdd.gpl.utils.enums.CardBoardOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(
    private val boardPreferences: BoardPreferences,
    @IODispatcher val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _bufferOption = MutableLiveData(CardBoardOptions.OPTION_4x5)
    val bufferOption: LiveData<CardBoardOptions> = _bufferOption

    fun setBufferOption() = viewModelScope.launch(ioDispatcher) {
        _bufferOption.postValue(boardPreferences.preferencesFlow.first().boardOption)
    }

    fun saveBufferOption(options: CardBoardOptions) {
        _bufferOption.value = options
    }

    fun saveOption() = viewModelScope.launch {
        boardPreferences.updateBoardOption(bufferOption.value!!)
    }
}