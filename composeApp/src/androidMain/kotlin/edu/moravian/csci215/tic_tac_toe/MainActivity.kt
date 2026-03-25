package edu.moravian.csci215.tic_tac_toe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(scrim = 0xFFF6F0F8.toInt(), darkScrim = 0xFF1D1B20.toInt()),
            navigationBarStyle = SystemBarStyle.light(scrim = 0xFFF6F0F8.toInt(), darkScrim = 0xFF1D1B20.toInt()),
        )
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
