package com.example.practicejumper

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.abs
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {
    private val _gameState = MutableStateFlow(
        GameState(
            Player(Location(1000f, 800f), 20f, 20f),
            listOf(Debris(Location(RIGHT_SIDE, GROUND_LEVEL - 20f), 20f, 50f))
        )
    )
    val gameState = _gameState.asStateFlow()

    private var debrisCount = 1

    fun jump() {
        _gameState.update {
            if (it.playerData.location.yCoord >= GROUND_LEVEL) {
                it.copy(playerData = it.playerData.copy(location = it.playerData.location.copy(yCoord = it.playerData.location.yCoord - 150f)))
            } else
                return
        }
    }

    fun gravityEffect() {
        _gameState.update {
            if (it.playerData.location.yCoord < GROUND_LEVEL) {
                it.copy(playerData = it.playerData.copy(location = it.playerData.location.copy(yCoord = it.playerData.location.yCoord + 2f)))
            } else
                return
        }
    }

    fun scrollDebris() : Boolean {
        var removeFlag = false
        _gameState.update {
            val debrisList = mutableListOf<Debris>()


            it.debrisList.forEach { debris ->
                if (debris.debrisLocation.xCoord > 0) {
                    val debrisScrollSpeed = Random.nextFloat() * 5
                    debrisList.add(debris.copy(debrisLocation = debris.debrisLocation.copy(xCoord = debris.debrisLocation.xCoord - debrisScrollSpeed)))
                } else {
                    removeFlag = true
                }
            }
                it.copy(
                    debrisList = debrisList.toList()
                )
        }

        return removeFlag
    }

    fun addDebris() {
            val debrisList = mutableListOf<Debris>()

            for (i in 1..debrisCount) {
                debrisList.add(Debris(Location(RIGHT_SIDE + i * 500, GROUND_LEVEL - 20f), 20f, 50f))
            }

        debrisCount++
        _gameState.update {
            it.copy(debrisList = debrisList.toList())
        }
    }

    fun isCollision(): Boolean {
        val debrisList = mutableListOf<Debris>()
        var collideFlag = false

        gameState.value.debrisList.forEach { debris ->
            if (debris.collidesWith(gameState.value.playerData)) {
                collideFlag = true
            }
            else
                debrisList.add(debris)
        }

        _gameState.update {
            it.copy(debrisList = debrisList.toList())
        }

        return collideFlag
    }

    companion object {
        const val GROUND_LEVEL = 800f
        const val RIGHT_SIDE = 2400f
    }
}

data class GameState(val playerData: Player, val debrisList: List<Debris>)

data class Debris(val debrisLocation: Location, val width: Float, val height: Float) {
    fun collidesWith(playerToCompare: Player): Boolean {
        val xCollide = (abs(playerToCompare.location.xCoord + playerToCompare.width - debrisLocation.xCoord)) < width
        val yCollide = (abs(playerToCompare.location.yCoord - debrisLocation.yCoord + 20f)) < height
        return xCollide && yCollide
    }
}

data class Location(val xCoord: Float, val yCoord: Float)

data class Player(val location: Location, val width: Float, val height: Float)
