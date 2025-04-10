package com.passive.gameexplorer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.passive.gameexplorer.model.ProfileField
import com.passive.gameexplorer.ui.screen.TopPlayerInputScreen
import com.passive.gameexplorer.ui.theme.GameExplorerTheme
import com.passive.gameexplorer.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopPlayerNameActivity : ComponentActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    private var name: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameExplorerTheme {
                TopPlayerInputScreen(
                    onPlayerNameEntered = {
                        name = it
                    },
                    onSubmit = {
                        viewModel.saveField(
                            field = ProfileField.TOP_PLAYER,
                            value = name ?: "",
                            onSuccess = {
                                startActivity(Intent(this, MainActivity::class.java))
                            },
                            onFailure = {
                                Toast.makeText(
                                    this,
                                    "Failed to save game level",
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
