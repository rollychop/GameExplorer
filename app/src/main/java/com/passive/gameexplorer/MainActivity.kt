package com.passive.gameexplorer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.passive.gameexplorer.ui.screen.MainScreen
import com.passive.gameexplorer.ui.theme.GameExplorerTheme
import com.passive.gameexplorer.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameExplorerTheme {
                MainScreen(
                    gameTypes = listOf("Free Fire", "BGMI"),
                    onGameTypeSelected = { selectedGame ->
                        startActivity(
                            Intent(this, GameIdListActivity::class.java)
                                .apply {
                                    putExtra(GameIdListActivity.GAME_TYPE_KEY, selectedGame)
                                }
                        )
                    }
                )
            }
        }
    }
}
