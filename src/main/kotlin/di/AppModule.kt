package di

import viewmodel.MainViewModel
import org.koin.dsl.module

val appModule = module {
    single { MainViewModel() }
}