package red.tetracube.cosynestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import red.tetracube.cosynestapp.ui.theme.CosyNestAppTheme
import red.tetracube.cosynestapp.ui.theme.md_theme_dark_surface
import red.tetracube.cosynestapp.ui.theme.md_theme_light_surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosyNestAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (isSystemInDarkTheme()) {
                        window?.statusBarColor = md_theme_dark_surface.toArgb()
                    } else {
                        window?.statusBarColor = md_theme_light_surface.toArgb()
                    }
                    CosyNestApplication()
                }
            }
        }
    }
}