package com.passive.gameexplorer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.passive.gameexplorer.model.ProfileField
import com.passive.gameexplorer.ui.screen.LanguageSelectorScreen
import com.passive.gameexplorer.ui.theme.GameExplorerTheme
import com.passive.gameexplorer.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageActivity : ComponentActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameExplorerTheme {
                LanguageSelectorScreen(
                    languages = listOf(
                        "English" to "🇬🇧",
                        "Hindi" to "🇮🇳",
                    ),
                    onLanguageSelected = {},
                    onSubmit = {
                        viewModel.saveField(
                            field = ProfileField.LANGUAGE,
                            value = it,
                            onSuccess = {
                                startActivity(Intent(this, GameLevelActivity::class.java))
                            },
                            onFailure = {
                                Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                )
            }
        }
    }
}
