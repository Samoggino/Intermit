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
import com.samoggino.intermit.ui.screens.LiveUpdateSample
import com.samoggino.intermit.ui.screens.Screen
import com.samoggino.intermit.ui.screens.SettingsScreen
import com.samoggino.intermit.ui.screens.home.HomeScreen
import com.samoggino.intermit.viewmodel.HomeViewModel
import com.samoggino.intermit.viewmodel.HomeViewModelFactory
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

            val context = LocalContext.current
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.Home.route)
            }

            // DAO e Repository creati una sola volta con remember
            val dao = remember { AppDatabase.getDatabase(context).fastingDao() }
            val repository = remember { FastingRepository(dao) }

            // ViewModel per il timer
            val timerViewModel: TimerViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = TimerViewModelFactory(Plan.TWENTY_FOUR)
            )

            // ViewModel per il repository
            val sessionRepositoryViewModel = viewModel(
                modelClass = SessionRepositoryViewModel::class.java,
                factory = SessionRepositoryViewModelFactory(repository),
                viewModelStoreOwner = parentEntry
            )

            // Factory ricordata per evitare ricreazioni
            val homeViewModelFactory = remember(timerViewModel, sessionRepositoryViewModel) {
                HomeViewModelFactory(timerViewModel, sessionRepositoryViewModel)
            }

            // ViewModel coordinatore
            val homeViewModel = viewModel(
                modelClass = HomeViewModel::class.java,
                factory = homeViewModelFactory,
                viewModelStoreOwner = parentEntry
            )

            // Passaggio finale alla screen
            HomeScreen(homeViewModel)
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