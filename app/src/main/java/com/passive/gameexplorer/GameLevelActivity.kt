package com.passive.gameexplorer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.passive.gameexplorer.model.ProfileField
import com.passive.gameexplorer.ui.screen.GameLevelSelectorScreen
import com.passive.gameexplorer.ui.theme.GameExplorerTheme
import com.passive.gameexplorer.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameLevelActivity : ComponentActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    private var level: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameExplorerTheme {
                GameLevelSelectorScreen(
                    levels = listOf("0-25", "26-50", "51-75", "76-100", "100+"),
                    onLevelSelected = {
                        level = it
                    },
                    onSubmit = {
                        viewModel.saveField(
                            field = ProfileField.GAME_LEVEL,
                            value = level ?: "",
                            onFailure = {
                                Toast.makeText(this, "Failed to save Level", Toast.LENGTH_SHORT)
                                    .show()
                            },
                            onSuccess = {
                                startActivity(Intent(this, FavoriteGameActivity::class.java))
                            }
                        )
                    }
                )
            }
        }
    }
}
