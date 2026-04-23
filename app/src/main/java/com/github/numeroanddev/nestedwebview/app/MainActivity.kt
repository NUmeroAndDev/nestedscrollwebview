package com.github.numeroanddev.nestedwebview.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.github.numeroanddev.nestedwebview.app.theme.TemplateAppTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            TemplateAppTheme {
                Surface(
                    color = Color.Black
                ) {
                    SampleAppNavigation(
                        navController = navController,
                    )
                }
            }
        }
    }
}