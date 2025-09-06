package com.samoggino.intermit.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samoggino.intermit.core.TimerConfig
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.ui.screens.HistoryScreen
import com.samoggino.intermit.ui.screens.LiveUpdateSample
import com.samoggino.intermit.ui.screens.Screen
import com.samoggino.intermit.ui.screens.SettingsScreen
import com.samoggino.intermit.ui.screens.home.HomeScreen
import com.samoggino.intermit.viewmodel.HistoryViewModel
import com.samoggino.intermit.viewmodel.HomeViewModel
import com.samoggino.intermit.viewmodel.SessionRepositoryViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MyNavigator(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(innerPadding)
    ) {

        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = koinViewModel {
                parametersOf(TimerConfig.DEFAULT_PLAN) // o qualsiasi Plan dinamico
            }

            HomeScreen(homeViewModel)
        }

        composable(Screen.History.route) {
            val sessionViewModel: SessionRepositoryViewModel = koinViewModel()
            val historyViewModel: HistoryViewModel = koinViewModel {
                parametersOf(sessionViewModel)
            }
            HistoryScreen(sessionViewModel = sessionViewModel, historyViewModel = historyViewModel)
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(Screen.TestNotification.route) {
            LiveUpdateSample()
        }
    }
}
