package com.passive.gameexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.passive.gameexplorer.ui.screen.GameDetailScreen
import com.passive.gameexplorer.ui.theme.GameExplorerTheme
import com.passive.gameexplorer.viewmodels.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameIdDetailActivity : ComponentActivity() {

    companion object {
        const val GAME_TYPE_KEY = "game.type.key"
        const val GAME_ID_KEY = "game.id.key"
    }

    private val viewModel: GameViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameType = intent.getStringExtra(GAME_TYPE_KEY) ?: "bgmi"
        val gameId = intent.getStringExtra(GAME_ID_KEY) ?: run {
            finish()
            ""
        }
        enableEdgeToEdge()
        setContent {
            GameExplorerTheme {
                GameDetailScreen(
                    gameId = gameId,
                    gameType = gameType,
                    gameViewModel = viewModel
                )
            }
        }
    }
}
