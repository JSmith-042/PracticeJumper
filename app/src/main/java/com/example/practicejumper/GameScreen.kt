package com.example.practicejumper

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun GameScreen(gameState: GameState, paddingValues: PaddingValues, onTapEvent: () -> Unit) {
    Canvas(modifier = Modifier.fillMaxSize().padding(paddingValues).clickable {
        onTapEvent()
    }) {
        drawRect(
            Color.White,
            Offset(
                gameState.playerData.location.xCoord,
                gameState.playerData.location.yCoord
            ),
            Size(gameState.playerData.width, gameState.playerData.height)
        )

        drawRect(
            Color.Gray,
            topLeft = Offset(0f, 830f),
            Size(10000f, 20f)
        )

        gameState.debrisList.forEach {
            drawRect(
                Color.Red,
                Offset(
                    it.debrisLocation.xCoord,
                    it.debrisLocation.yCoord
                ),
                Size(it.width, it.height)
            )
        }
    }
    HorizontalDivider(Modifier.fillMaxWidth())
}