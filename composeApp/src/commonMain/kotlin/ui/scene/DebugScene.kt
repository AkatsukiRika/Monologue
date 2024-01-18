package ui.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import createDataStore
import global.AppPreferences
import global.Global
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import playAudioFile

@Composable
fun DebugScene(navigator: Navigator) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var displayText by remember { mutableStateOf("") }

        Button(onClick = {
            playAudioFile(fileName = "main_bgm.mp3")
        }) {
            Text("Play Audio")
        }

        Button(onClick = {
            createDataStore().let { dataStore ->
                Global.dataStore = dataStore
                Global.appPreferences = AppPreferences(dataStore)
            }
        }) {
            Text("Create DataStore")
        }

        Button(onClick = {
            scope.launch {
                Global.appPreferences?.setTestString("Some String")
            }
        }) {
            Text("Save to DataStore")
        }

        Button(onClick = {
            scope.launch {
                val value = Global.appPreferences?.getTestString()
                displayText = value ?: "null"
            }
        }) {
            Text("Get from DataStore")
        }

        Text(text = displayText, textAlign = TextAlign.Center, color = Color.White)
    }
}