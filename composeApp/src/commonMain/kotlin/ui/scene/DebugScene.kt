package ui.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import createDataStore
import moe.tlaster.precompose.navigation.Navigator
import playAudioFile

@Composable
fun DebugScene(navigator: Navigator) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            playAudioFile(fileName = "test.mp3")
        }) {
            Text("Play Audio")
        }

        Button(onClick = {
            createDataStore()
        }) {
            Text("Create DataStore")
        }
    }
}