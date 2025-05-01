package com.samoggino.intermit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.samoggino.intermit.data.database.AppDatabase
import com.samoggino.intermit.data.repository.FastingRepository
import com.samoggino.intermit.ui.screens.MainScreen
import com.samoggino.intermit.ui.theme.IntermitTheme
import com.samoggino.intermit.viewmodel.MainViewModel
import com.samoggino.intermit.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val repository = FastingRepository(db.fastingDao())
        val factory = MainViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setContent {
            IntermitTheme(darkTheme = true) {
                MainScreen(viewModel)
            }
        }
    }
}