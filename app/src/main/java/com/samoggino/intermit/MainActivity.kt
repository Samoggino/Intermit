package com.samoggino.intermit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.samoggino.intermit.ui.navigation.MyNavigator
import com.samoggino.intermit.ui.screens.Screen
import com.samoggino.intermit.ui.theme.IntermitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IntermitTheme(darkTheme = true) {
                val navController = rememberNavController()
                val screens = listOf(Screen.Home, Screen.History, Screen.Settings)

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val currentDestination =
                                navController.currentBackStackEntryAsState().value?.destination
                            screens.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    label = { Text(screen.title) },
                                    selected = currentDestination?.route == screen.route,
                                    onClick = { navController.navigate(screen.route) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    MyNavigator(navController, innerPadding) // 👈 via il viewModel da qui
                }
            }
        }

    }
}