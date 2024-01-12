package ui.vm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.GameModels
import models.GameTypes
import moe.tlaster.precompose.viewmodel.viewModelScope
import parseScenarioXML

class GameViewModel : BaseViewModel<GameState, GameEvent, GameEffect>() {
    private var scenario: GameModels.Scenario? = null

    private var currentScene: GameModels.Scene? = null

    private var currentSceneElement: GameModels.SceneElement? = null

    override fun getInitState(): GameState {
        return GameState()
    }

    override fun dispatch(event: GameEvent) {
        when (event) {
            is GameEvent.StartGame -> {
                emitState {
                    copy(showStartScreen = false, showGameScreen = true)
                }
                startGame()
            }

            is GameEvent.BackToMenu -> {
                emitState {
                    copy(showStartScreen = true, showGameScreen = false, showSettingsSubScreen = false)
                }
            }

            is GameEvent.LoadScenario -> {
                startParse(event.data)
            }

            is GameEvent.ClickNext -> {
                clickNext()
            }

            is GameEvent.ShowSettings -> {
                emitState {
                    copy(showSettingsSubScreen = event.show)
                }
            }
        }
    }

    private fun startParse(data: String) {
        viewModelScope.launch(Dispatchers.IO) {
            scenario = parseScenarioXML(data)
        }
    }

    private fun startGame() {
        viewModelScope.launch(Dispatchers.IO) {
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
                copy(currentBack = it.back, currentFront = it.front ?: "")
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

                    GameTypes.Text.Speech -> {
                        handleSpeechText(
                            text = element.content,
                            characterName = element.character ?: "",
                            timeText = element.time ?: ""
                        )
                    }

                    GameTypes.Text.Normal -> {
                        handleNormalText(text = element.content)
                    }
                }
            } else if (element is GameModels.Effect) {
                handleEffect(type = element.type)
            }
        }
    }

    private fun handleSpecialWhiteText(text: String) {
        emitState {
            copy(
                showSpecialWhiteText = true,
                showSpeechText = false,
                showNormalText = false,
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
                currentText = text,
                characterName = characterName,
                timeText = timeText
            )
        }
    }

    private fun handleNormalText(text: String) {
        emitState {
            copy(
                showSpecialWhiteText = false,
                showSpeechText = false,
                showNormalText = true,
                currentText = text
            )
        }
    }

    private fun handleEffect(type: String) {
        when (type) {
            GameTypes.Effect.RedTurn -> {
                emitEffect(GameEffect.RedTurnEffect)
            }
            GameTypes.Effect.ClimaxBlink -> {
                emitEffect(GameEffect.ClimaxBlinkEffect)
            }
        }
    }
}

data class GameState(
    val showStartScreen: Boolean = true,
    val showGameScreen: Boolean = false,
    val showGameBlackScreen: Boolean = false,
    val showSpecialWhiteText: Boolean = false,
    val showSpeechText: Boolean = false,
    val showNormalText: Boolean = false,
    val showSettingsSubScreen: Boolean = false,
    val currentBack: String = "",
    val currentFront: String = "",
    val currentText: String = "",
    val characterName: String = "",
    val timeText: String = ""
) : BaseState

sealed class GameEvent : BaseEvent {
    data object StartGame : GameEvent()
    data object ClickNext : GameEvent()
    data object BackToMenu : GameEvent()
    data class LoadScenario(val data: String) : GameEvent()
    data class ShowSettings(val show: Boolean) : GameEvent()
}

sealed class GameEffect : BaseEffect {
    data object RedTurnEffect : GameEffect()
    data object ClimaxBlinkEffect : GameEffect()
}