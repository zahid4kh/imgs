package di

import data.Database
import viewmodel.MainViewModel
import org.koin.dsl.module

val appModule = module {
    single { Database() }
    single { MainViewModel(get()) }
}