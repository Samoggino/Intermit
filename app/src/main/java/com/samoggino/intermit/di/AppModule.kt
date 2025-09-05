package com.samoggino.intermit.di

import com.samoggino.intermit.data.database.AppDatabase
import com.samoggino.intermit.data.model.Plan
import com.samoggino.intermit.data.repository.FastingRepository
import com.samoggino.intermit.viewmodel.HomeViewModel
import com.samoggino.intermit.viewmodel.SessionRepositoryViewModel
import com.samoggino.intermit.viewmodel.TimerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val appModule = module {
    // Repository
    single { FastingRepository(get()) } // get() risolve FastingDao

    // TimerViewModel
    factory { (plan: Plan) -> TimerViewModel(plan) }

    // SessionRepositoryViewModel
    viewModel { SessionRepositoryViewModel(get()) }

    // HomeViewModel
    viewModel { (plan: Plan) ->
        HomeViewModel(
            timerViewModel = get { parametersOf(plan) },
            sessionViewModel = get()
        )
    }
}

val databaseModule = module {

    single {
        // Crea il database usando la tua classe AppDatabase
        AppDatabase.getDatabase(androidContext())
    }

    single { get<AppDatabase>().fastingDao() } // fornisci il DAO
}


