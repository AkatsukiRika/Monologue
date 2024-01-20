package ui.vm

import global.Global
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
}

data class SettingsState(
    val language: Int = Global.LANGUAGE_JP
) : BaseState

sealed class SettingsEvent : BaseEvent {
    data class ChangeLanguage(val language: Int) : SettingsEvent()
}

sealed class SettingsEffect : BaseEffect