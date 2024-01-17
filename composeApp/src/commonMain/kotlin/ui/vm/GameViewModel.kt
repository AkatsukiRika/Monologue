package ui.vm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.GameModels
import models.GameTypes
import moe.tlaster.precompose.viewmodel.viewModelScope
import parseScenarioXML
import playAudioFile
import stopAudio

class GameViewModel : BaseViewModel<GameState, GameEvent, GameEffect>() {
    companion object {
        const val AUDIO_BGM_MAIN = "main_bgm.mp3"
    }

    private var scenario: GameModels.Scenario? = null

    private var currentScene: GameModels.Scene? = null

    private var currentSceneElement: GameModels.SceneElement? = null

    init {
        playAudioFile(fileName = AUDIO_BGM_MAIN)
    }

    override fun getInitState(): GameState {
        return GameState()
    }

    override fun dispatch(event: GameEvent) {
        when (event) {
            is GameEvent.LoadScenario -> {
                startParse(event.data)
            }

            is GameEvent.StopBGM -> {
                stopAudio()
            }

            is GameEvent.ClickNext -> {
                clickNext()
            }

            is GameEvent.ShowSettings -> {
                emitState {
                    copy(showSettingsScreen = event.show)
                }
            }
        }
    }

    private fun startParse(data: String) {
        viewModelScope.launch(Dispatchers.IO) {
            scenario = parseScenarioXML(data)
            startGame()
        }
    }

    private suspend fun startGame() {
        delay(300)

        scenario?.scenes?.takeIf { it.isNotEmpty() }?.let { scenes ->
            currentScene = scenes[0]
            handleCurrentScene()

            currentScene?.elements?.takeIf { it.isNotEmpty() }?.let { element ->
                currentSceneElement = element.first()
                handleCurrentSceneElement()
            }
        }
    }

    private fun clickNext() {
        viewModelScope.launch(Dispatchers.IO) {
            scenario?.scenes?.let { scenes ->
                val currentSceneIndex = scenes.indexOf(currentScene)
                if (currentSceneIndex == scenes.indices.last) {
                    // TODO: 结束游戏
                } else {
                    currentScene?.elements?.let { elements ->
                        val currentElementIndex = elements.indexOf(currentSceneElement)
                        if (currentElementIndex == elements.indices.last) {
                            // 进入下一个Scene
                            val newScene = scenes[currentSceneIndex + 1]
                            currentScene = newScene
                            handleCurrentScene()
                            currentSceneElement = newScene.elements[0]
                            handleCurrentSceneElement()
                        } else {
                            // 进入下一句对话
                            val newElement = elements[currentElementIndex + 1]
                            currentSceneElement = newElement
                            handleCurrentSceneElement()
                        }
                    }
                }
            }
        }
    }

    private fun handleCurrentScene() {
        currentScene?.let {
            emitState {
                copy(
                    currentBack = it.back,
                    currentFront = it.front ?: "",
                    currentFrontAlpha = it.frontAlpha
                )
            }
        }
    }

    private fun handleCurrentSceneElement() {
        currentSceneElement?.let { element ->
            if (element is GameModels.Text) {
                when (element.type) {
                    GameTypes.Text.SpecialWhite -> {
                        handleSpecialWhiteText(text = element.content)
                    }

                    GameTypes.Text.SpecialRed -> {
                        handleSpecialRedText(text = element.content)
                    }

                    GameTypes.Text.Speech -> {
                        handleSpeechText(
                            text = element.content,
                            characterName = element.character ?: "",
                            timeText = element.time ?: ""
                        )
                    }

                    GameTypes.Text.Scary -> {
                        handleScaryText(text = element.content)
                    }

                    GameTypes.Text.Normal -> {
                        handleNormalText(text = element.content)
                    }
                }
            } else if (element is GameModels.Effect) {
                emitEffect(GameEffect.ShowEffect(effect = element.type))
            }
        }
    }

    private fun handleSpecialWhiteText(text: String) {
        emitState {
            copy(
                showSpecialWhiteText = true,
                showSpeechText = false,
                showNormalText = false,
                showScaryText = false,
                showSpecialRedText = false,
                currentText = text
            )
        }
    }

    private fun handleSpecialRedText(text: String) {
        emitState {
            copy(
                showSpecialWhiteText = false,
                showSpeechText = false,
                showNormalText = false,
                showScaryText = false,
                showSpecialRedText = true,
                currentText = text
            )
        }
    }

    private fun handleSpeechText(text: String, characterName: String, timeText: String) {
        emitState {
            copy(
                showSpecialWhiteText = false,
                showSpeechText = true,
                showNormalText = false,
                showScaryText = false,
                showSpecialRedText = false,
                currentText = text,
                characterName = characterName,
                timeText = timeText
            )
        }
    }

    private fun handleScaryText(text: String) {
        emitState {
            copy(
                showSpecialWhiteText = false,
                showSpeechText = false,
                showNormalText = false,
                showScaryText = true,
                showSpecialRedText = false,
                currentText = text
            )
        }
    }

    private fun handleNormalText(text: String) {
        emitState {
            copy(
                showSpecialWhiteText = false,
                showSpeechText = false,
                showNormalText = true,
                showScaryText = false,
                showSpecialRedText = false,
                currentText = text
            )
        }
    }
}

data class GameState(
    val showSpecialWhiteText: Boolean = false,
    val showSpeechText: Boolean = false,
    val showNormalText: Boolean = false,
    val showScaryText: Boolean = false,
    val showSpecialRedText: Boolean = false,
    val showSettingsScreen: Boolean = false,
    val currentBack: String = "",
    val currentFront: String = "",
    val currentFrontAlpha: Float = 1f,
    val currentText: String = "",
    val characterName: String = "",
    val timeText: String = ""
) : BaseState

sealed class GameEvent : BaseEvent {
    data object ClickNext : GameEvent()
    data object StopBGM : GameEvent()
    data class LoadScenario(val data: String) : GameEvent()
    data class ShowSettings(val show: Boolean) : GameEvent()
}

sealed class GameEffect : BaseEffect {
    data class ShowEffect(val effect: String) : GameEffect()
}