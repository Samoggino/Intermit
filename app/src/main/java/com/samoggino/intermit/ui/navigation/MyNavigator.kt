package com.samoggino.intermit.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samoggino.intermit.ui.screens.HistoryScreen
import com.samoggino.intermit.ui.screens.HomeScreen
import com.samoggino.intermit.ui.screens.Screen
import com.samoggino.intermit.ui.screens.SettingsScreen
import com.samoggino.intermit.viewmodel.MainViewModel

@Composable
fun MyNavigator(
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Home.route) {
            HomeScreen(viewModel)
        }
        composable(Screen.History.route) {
            HistoryScreen(viewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
