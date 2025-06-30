package com.example.sozialchatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.example.sozialchatapp.ui.theme.SozialChatAppTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.luminance
import com.example.sozialchatapp.ui.screens.LogScreen
import com.example.sozialchatapp.ui.screens.NewsScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SozialChatAppTheme {
                val systemUiController = rememberSystemUiController()
                val backgroundColor = MaterialTheme.colorScheme.background
                val useDarkIcons = backgroundColor.luminance() > 0.5f

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = backgroundColor,
                        darkIcons = useDarkIcons
                    )
                }
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable("login") {
                            LogScreen(
                                onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } }
                            )
                        }
                        composable("splash") {
                            SplashScreen(navController)
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("chat") {
                            ChatScreen()
                        }
                        composable("news") {
                            NewsScreen()
                        }
                    }
                }
            }
        }
    }
}