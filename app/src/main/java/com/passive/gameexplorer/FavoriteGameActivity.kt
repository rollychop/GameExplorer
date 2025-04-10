package com.passive.gameexplorer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.passive.gameexplorer.model.ProfileField
import com.passive.gameexplorer.ui.screen.FavoriteGameSelectorScreen
import com.passive.gameexplorer.ui.theme.GameExplorerTheme
import com.passive.gameexplorer.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteGameActivity : ComponentActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    private var selectedGame: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameExplorerTheme {
                FavoriteGameSelectorScreen(
                    games = listOf("PUBG", "BGMI", "Free Fire"),
                    onGameSelected = {
                        selectedGame = it
                    },
                    onSubmit = {
                        viewModel.saveField(
                            field = ProfileField.FAVORITE_GAME,
                            value = selectedGame ?: "",
                            onSuccess = {
                                startActivity(
                                    Intent(this, TopPlayerNameActivity::class.java)
                                )
                            },
                            onFailure = {
                                Toast.makeText(
                                    this,
                                    "Failed to save Favorite game",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                )
            }
        }
    }
}
