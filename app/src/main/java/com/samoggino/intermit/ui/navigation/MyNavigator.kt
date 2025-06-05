package com.samoggino.intermit.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.samoggino.intermit.data.database.AppDatabase
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.repository.FastingRepository
import com.samoggino.intermit.ui.screens.HistoryScreen
import com.samoggino.intermit.ui.screens.HomeScreen
import com.samoggino.intermit.ui.screens.LiveUpdateSample
import com.samoggino.intermit.ui.screens.Screen
import com.samoggino.intermit.ui.screens.SettingsScreen
import com.samoggino.intermit.viewmodel.SessionRepositoryViewModel
import com.samoggino.intermit.viewmodel.SessionRepositoryViewModelFactory
import com.samoggino.intermit.viewmodel.TimerViewModel
import com.samoggino.intermit.viewmodel.TimerViewModelFactory

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
        composable(Screen.Home.route) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Home.route)
            }

            val timerViewModel: TimerViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = TimerViewModelFactory(Plan.TWENTY_FOUR)
            )

            val sessionRepositoryViewModel = viewModel(
                modelClass = SessionRepositoryViewModel::class.java,
                factory = SessionRepositoryViewModelFactory(
                    FastingRepository(AppDatabase.getDatabase(LocalContext.current).fastingDao())
                ),
                viewModelStoreOwner = parentEntry
            )

            HomeScreen(timerViewModel, sessionRepositoryViewModel)
        }

        composable(Screen.History.route) { backStackEntry ->
            val context = LocalContext.current
            val viewModel = viewModel(
                modelClass = SessionRepositoryViewModel::class.java,
                factory = SessionRepositoryViewModelFactory(
                    FastingRepository(AppDatabase.getDatabase(context).fastingDao())
                ),
                viewModelStoreOwner = backStackEntry
            )
            HistoryScreen(viewModel = viewModel)
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(Screen.TestNotification.route) {
            LiveUpdateSample()
        }
    }
}

