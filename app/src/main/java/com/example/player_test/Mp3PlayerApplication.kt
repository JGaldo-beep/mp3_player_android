package com.example.player_test

import android.app.Application
import com.example.player_test.di.DependencyContainer

class Mp3PlayerApplication : Application() {
    
    lateinit var dependencyContainer: DependencyContainer
        private set
    
    override fun onCreate() {
        super.onCreate()
        dependencyContainer = DependencyContainer(this)
    }
}