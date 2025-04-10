package com.passive.gameexplorer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.passive.gameexplorer.ui.screen.GameListScreenRoot
import com.passive.gameexplorer.ui.theme.GameExplorerTheme
import com.passive.gameexplorer.viewmodels.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameIdListActivity : ComponentActivity() {

    companion object {
        const val GAME_TYPE_KEY = "game.type.key"
    }

    private val viewModel: GameViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameType = intent.getStringExtra(GAME_TYPE_KEY) ?: "BGMI"
        enableEdgeToEdge()
        setContent {
            GameExplorerTheme {
                GameListScreenRoot(
                    gameType = gameType,
                    gameViewModel = viewModel,
                    onGameClick = {
                        startActivity(
                            Intent(this, GameIdDetailActivity::class.java)
                                .apply {
                                    putExtra(GameIdDetailActivity.GAME_TYPE_KEY, gameType)
                                    putExtra(GameIdDetailActivity.GAME_ID_KEY, it.gameId)
                                }
                        )
                    },
                )
            }
        }
    }
}
