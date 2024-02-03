package ui.vm

import global.Global
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import models.GameTypes
import moe.tlaster.precompose.viewmodel.viewModelScope

class SettingsViewModel : BaseViewModel<SettingsState, SettingsEvent, SettingsEffect>() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val language = Global.appPreferences?.getLanguage()
            if (language != null) {
                emitState {
                    copy(language = language)
                }
            }
            val bgmVolume = Global.bgmVolumeFlow.first()
            emitState {
                copy(
                    bgmVolume = bgmVolume,
                    textSpeed = Global.textSpeedMillis
                )
            }
        }
    }

    override fun getInitState(): SettingsState {
        return SettingsState()
    }

    override fun dispatch(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ChangeLanguage -> {
                changeLanguage(event.language)
            }
            is SettingsEvent.SetTextSpeed -> {
                setTextSpeed(event.speed)
            }
            is SettingsEvent.VolumeDown -> {
                decreaseVolume()
            }
            is SettingsEvent.VolumeUp -> {
                increaseVolume()
            }
        }
    }

    private fun changeLanguage(language: Int) {
        Global.appPreferences?.let { appPref ->
            viewModelScope.launch(Dispatchers.IO) {
                appPref.setLanguage(language)
                emitState {
                    copy(language = language)
                }
            }
        }
    }

    private fun setTextSpeed(speed: String) {
        Global.appPreferences?.let { appPref ->
            viewModelScope.launch(Dispatchers.IO) {
                val speedMillis = GameTypes.TextSpeed.getMillis(speed)
                appPref.setTextSpeed(speedMillis)
                emitState {
                    copy(textSpeed = speedMillis)
                }
            }
        }
    }

    private fun decreaseVolume() {
        val state = uiState.value
        viewModelScope.launch(Dispatchers.IO) {
            if (state.bgmVolume - 1 >= Global.BGM_VOLUME_MIN) {
                Global.appPreferences?.setBGMVolume(state.bgmVolume - 1)
                emitState {
                    copy(bgmVolume = state.bgmVolume - 1)
                }
            }
        }
    }

    private fun increaseVolume() {
        val state = uiState.value
        viewModelScope.launch(Dispatchers.IO) {
            if (state.bgmVolume + 1 <= Global.BGM_VOLUME_MAX) {
                Global.appPreferences?.setBGMVolume(state.bgmVolume + 1)
                emitState {
                    copy(bgmVolume = state.bgmVolume + 1)
                }
            }
        }
    }
}

data class SettingsState(
    val language: Int = Global.LANGUAGE_JP,
    val bgmVolume: Int = Global.BGM_VOLUME_MAX,
    val textSpeed: Long = GameTypes.TextSpeed.getMillis(GameTypes.TextSpeed.Normal)
) : BaseState

sealed class SettingsEvent : BaseEvent {
    data class ChangeLanguage(val language: Int) : SettingsEvent()
    data class SetTextSpeed(val speed: String) : SettingsEvent()
    data object VolumeDown : SettingsEvent()
    data object VolumeUp : SettingsEvent()
}

sealed class SettingsEffect : BaseEffect