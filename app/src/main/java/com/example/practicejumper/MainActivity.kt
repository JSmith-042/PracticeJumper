package com.example.practicejumper

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.practicejumper.ui.theme.PracticeJumperTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val gameViewModel : GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val gameState = gameViewModel.gameState.collectAsState()

            LaunchedEffect(Unit) {
                while (true) {
                    delay(16)

                    //update position stuff here
                    gameViewModel.gravityEffect()
                    if (gameViewModel.scrollDebris())
                        gameViewModel.addDebris()

                    if (gameViewModel.isCollision()) {
                        Toast.makeText(applicationContext, "BANG!", Toast.LENGTH_SHORT).show()
                        gameViewModel.addDebris()
                    }

                }
            }

            PracticeJumperTheme(darkTheme = true){
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    GameScreen(gameState.value, paddingValues = padding) { gameViewModel.jump() }
                }
            }
        }
    }
}